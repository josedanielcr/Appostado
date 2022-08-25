package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.*;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.service.LigaService;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import cr.ac.cenfotec.appostado.web.rest.vm.AmigoDetailsVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Liga}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LigaResource {

    private final Logger log = LoggerFactory.getLogger(LigaResource.class);

    private static final String ENTITY_NAME = "liga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigaRepository ligaRepository;

    private final UserRepository userRepository;

    private final UsuarioRepository usuarioRepository;

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    private final LigaService ligaService;

    private final LigaUsuarioRepository ligaUsuarioRepository;

    private final TransaccionRepository transaccionRepository;

    private final AmigoRepository amigoRepository;

    public LigaResource(
        LigaRepository ligaRepository,
        UserRepository userRepository,
        LigaService ligaService,
        LigaUsuarioRepository ligaUsuarioRepository,
        UsuarioRepository usuarioRepository,
        CuentaUsuarioRepository cuentaUsuarioRepository,
        TransaccionRepository transaccionRepository,
        AmigoRepository amigoRepository
    ) {
        this.ligaRepository = ligaRepository;
        this.userRepository = userRepository;
        this.ligaService = ligaService;
        this.ligaUsuarioRepository = ligaUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.transaccionRepository = transaccionRepository;
        this.amigoRepository = amigoRepository;
    }

    /**
     * {@code POST  /ligas} : Create a new liga.
     *
     * @param liga the liga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liga, or with status {@code 400 (Bad Request)} if the liga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligas")
    public ResponseEntity<Liga> createLiga(@Valid @RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to save Liga : {}", liga);
        if (liga.getId() != null) {
            throw new BadRequestAlertException("A new liga cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<User> user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);

        LigaUsuario nuevo = new LigaUsuario();

        if (user.isPresent()) {
            Usuario usuario = usuarioRepository.findById(user.get().getId()).get();
            liga.setDescripcion(ligaService.processDescripcion(user.get().getLogin(), liga.getDescripcion()));
            nuevo.setUsuario(usuario);
        }

        Liga result = ligaRepository.save(liga);
        nuevo.setLiga(result);
        ligaUsuarioRepository.save(nuevo);

        return ResponseEntity
            .created(new URI("/api/ligas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligas/:id} : Updates an existing liga.
     *
     * @param id the id of the liga to save.
     * @param liga the liga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liga,
     * or with status {@code 400 (Bad Request)} if the liga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligas/{id}")
    public ResponseEntity<Liga> updateLiga(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Liga liga)
        throws URISyntaxException {
        log.debug("REST request to update Liga : {}, {}", id, liga);
        if (liga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                liga.setDescripcion(ligaService.processDescripcion(user.getLogin(), liga.getDescripcion()));
            });

        Liga result = ligaRepository.save(liga);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liga.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligas/:id} : Partial updates given fields of an existing liga, field will ignore if it is null
     *
     * @param id the id of the liga to save.
     * @param liga the liga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liga,
     * or with status {@code 400 (Bad Request)} if the liga is not valid,
     * or with status {@code 404 (Not Found)} if the liga is not found,
     * or with status {@code 500 (Internal Server Error)} if the liga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Liga> partialUpdateLiga(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Liga liga
    ) throws URISyntaxException {
        log.debug("REST request to partial update Liga partially : {}, {}", id, liga);
        if (liga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Liga> result = ligaRepository
            .findById(liga.getId())
            .map(existingLiga -> {
                if (liga.getNombre() != null) {
                    existingLiga.setNombre(liga.getNombre());
                }
                if (liga.getDescripcion() != null) {
                    existingLiga.setDescripcion(liga.getDescripcion());
                }
                if (liga.getFoto() != null) {
                    existingLiga.setFoto(liga.getFoto());
                }

                return existingLiga;
            })
            .map(ligaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liga.getId().toString())
        );
    }

    /**
     * {@code GET  /ligas} : get all the ligas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligas in body.
     */
    @GetMapping("/ligas")
    public List<Liga> getAllLigas() {
        log.debug("REST request to get all Ligas");
        return ligaRepository.findAll();
    }

    /**
     * {@code GET  /ligas/:id} : get the "id" liga.
     *
     * @param id the id of the liga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligas/{id}")
    public ResponseEntity<Liga> getLiga(@PathVariable Long id) {
        log.debug("REST request to get Liga : {}", id);
        Optional<Liga> liga = ligaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(liga);
    }

    /**
     * {@code DELETE  /ligas/:id} : delete the "id" liga.
     *
     * @param id the id of the liga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligas/{id}")
    public ResponseEntity<Void> deleteLiga(@PathVariable Long id) {
        log.debug("REST request to delete Liga : {}", id);
        ligaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/ligas/listar/misligas")
    public List<Liga> getAllMisLigas() {
        log.debug("REST request to get all Mis Ligas");
        List<Liga> misLigas = new ArrayList<>();

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                List<LigaUsuario> ligas = ligaUsuarioRepository.findAllByUsuario(usuarioRepository.findById(user.getId()).get());
                for (LigaUsuario l : ligas) {
                    if (ligaService.getLigaOwner(l.getLiga().getDescripcion()).equals(user.getLogin())) {
                        misLigas.add(l.getLiga());
                    }
                }
            });

        log.debug("Sending friendly leagues for User: {}", misLigas);

        return misLigas;
    }

    @GetMapping("/ligas/listar/ligasamigos")
    public List<Liga> getAllLigasAmigos() {
        log.debug("REST request to get all Ligas Amigos");

        List<Liga> ligasAmigos = new ArrayList<>();

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                List<LigaUsuario> ligas = ligaUsuarioRepository.findAllByUsuario(usuarioRepository.findById(user.getId()).get());
                for (LigaUsuario l : ligas) {
                    if (!ligaService.getLigaOwner(l.getLiga().getDescripcion()).equals(user.getLogin())) {
                        ligasAmigos.add(l.getLiga());
                    }
                }
            });

        log.debug("Sending friendly leagues for User: {}", ligasAmigos);

        return ligasAmigos;
    }

    @GetMapping("/ligas/ranking/{id}")
    public List<Ranking> getAllAmigosLiga(@PathVariable Long id) {
        log.debug("REST request to get all Friends from Liga");

        List<Ranking> rankingOficial = new ArrayList<>();

        List<LigaUsuario> ligaUsuarios = ligaUsuarioRepository.findAllByLiga(ligaRepository.findById(id).get());

        for (LigaUsuario l : ligaUsuarios) {
            CuentaUsuario cuenta = cuentaUsuarioRepository.findCuentaUsuarioByUsuario(l.getUsuario()).get();
            Ranking r = new Ranking();
            r.setNombreJugador(l.getUsuario().getUser().getLogin());
            r.setNacionalidad(l.getUsuario().getPais());
            r.setTotalCanjes(cuenta.getNumCanjes());
            r.setTotalGanadas(cuenta.getApuestasGanadas());
            r.setTotalPerdidas(cuenta.getApuestasTotales() - cuenta.getApuestasGanadas());
            if (cuenta.getApuestasTotales() > 0) {
                r.setRendimiento(((double) r.getTotalGanadas() / (double) cuenta.getApuestasTotales()) * 100);
            } else {
                r.setRendimiento(0);
            }

            float numCanjes = cuenta.getNumCanjes().floatValue();
            if (numCanjes > 0) {
                float creditosCanjeados = 0;
                List<Transaccion> canjeados = transaccionRepository.findAllByCuentaAndTipo(cuenta, "Canje");

                for (Transaccion t : canjeados) {
                    creditosCanjeados = creditosCanjeados + t.getMonto();
                }
                cuenta.setBalance(cuenta.getBalance() + creditosCanjeados);
            } else {
                r.setRecordNeto(cuenta.getBalance());
            }

            r.setFoto(l.getUsuario().getUser().getImageUrl());
            rankingOficial.add(r);
        }

        Collections.sort(rankingOficial);

        for (int i = 0; i < rankingOficial.size(); i++) {
            rankingOficial.get(i).setPosicion(i + 1);
        }

        return rankingOficial;
    }

    @GetMapping("/ligas/owner/{id}")
    public boolean getOwnerLigaAutentication(@PathVariable Long id) {
        log.debug("REST request to autentication of Liga Owner");
        boolean isOwner = false;

        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
        Optional<Liga> liga = ligaRepository.findById(id);
        if (liga.isPresent()) {
            if (ligaService.getLigaOwner(liga.get().getDescripcion()).equals(user.getLogin())) {
                isOwner = true;
            }
        }

        log.debug("autentication: {}", isOwner);

        return isOwner;
    }

    @GetMapping("/ligas/listaramigos/{id}")
    public List<AmigoDetailsVM> getAllLigasAmigos(@PathVariable Long id) {
        log.debug("REST request to get all availableAmigos");

        List<AmigoDetailsVM> amigosList = new ArrayList<>();
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAllByLiga(ligaRepository.findById(id).get());
        List<Usuario> usuariosAmigos = new ArrayList<>();

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                List<Amigo> amigos = amigoRepository.findAllByUsuario(usuarioRepository.findById(user.getId()).get());

                for (LigaUsuario l : ligaUsuarioList) {
                    usuariosAmigos.add(l.getUsuario());
                }

                for (Amigo amigo : amigos) {
                    Optional<Usuario> usuarioAmigoLiga = usuarioRepository.findById(amigo.getAmigo().getId());
                    boolean present = false;

                    if (usuarioAmigoLiga.isPresent()) {
                        present = usuariosAmigos.contains(usuarioAmigoLiga.get());
                    }

                    if (!present) {
                        AmigoDetailsVM amigoDetailsVM = new AmigoDetailsVM();
                        amigoDetailsVM.setLogin(usuarioAmigoLiga.get().getUser().getLogin());
                        amigosList.add(amigoDetailsVM);
                    }
                }
            });

        log.debug("Sending friends for User: {}", amigosList);

        return amigosList;
    }

    @PostMapping("/ligas/agregaramigo/{id}")
    public ResponseEntity<LigaUsuario> addAmigoNotificacion(@PathVariable Long id, @Valid @RequestBody String amigo)
        throws URISyntaxException {
        log.debug("REST request to create friendship notificacion: {}", amigo);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (!login.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<Liga> liga = ligaRepository.findById(id);
        Optional<User> userFriend = userRepository.findOneByLogin(amigo);
        Usuario usuarioFriend = new Usuario();
        LigaUsuario nuevo = new LigaUsuario();

        if (userFriend.isPresent()) {
            usuarioFriend = usuarioRepository.findById(userFriend.get().getId()).get();
        } else {
            throw new BadRequestAlertException("No existe ningún usuario registrado con ese nombre", ENTITY_NAME, "notfound");
        }

        Optional<LigaUsuario> validation = ligaUsuarioRepository.findByUsuarioAndLiga(usuarioFriend, liga.get());

        if (validation.isPresent()) {
            throw new BadRequestAlertException("Ya eres amigo de ese usuario", ENTITY_NAME, "amigoexists");
        } else {
            if (liga.isPresent()) {
                nuevo.setUsuario(usuarioFriend);
                nuevo.setLiga(liga.get());
                nuevo = ligaUsuarioRepository.save(nuevo);
            }
        }

        return ResponseEntity
            .created(new URI("/api/ligas/agregaramigo" + id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nuevo.getId().toString()))
            .body(nuevo);
    }

    @DeleteMapping("/ligas/abandonar/{id}")
    public ResponseEntity<Liga> deleteAmigoNombre(@PathVariable Long id) {
        log.debug("REST request to abandonar Liga : {}", id);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<Liga> liga = ligaRepository.findById(id);
        Optional<User> user = userRepository.findOneByLogin(login.get());
        Optional<Usuario> usuario = usuarioRepository.findById(user.get().getId());

        if (usuario.isPresent() && liga.isPresent()) {
            Optional<LigaUsuario> ligaUsuario = ligaUsuarioRepository.findByUsuarioAndLiga(usuario.get(), liga.get());

            if (ligaUsuario.isPresent()) {
                ligaUsuarioRepository.delete(ligaUsuario.get());
            }
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, String.valueOf(liga)))
            .build();
    }
}
