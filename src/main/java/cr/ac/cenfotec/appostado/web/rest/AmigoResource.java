package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.*;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import cr.ac.cenfotec.appostado.web.rest.vm.AmigoDetailsVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Amigo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AmigoResource {

    private final Logger log = LoggerFactory.getLogger(AmigoResource.class);

    private static final String ENTITY_NAME = "amigo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmigoRepository amigoRepository;
    private final UserRepository userRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaUsuarioRepository cuentaRepository;
    private final NotificacionRepository notificacionRepository;

    public AmigoResource(
        AmigoRepository amigoRepository,
        UserRepository userRepository,
        UsuarioRepository usuarioRepository,
        CuentaUsuarioRepository cuentaRepository,
        NotificacionRepository notificacionRepository
    ) {
        this.amigoRepository = amigoRepository;
        this.userRepository = userRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * {@code POST  /amigos} : Create a new amigo.
     *
     * @param amigo the amigo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amigo, or with status {@code 400 (Bad Request)} if the amigo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amigos")
    public ResponseEntity<Amigo> createAmigo(@RequestBody Amigo amigo) throws URISyntaxException {
        log.debug("REST request to save Amigo : {}", amigo);
        if (amigo.getId() != null) {
            throw new BadRequestAlertException("A new amigo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Amigo result = amigoRepository.save(amigo);
        return ResponseEntity
            .created(new URI("/api/amigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amigos/:id} : Updates an existing amigo.
     *
     * @param id the id of the amigo to save.
     * @param amigo the amigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amigo,
     * or with status {@code 400 (Bad Request)} if the amigo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amigos/{id}")
    public ResponseEntity<Amigo> updateAmigo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Amigo amigo) {
        log.debug("REST request to update Amigo : {}, {}", id, amigo);
        if (amigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Amigo result = amigoRepository.save(amigo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amigo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amigos/:id} : Partial updates given fields of an existing amigo, field will ignore if it is null
     *
     * @param id the id of the amigo to save.
     * @param amigo the amigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amigo,
     * or with status {@code 400 (Bad Request)} if the amigo is not valid,
     * or with status {@code 404 (Not Found)} if the amigo is not found,
     * or with status {@code 500 (Internal Server Error)} if the amigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amigos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Amigo> partialUpdateAmigo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Amigo amigo) {
        log.debug("REST request to partial update Amigo partially : {}, {}", id, amigo);
        if (amigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Amigo> result = amigoRepository
            .findById(amigo.getId())
            .map(existingAmigo -> {
                return existingAmigo;
            })
            .map(amigoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amigo.getId().toString())
        );
    }

    /**
     * {@code GET  /amigos} : get all the amigos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amigos in body.
     */
    @GetMapping("/amigos")
    public List<Amigo> getAllAmigos() {
        log.debug("REST request to get all Amigos");
        return amigoRepository.findAll();
    }

    /**
     * {@code GET  /amigos/:id} : get the "id" amigo.
     *
     * @param id the id of the amigo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amigo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amigos/{id}")
    public ResponseEntity<Amigo> getAmigo(@PathVariable Long id) {
        log.debug("REST request to get Amigo : {}", id);
        Optional<Amigo> amigo = amigoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(amigo);
    }

    /**
     * {@code DELETE  /amigos/:id} : delete the "id" amigo.
     *
     * @param id the id of the amigo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amigos/{id}")
    public ResponseEntity<Void> deleteAmigo(@PathVariable Long id) {
        log.debug("REST request to delete Amigo : {}", id);
        amigoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/amigos/list")
    public List<AmigoDetailsVM> getMyAmigos() {
        log.debug("REST request to get all Amigos from an user account");
        List<AmigoDetailsVM> amigosList = new ArrayList<>();

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                List<Amigo> amigos = amigoRepository.findAllByUsuario(usuarioRepository.findById(user.getId()).get());
                for (Amigo amigo : amigos) {
                    Optional<User> usuarioAmigable = userRepository.findById(amigo.getAmigo().getId());
                    Optional<Usuario> usuarioAmigo = usuarioRepository.findById(amigo.getAmigo().getId());
                    log.debug("Found friend for User: {}", usuarioAmigo);
                    Optional<CuentaUsuario> cuenta = cuentaRepository.findCuentaUsuarioByUsuario_Id(amigo.getAmigo().getId());
                    AmigoDetailsVM amigoDetailsVM = new AmigoDetailsVM();
                    amigoDetailsVM.setLogin(usuarioAmigable.get().getLogin());
                    if (usuarioAmigable.get().getImageUrl() != null) {
                        amigoDetailsVM.setAvatar(usuarioAmigable.get().getImageUrl());
                    }
                    amigoDetailsVM.setCountry(usuarioAmigo.get().getPais());
                    amigoDetailsVM.setPerfil(usuarioAmigo.get().getNombrePerfil());
                    amigoDetailsVM.setBalance(cuenta.get().getBalance());
                    amigoDetailsVM.setGanados(cuenta.get().getApuestasGanadas());
                    amigoDetailsVM.setTotales(cuenta.get().getApuestasTotales());
                    amigoDetailsVM.setCanjes(cuenta.get().getNumCanjes());

                    amigosList.add(amigoDetailsVM);
                }
            });

        log.debug("Sending friends for User: {}", amigosList);

        return amigosList;
    }

    @PostMapping("/amigos/accept/{amigo}")
    public ResponseEntity<Amigo> addAmigo(@PathVariable String amigo) throws URISyntaxException {
        log.debug("REST request to accept Amigo : {}", amigo);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (!login.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<User> amigable = userRepository.findOneByLogin(login.get());
        Optional<User> newAmigo = userRepository.findOneByLogin(amigo);

        if (!newAmigo.isPresent()) {
            throw new BadRequestAlertException("No existe ningún usuario registrado con ese nombre", ENTITY_NAME, "notfound");
        }

        Amigo nuevo = new Amigo();
        Optional<Usuario> usuarioAmigable = usuarioRepository.findById(amigable.get().getId());
        Optional<Usuario> usuarioNewAmigo = usuarioRepository.findById(newAmigo.get().getId());
        nuevo.setUsuario(usuarioAmigable.get());
        nuevo.setAmigo(usuarioNewAmigo.get());

        if (amigoRepository.existsByUsuarioAndAmigo(nuevo.getUsuario(), nuevo.getAmigo())) {
            throw new BadRequestAlertException("Ya eres amigo de ese usuario", ENTITY_NAME, "amigoexists");
        }

        Optional<Notificacion> notificacion = notificacionRepository.findByUsuarioAndDescripcionAndHaGanado(
            usuarioNewAmigo.get(),
            login.get(),
            false
        );
        if (notificacion.isPresent()) {
            notificacion.get().setFueLeida(true);
            notificacion.get().setHaGanado(true);
            notificacionRepository.save(notificacion.get());
        } else {
            throw new BadRequestAlertException("No existe una solicitud de amistad para ese usuario", ENTITY_NAME, "unexistingrequest");
        }

        Amigo inverso = new Amigo();
        inverso.setUsuario(usuarioRepository.findById(newAmigo.get().getId()).get());
        inverso.setAmigo(usuarioRepository.findById(amigable.get().getId()).get());
        nuevo = amigoRepository.save(nuevo);

        amigoRepository.save(inverso);

        return ResponseEntity
            .created(new URI("/api/amigos/accept/" + amigo))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nuevo.getId().toString()))
            .body(nuevo);
    }

    @PutMapping("/amigos/cancel/{amigo}")
    public ResponseEntity<Notificacion> rejectAmigo(@PathVariable String amigo) throws URISyntaxException {
        log.debug("REST request to reject Amigo : {}", amigo);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (!login.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<User> rejected = userRepository.findOneByLogin(amigo);
        Optional<Usuario> usuarioRejected = usuarioRepository.findById(rejected.get().getId());

        Optional<Notificacion> notificacion = notificacionRepository.findByUsuarioAndDescripcionAndHaGanado(
            usuarioRejected.get(),
            login.get(),
            false
        );
        if (notificacion.isPresent()) {
            notificacion.get().setFueLeida(true);
            notificacion.get().setHaGanado(true);
            notificacionRepository.save(notificacion.get());
        } else {
            throw new BadRequestAlertException("No existe una solicitud de amistad para ese usuario", ENTITY_NAME, "unexistingrequest");
        }

        return ResponseEntity
            .created(new URI("/api/amigos/cancel/" + login.get()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificacion.get().getId().toString()))
            .body(notificacion.get());
    }

    @DeleteMapping("/amigos/eliminar/{amigo}")
    public ResponseEntity<Void> deleteAmigoNombre(@PathVariable String amigo) {
        log.debug("REST request to delete Amigo : {}", amigo);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (!login.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<User> amigable = userRepository.findOneByLogin(login.get());
        Optional<User> currentAmigo = userRepository.findOneByLogin(amigo);
        Optional<Usuario> usuarioAmigable = usuarioRepository.findById(amigable.get().getId());
        Optional<Usuario> usuarioAmigo = usuarioRepository.findById(currentAmigo.get().getId());

        amigoRepository.deleteByUsuarioAndAmigo(usuarioAmigable.get(), usuarioAmigo.get());
        amigoRepository.deleteByUsuarioAndAmigo(usuarioAmigo.get(), usuarioAmigable.get());

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, amigo)).build();
    }
}
