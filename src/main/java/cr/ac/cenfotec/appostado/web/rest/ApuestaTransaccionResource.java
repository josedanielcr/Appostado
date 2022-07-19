package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.ApuestaTransaccion;
import cr.ac.cenfotec.appostado.repository.ApuestaTransaccionRepository;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.ApuestaTransaccion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApuestaTransaccionResource {

    private final Logger log = LoggerFactory.getLogger(ApuestaTransaccionResource.class);

    private static final String ENTITY_NAME = "apuestaTransaccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApuestaTransaccionRepository apuestaTransaccionRepository;

    public ApuestaTransaccionResource(ApuestaTransaccionRepository apuestaTransaccionRepository) {
        this.apuestaTransaccionRepository = apuestaTransaccionRepository;
    }

    /**
     * {@code POST  /apuesta-transaccions} : Create a new apuestaTransaccion.
     *
     * @param apuestaTransaccion the apuestaTransaccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apuestaTransaccion, or with status {@code 400 (Bad Request)} if the apuestaTransaccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apuesta-transaccions")
    public ResponseEntity<ApuestaTransaccion> createApuestaTransaccion(@RequestBody ApuestaTransaccion apuestaTransaccion)
        throws URISyntaxException {
        log.debug("REST request to save ApuestaTransaccion : {}", apuestaTransaccion);
        if (apuestaTransaccion.getId() != null) {
            throw new BadRequestAlertException("A new apuestaTransaccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApuestaTransaccion result = apuestaTransaccionRepository.save(apuestaTransaccion);
        return ResponseEntity
            .created(new URI("/api/apuesta-transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apuesta-transaccions/:id} : Updates an existing apuestaTransaccion.
     *
     * @param id the id of the apuestaTransaccion to save.
     * @param apuestaTransaccion the apuestaTransaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuestaTransaccion,
     * or with status {@code 400 (Bad Request)} if the apuestaTransaccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apuestaTransaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apuesta-transaccions/{id}")
    public ResponseEntity<ApuestaTransaccion> updateApuestaTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApuestaTransaccion apuestaTransaccion
    ) throws URISyntaxException {
        log.debug("REST request to update ApuestaTransaccion : {}, {}", id, apuestaTransaccion);
        if (apuestaTransaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuestaTransaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaTransaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApuestaTransaccion result = apuestaTransaccionRepository.save(apuestaTransaccion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuestaTransaccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apuesta-transaccions/:id} : Partial updates given fields of an existing apuestaTransaccion, field will ignore if it is null
     *
     * @param id the id of the apuestaTransaccion to save.
     * @param apuestaTransaccion the apuestaTransaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuestaTransaccion,
     * or with status {@code 400 (Bad Request)} if the apuestaTransaccion is not valid,
     * or with status {@code 404 (Not Found)} if the apuestaTransaccion is not found,
     * or with status {@code 500 (Internal Server Error)} if the apuestaTransaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apuesta-transaccions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApuestaTransaccion> partialUpdateApuestaTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApuestaTransaccion apuestaTransaccion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApuestaTransaccion partially : {}, {}", id, apuestaTransaccion);
        if (apuestaTransaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuestaTransaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaTransaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApuestaTransaccion> result = apuestaTransaccionRepository
            .findById(apuestaTransaccion.getId())
            .map(existingApuestaTransaccion -> {
                return existingApuestaTransaccion;
            })
            .map(apuestaTransaccionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuestaTransaccion.getId().toString())
        );
    }

    /**
     * {@code GET  /apuesta-transaccions} : get all the apuestaTransaccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apuestaTransaccions in body.
     */
    @GetMapping("/apuesta-transaccions")
    public List<ApuestaTransaccion> getAllApuestaTransaccions() {
        log.debug("REST request to get all ApuestaTransaccions");
        return apuestaTransaccionRepository.findAll();
    }

    /**
     * {@code GET  /apuesta-transaccions/:id} : get the "id" apuestaTransaccion.
     *
     * @param id the id of the apuestaTransaccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apuestaTransaccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apuesta-transaccions/{id}")
    public ResponseEntity<ApuestaTransaccion> getApuestaTransaccion(@PathVariable Long id) {
        log.debug("REST request to get ApuestaTransaccion : {}", id);
        Optional<ApuestaTransaccion> apuestaTransaccion = apuestaTransaccionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apuestaTransaccion);
    }

    /**
     * {@code DELETE  /apuesta-transaccions/:id} : delete the "id" apuestaTransaccion.
     *
     * @param id the id of the apuestaTransaccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apuesta-transaccions/{id}")
    public ResponseEntity<Void> deleteApuestaTransaccion(@PathVariable Long id) {
        log.debug("REST request to delete ApuestaTransaccion : {}", id);
        apuestaTransaccionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
