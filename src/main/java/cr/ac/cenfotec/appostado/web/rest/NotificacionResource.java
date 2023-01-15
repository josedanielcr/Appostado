package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.AmigoRepository;
import cr.ac.cenfotec.appostado.repository.NotificacionRepository;
import cr.ac.cenfotec.appostado.repository.UserRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import cr.ac.cenfotec.appostado.web.rest.vm.NotificacionAmigoVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Notificacion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private static final String ENTITY_NAME = "notificacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final UserRepository userRepository;
    private final AmigoRepository amigoRepository;

    public NotificacionResource(
        NotificacionRepository notificacionRepository,
        UsuarioRepository usuarioRepository,
        UserRepository userRepository,
        AmigoRepository amigoRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.userRepository = userRepository;
        this.amigoRepository = amigoRepository;
    }

    /**
     * {@code POST  /notificacions} : Create a new notificacion.
     *
     * @param notificacion the notificacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacion, or with status {@code 400 (Bad Request)} if the notificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notificacions")
    public ResponseEntity<Notificacion> createNotificacion(@Valid @RequestBody Notificacion notificacion) throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacion);
        if (notificacion.getId() != null) {
            throw new BadRequestAlertException("A new notificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notificacion result = notificacionRepository.save(notificacion);
        return ResponseEntity
            .created(new URI("/api/notificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacions/:id} : Updates an existing notificacion.
     *
     * @param id the id of the notificacion to save.
     * @param notificacion the notificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacion,
     * or with status {@code 400 (Bad Request)} if the notificacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notificacions/{id}")
    public ResponseEntity<Notificacion> updateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Notificacion notificacion
    ) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}, {}", id, notificacion);
        if (notificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Notificacion result = notificacionRepository.save(notificacion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notificacions/:id} : Partial updates given fields of an existing notificacion, field will ignore if it is null
     *
     * @param id the id of the notificacion to save.
     * @param notificacion the notificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacion,
     * or with status {@code 400 (Bad Request)} if the notificacion is not valid,
     * or with status {@code 404 (Not Found)} if the notificacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notificacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Notificacion> partialUpdateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Notificacion notificacion
    ) {
        log.debug("REST request to partial update Notificacion partially : {}, {}", id, notificacion);
        if (notificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Notificacion> result = notificacionRepository
            .findById(notificacion.getId())
            .map(existingNotificacion -> {
                if (notificacion.getDescripcion() != null) {
                    existingNotificacion.setDescripcion(notificacion.getDescripcion());
                }
                if (notificacion.getTipo() != null) {
                    existingNotificacion.setTipo(notificacion.getTipo());
                }
                if (notificacion.getFecha() != null) {
                    existingNotificacion.setFecha(notificacion.getFecha());
                }
                if (notificacion.getHaGanado() != null) {
                    existingNotificacion.setHaGanado(notificacion.getHaGanado());
                }
                if (notificacion.getGanancia() != null) {
                    existingNotificacion.setGanancia(notificacion.getGanancia());
                }
                if (notificacion.getFueLeida() != null) {
                    existingNotificacion.setFueLeida(notificacion.getFueLeida());
                }

                return existingNotificacion;
            })
            .map(notificacionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacion.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacions} : get all the notificacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacions in body.
     */
    @GetMapping("/notificacions")
    public List<Notificacion> getAllNotificacions() {
        log.debug("REST request to get all Notificacions");
        return notificacionRepository.findAll();
    }

    /**
     * {@code GET  /notificacions/:id} : get the "id" notificacion.
     *
     * @param id the id of the notificacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/{id}")
    public ResponseEntity<Notificacion> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Optional<Notificacion> notificacion = notificacionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notificacion);
    }

    /**
     * {@code DELETE  /notificacions/:id} : delete the "id" notificacion.
     *
     * @param id the id of the notificacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notificacions/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/notificacions/amistades")
    public List<NotificacionAmigoVM> getAllNotificationsAmistades() {
        log.debug("REST request to get all friendship Notificacions");

        List<NotificacionAmigoVM> listNotificacionesAmigo = new ArrayList<>();

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                List<Notificacion> listNotificaciones = notificacionRepository.findAllByDescripcionAndTipoAndHaGanado(
                    user.getLogin(),
                    "Amistad",
                    false
                );
                for (Notificacion n : listNotificaciones) {
                    User solicitante = userRepository.getById(n.getUsuario().getId());
                    NotificacionAmigoVM nuevo = new NotificacionAmigoVM(
                        solicitante.getLogin(),
                        solicitante.getImageUrl(),
                        n.getFecha(),
                        n.getUsuario().getPais()
                    );
                    listNotificacionesAmigo.add(nuevo);
                }
            });

        log.debug("Sending friendship notifications for User: {}", listNotificacionesAmigo);

        return listNotificacionesAmigo;
    }

    @PostMapping("/notificacions/amistad/{amigo}")
    public ResponseEntity<Notificacion> addAmigoNotificacion(@PathVariable String amigo) throws URISyntaxException {
        log.debug("REST request to create friendship notificacion: {}", amigo);

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (!login.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado a realizar esta acción", ENTITY_NAME, "notlogged");
        }

        Optional<User> amigable = userRepository.findOneByLogin(login.get());
        Optional<User> newAmigo = userRepository.findOneByLogin(amigo);

        if (!newAmigo.isPresent()) {
            throw new BadRequestAlertException("No existe ningún usuario registrado con ese nombre", ENTITY_NAME, "notfound");
        }

        Optional<Usuario> usuarioAmigable = usuarioRepository.findById(amigable.get().getId());
        Optional<Usuario> usuarioAmigo = usuarioRepository.findById(newAmigo.get().getId());

        if (
            notificacionRepository.existsByUsuarioAndDescripcionAndHaGanado(usuarioAmigable.get(), amigo, false) ||
            notificacionRepository.existsByUsuarioAndDescripcionAndHaGanado(usuarioAmigo.get(), login.get(), false)
        ) {
            throw new BadRequestAlertException(
                "Ya existe una solicitud de amistad pendiente con ese usuario",
                ENTITY_NAME,
                "notificationexists"
            );
        }

        if (amigoRepository.existsByUsuarioAndAmigo(usuarioAmigable.get(), usuarioAmigo.get())) {
            throw new BadRequestAlertException("Ya eres amigo de ese usuario", ENTITY_NAME, "amigoexists");
        }

        Notificacion nueva = new Notificacion();
        nueva.setUsuario(usuarioAmigable.get());
        nueva.setHaGanado(false);
        nueva.setFueLeida(false);
        nueva.setDescripcion(amigo);
        nueva.setTipo("Amistad");
        nueva.setFecha(LocalDate.now());

        nueva = notificacionRepository.save(nueva);

        return ResponseEntity
            .created(new URI("/api/notificacions/amistad" + amigo))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nueva.getId().toString()))
            .body(nueva);
    }

    /**
     * {@code GET  /notificacions/:id} : get notifications by user
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/user")
    public List<Notificacion> getNotificationByUser() {
        log.debug("REST request to get getNotificationByUser : {}");
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin.get());
        if (!currentUser.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado para realizar esta acción", ENTITY_NAME, "notfound");
        } else {
            return this.notificacionRepository.findAllByUsuarioId(currentUser.get().getId());
        }
    }

    @GetMapping("/notificacions/user/active")
    public List<Notificacion> getNotificationByUserActive() {
        log.debug("REST request to get getNotificationByUser : {}");
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> currentUser = userRepository.findOneByLogin(userLogin.get());
        if (!currentUser.isPresent()) {
            throw new BadRequestAlertException("No se encuentra autorizado para realizar esta acción", ENTITY_NAME, "notfound");
        } else {
            Usuario usuario = usuarioRepository.findById(currentUser.get().getId()).get();
            return this.notificacionRepository.findAllByUsuarioAndFueLeida(usuario, false);
        }
    }

    @PutMapping("/notificacions/read/{id}")
    public ResponseEntity<Void> updateReadNotification(@PathVariable long id, @RequestBody Notificacion newNotification) {
        try {
            newNotification.setFueLeida(true);
            updateNotificacion(id, newNotification);
            return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id)))
                .build();
        } catch (Exception e) {
            throw new BadRequestAlertException("No se encuentra autorizado para realizar esta acción", ENTITY_NAME, "notfound");
        }
    }
}
