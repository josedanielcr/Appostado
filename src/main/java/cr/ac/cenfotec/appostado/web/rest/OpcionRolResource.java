package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.OpcionRol;
import cr.ac.cenfotec.appostado.repository.OpcionRolRepository;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.OpcionRol}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OpcionRolResource {

    private final Logger log = LoggerFactory.getLogger(OpcionRolResource.class);

    private static final String ENTITY_NAME = "opcionRol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpcionRolRepository opcionRolRepository;

    public OpcionRolResource(OpcionRolRepository opcionRolRepository) {
        this.opcionRolRepository = opcionRolRepository;
    }

    /**
     * {@code POST  /opcion-rols} : Create a new opcionRol.
     *
     * @param opcionRol the opcionRol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opcionRol, or with status {@code 400 (Bad Request)} if the opcionRol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opcion-rols")
    public ResponseEntity<OpcionRol> createOpcionRol(@Valid @RequestBody OpcionRol opcionRol) throws URISyntaxException {
        log.debug("REST request to save OpcionRol : {}", opcionRol);
        if (opcionRol.getId() != null) {
            throw new BadRequestAlertException("A new opcionRol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpcionRol result = opcionRolRepository.save(opcionRol);
        return ResponseEntity
            .created(new URI("/api/opcion-rols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /opcion-rols/:id} : Updates an existing opcionRol.
     *
     * @param id the id of the opcionRol to save.
     * @param opcionRol the opcionRol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opcionRol,
     * or with status {@code 400 (Bad Request)} if the opcionRol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opcionRol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opcion-rols/{id}")
    public ResponseEntity<OpcionRol> updateOpcionRol(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OpcionRol opcionRol
    ) throws URISyntaxException {
        log.debug("REST request to update OpcionRol : {}, {}", id, opcionRol);
        if (opcionRol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opcionRol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opcionRolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OpcionRol result = opcionRolRepository.save(opcionRol);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opcionRol.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /opcion-rols/:id} : Partial updates given fields of an existing opcionRol, field will ignore if it is null
     *
     * @param id the id of the opcionRol to save.
     * @param opcionRol the opcionRol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opcionRol,
     * or with status {@code 400 (Bad Request)} if the opcionRol is not valid,
     * or with status {@code 404 (Not Found)} if the opcionRol is not found,
     * or with status {@code 500 (Internal Server Error)} if the opcionRol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/opcion-rols/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OpcionRol> partialUpdateOpcionRol(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OpcionRol opcionRol
    ) throws URISyntaxException {
        log.debug("REST request to partial update OpcionRol partially : {}, {}", id, opcionRol);
        if (opcionRol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opcionRol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opcionRolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OpcionRol> result = opcionRolRepository
            .findById(opcionRol.getId())
            .map(existingOpcionRol -> {
                if (opcionRol.getOpcion() != null) {
                    existingOpcionRol.setOpcion(opcionRol.getOpcion());
                }
                if (opcionRol.getPath() != null) {
                    existingOpcionRol.setPath(opcionRol.getPath());
                }
                if (opcionRol.getIcono() != null) {
                    existingOpcionRol.setIcono(opcionRol.getIcono());
                }
                if (opcionRol.getNombre() != null) {
                    existingOpcionRol.setNombre(opcionRol.getNombre());
                }

                return existingOpcionRol;
            })
            .map(opcionRolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opcionRol.getId().toString())
        );
    }

    /**
     * {@code GET  /opcion-rols} : get all the opcionRols.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opcionRols in body.
     */
    @GetMapping("/opcion-rols")
    public List<OpcionRol> getAllOpcionRols() {
        log.debug("REST request to get all OpcionRols");
        return opcionRolRepository.findAll();
    }

    /**
     * {@code GET  /opcion-rols/:id} : get the "id" opcionRol.
     *
     * @param id the id of the opcionRol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opcionRol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opcion-rols/{id}")
    public ResponseEntity<OpcionRol> getOpcionRol(@PathVariable Long id) {
        log.debug("REST request to get OpcionRol : {}", id);
        Optional<OpcionRol> opcionRol = opcionRolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(opcionRol);
    }

    /**
     * {@code DELETE  /opcion-rols/:id} : delete the "id" opcionRol.
     *
     * @param id the id of the opcionRol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/opcion-rols/{id}")
    public ResponseEntity<Void> deleteOpcionRol(@PathVariable Long id) {
        log.debug("REST request to delete OpcionRol : {}", id);
        opcionRolRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
