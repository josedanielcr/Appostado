package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Transaccion;
import cr.ac.cenfotec.appostado.domain.Usuario;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import cr.ac.cenfotec.appostado.repository.UserRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import cr.ac.cenfotec.appostado.web.rest.vm.AmigoDetailsVM;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Transaccion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransaccionResource {

    private final Logger log = LoggerFactory.getLogger(TransaccionResource.class);

    private static final String ENTITY_NAME = "transaccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransaccionRepository transaccionRepository;

    private final UserRepository userRepository;

    private final UsuarioRepository usuarioRepository;

    private final CuentaUsuarioRepository cuentaRepository;

    public TransaccionResource(
        TransaccionRepository transaccionRepository,
        UserRepository userRepository,
        UsuarioRepository usuarioRepository,
        CuentaUsuarioRepository cuentaRepository
    ) {
        this.transaccionRepository = transaccionRepository;
        this.userRepository = userRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * {@code POST  /transaccions} : Create a new transaccion.
     *
     * @param transaccion the transaccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaccion, or with status {@code 400 (Bad Request)} if the transaccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaccions")
    public ResponseEntity<Transaccion> createTransaccion(@Valid @RequestBody Transaccion transaccion) throws URISyntaxException {
        log.debug("REST request to save Transaccion : {}", transaccion);
        if (transaccion.getId() != null) {
            throw new BadRequestAlertException("A new transaccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transaccion result = transaccionRepository.save(transaccion);
        return ResponseEntity
            .created(new URI("/api/transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaccions/:id} : Updates an existing transaccion.
     *
     * @param id the id of the transaccion to save.
     * @param transaccion the transaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaccion,
     * or with status {@code 400 (Bad Request)} if the transaccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaccions/{id}")
    public ResponseEntity<Transaccion> updateTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Transaccion transaccion
    ) throws URISyntaxException {
        log.debug("REST request to update Transaccion : {}, {}", id, transaccion);
        if (transaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transaccion result = transaccionRepository.save(transaccion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaccions/:id} : Partial updates given fields of an existing transaccion, field will ignore if it is null
     *
     * @param id the id of the transaccion to save.
     * @param transaccion the transaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaccion,
     * or with status {@code 400 (Bad Request)} if the transaccion is not valid,
     * or with status {@code 404 (Not Found)} if the transaccion is not found,
     * or with status {@code 500 (Internal Server Error)} if the transaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaccions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Transaccion> partialUpdateTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Transaccion transaccion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaccion partially : {}, {}", id, transaccion);
        if (transaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Transaccion> result = transaccionRepository
            .findById(transaccion.getId())
            .map(existingTransaccion -> {
                if (transaccion.getFecha() != null) {
                    existingTransaccion.setFecha(transaccion.getFecha());
                }
                if (transaccion.getTipo() != null) {
                    existingTransaccion.setTipo(transaccion.getTipo());
                }
                if (transaccion.getDescripcion() != null) {
                    existingTransaccion.setDescripcion(transaccion.getDescripcion());
                }
                if (transaccion.getMonto() != null) {
                    existingTransaccion.setMonto(transaccion.getMonto());
                }

                return existingTransaccion;
            })
            .map(transaccionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaccion.getId().toString())
        );
    }

    /**
     * {@code GET  /transaccions} : get all the transaccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transaccions in body.
     */
    @GetMapping("/transaccions")
    public List<Transaccion> getAllTransaccions() {
        log.debug("REST request to get all Transaccions");
        return transaccionRepository.findAll();
    }

    /**
     * {@code GET  /transaccions/:id} : get the "id" transaccion.
     *
     * @param id the id of the transaccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaccions/{id}")
    public ResponseEntity<Transaccion> getTransaccion(@PathVariable Long id) {
        log.debug("REST request to get Transaccion : {}", id);
        Optional<Transaccion> transaccion = transaccionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transaccion);
    }

    /**
     * {@code DELETE  /transaccions/:id} : delete the "id" transaccion.
     *
     * @param id the id of the transaccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaccions/{id}")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long id) {
        log.debug("REST request to delete Transaccion : {}", id);
        transaccionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/transaccions/user")
    public List<Transaccion> getTransaccionsUser() {
        log.debug("REST request to get all Transaccions from logged in User");

        List<Transaccion> list = new ArrayList<>();
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                Usuario usuario = usuarioRepository.findById(user.getId()).get();
                CuentaUsuario cuenta = cuentaRepository.findCuentaUsuarioByUsuario_Id(usuario.getId()).get();
                list.addAll(transaccionRepository.findAllByCuentaOrderByIdDesc(cuenta));
            });

        log.debug("Sending logged-in User transaccions: {}", list);

        return list;
    }
}
