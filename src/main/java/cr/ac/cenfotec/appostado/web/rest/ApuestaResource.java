package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.EventoRepository;
import cr.ac.cenfotec.appostado.repository.UserRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.service.ApuestaService;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import cr.ac.cenfotec.appostado.web.rest.vm.EventCalculatedData;
import cr.ac.cenfotec.appostado.web.rest.vm.HistorialApuestaVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import liquibase.pro.packaged.E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Apuesta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApuestaResource {

    private final Logger log = LoggerFactory.getLogger(ApuestaResource.class);

    private static final String ENTITY_NAME = "apuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApuestaRepository apuestaRepository;

    private final UserRepository userRepository;

    private final EventoRepository eventoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ApuestaService apuestaService;

    public ApuestaResource(
        ApuestaRepository apuestaRepository,
        UserRepository userRepository,
        UsuarioRepository usuarioRepository,
        ApuestaService apuestaService,
        EventoRepository eventoRepository
    ) {
        this.apuestaRepository = apuestaRepository;
        this.userRepository = userRepository;
        this.usuarioRepository = usuarioRepository;
        this.apuestaService = apuestaService;
        this.eventoRepository = eventoRepository;
    }

    /**
     * {@code POST  /apuestas} : Create a new apuesta.
     *
     * @param apuesta the apuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apuesta, or with status {@code 400 (Bad Request)} if the apuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apuestas")
    public ResponseEntity<Apuesta> createApuesta(@Valid @RequestBody Apuesta apuesta) throws URISyntaxException {
        log.debug("REST request to save Apuesta : {}", apuesta);
        /*gets current user*/
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin.get());
        if (!currentUser.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado para realizar esta acción", ENTITY_NAME, "notfound");
        } else {
            if (apuesta.getId() != null) {
                throw new BadRequestAlertException("A new apuesta cannot already have an ID", ENTITY_NAME, "idexists");
            }
            apuesta.setUsuario(usuarioRepository.findUsuarioByUserId(currentUser.get().getId()).get());
            try {
                Apuesta apuestaRes = this.apuestaService.createApuesta(apuesta);
                return ResponseEntity
                    .created(new URI("/api/apuestas/" + apuestaRes.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, apuestaRes.getId().toString()))
                    .body(apuestaRes);
            } catch (Exception e) {
                throw new InternalError("Ha ocurrido un error durante el proceso de creación de la apuesta" + e.getMessage());
            }
        }
    }

    /**
     * {@code PUT  /apuestas/:id} : Updates an existing apuesta.
     *
     * @param id the id of the apuesta to save.
     * @param apuesta the apuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuesta,
     * or with status {@code 400 (Bad Request)} if the apuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apuestas/{id}")
    public ResponseEntity<Apuesta> updateApuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Apuesta apuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Apuesta : {}, {}", id, apuesta);
        if (apuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apuesta result = apuestaRepository.save(apuesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apuestas/:id} : Partial updates given fields of an existing apuesta, field will ignore if it is null
     *
     * @param id the id of the apuesta to save.
     * @param apuesta the apuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuesta,
     * or with status {@code 400 (Bad Request)} if the apuesta is not valid,
     * or with status {@code 404 (Not Found)} if the apuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the apuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apuestas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apuesta> partialUpdateApuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Apuesta apuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apuesta partially : {}, {}", id, apuesta);
        if (apuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apuesta> result = apuestaRepository
            .findById(apuesta.getId())
            .map(existingApuesta -> {
                if (apuesta.getCreditosApostados() != null) {
                    existingApuesta.setCreditosApostados(apuesta.getCreditosApostados());
                }
                if (apuesta.getHaGanado() != null) {
                    existingApuesta.setHaGanado(apuesta.getHaGanado());
                }
                if (apuesta.getEstado() != null) {
                    existingApuesta.setEstado(apuesta.getEstado());
                }

                return existingApuesta;
            })
            .map(apuestaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /apuestas} : get all the apuestas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apuestas in body.
     */
    @GetMapping("/apuestas")
    public List<Apuesta> getAllApuestas() {
        log.debug("REST request to get all Apuestas");
        return apuestaRepository.findAll();
    }

    /**
     * {@code GET  /apuestas/:id} : get the "id" apuesta.
     *
     * @param id the id of the apuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apuestas/{id}")
    public ResponseEntity<Apuesta> getApuesta(@PathVariable Long id) {
        log.debug("REST request to get Apuesta : {}", id);
        Optional<Apuesta> apuesta = apuestaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apuesta);
    }

    /**
     * {@code DELETE  /apuestas/:id} : delete the "id" apuesta.
     *
     * @param id the id of the apuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apuestas/{id}")
    public ResponseEntity<Void> deleteApuesta(@PathVariable Long id) {
        log.debug("REST request to delete Apuesta : {}", id);
        apuestaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /apuestas} : get apuestas por evento.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apuestas in body.
     */
    @GetMapping("/apuestas/evento/{idEvento}")
    public List<Apuesta> getApuestasByEvento(@PathVariable Long idEvento) {
        log.debug("REST request to getApuestasByEvento");
        return apuestaRepository.findApuestaByEventoId(idEvento);
    }

    @PostMapping("/apuestas/data/{idEvento}")
    public ResponseEntity<EventCalculatedData> getApuestaData(@PathVariable Long idEvento, @Valid @RequestBody Apuesta apuesta)
        throws URISyntaxException {
        log.debug("Getting data of current Bet: {}, {}", idEvento, apuesta);

        EventCalculatedData data = apuestaService.generateEventData(eventoRepository.findById(idEvento).get(), apuesta);

        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    "Multiplicador 1: " + data.getMultiplicadorCompetidor1() + "; Multiplicador 2: " + data.getMultiplicadorCompetidor2()
                )
            )
            .body(data);
    }

    @GetMapping("/apuestas/user")
    public List<HistorialApuestaVM> getApuestasUser() {
        log.debug("REST request to get all Bets from logged-in User");

        List<Apuesta> list = new ArrayList<>();
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                Usuario usuario = usuarioRepository.findById(user.getId()).get();
                list.addAll(apuestaRepository.findAllByUsuarioAndEstadoOrderByIdDesc(usuario, "Finalizado"));
            });

        List<HistorialApuestaVM> historial = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            HistorialApuestaVM h = new HistorialApuestaVM();
            h.setCreditos(list.get(i).getCreditosApostados());

            if (list.get(i).getHaGanado() != null) {
                if (list.get(i).getHaGanado()) {
                    h.setResultado("Acertaste");
                } else {
                    h.setResultado("Perdiste");
                }
            }

            h.setDescripcion(
                list.get(i).getEvento().getDivision().getNombre() +
                ": " +
                list.get(i).getEvento().getCompetidor1().getNombre() +
                " vs " +
                list.get(i).getEvento().getCompetidor2().getNombre() +
                ". - Apostado: " +
                list.get(i).getApostado().getNombre()
            );

            h.setFecha(list.get(i).getEvento().getFecha());
            h.setEstado("Finalizado");
            h.setApostado(list.get(i).getApostado().getNombre());

            historial.add(h);
        }

        log.debug("Sending completed bets of user: {}", historial);

        return historial;
    }

    @GetMapping("/apuestas/pendientes")
    public List<Apuesta> getApuestasPendientes() {
        log.debug("REST request to get all pendant Bets from logged-in User");

        List<Apuesta> list = new ArrayList<>();
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                Usuario usuario = usuarioRepository.findById(user.getId()).get();
                list.addAll(apuestaRepository.findAllByUsuarioAndEstadoOrderByIdDesc(usuario, "Pendiente"));
            });

        log.debug("Sending completed bets of user: {}", list);

        return list;
    }

    @GetMapping("/apuestas/evento/user/{idEvento}")
    public Apuesta getApuestasByEventoAndUser(@PathVariable Long idEvento) {
        log.debug("REST request to getApuestasByEvento");
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin.get());
        if (currentUser.isEmpty()) {
            throw new BadRequestAlertException("No se encuentra autorizado para realizar esta acción", ENTITY_NAME, "notfound");
        } else {
            Optional<Apuesta> apuesta = apuestaRepository.findApuestaByEventoIdAndUsuarioId(idEvento, currentUser.get().getId());
            if (apuesta.isEmpty()) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id not available");
            }
            return apuesta.get();
        }
    }
}
