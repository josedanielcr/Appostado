package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.MisionTransaccion;
import cr.ac.cenfotec.appostado.repository.MisionTransaccionRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.MisionTransaccion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MisionTransaccionResource {

    private final Logger log = LoggerFactory.getLogger(MisionTransaccionResource.class);

    private static final String ENTITY_NAME = "misionTransaccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MisionTransaccionRepository misionTransaccionRepository;

    public MisionTransaccionResource(MisionTransaccionRepository misionTransaccionRepository) {
        this.misionTransaccionRepository = misionTransaccionRepository;
    }

    /**
     * {@code POST  /mision-transaccions} : Create a new misionTransaccion.
     *
     * @param misionTransaccion the misionTransaccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new misionTransaccion, or with status {@code 400 (Bad Request)} if the misionTransaccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mision-transaccions")
    public ResponseEntity<MisionTransaccion> createMisionTransaccion(@Valid @RequestBody MisionTransaccion misionTransaccion)
        throws URISyntaxException {
        log.debug("REST request to save MisionTransaccion : {}", misionTransaccion);
        if (misionTransaccion.getId() != null) {
            throw new BadRequestAlertException("A new misionTransaccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MisionTransaccion result = misionTransaccionRepository.save(misionTransaccion);
        return ResponseEntity
            .created(new URI("/api/mision-transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mision-transaccions/:id} : Updates an existing misionTransaccion.
     *
     * @param id the id of the misionTransaccion to save.
     * @param misionTransaccion the misionTransaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misionTransaccion,
     * or with status {@code 400 (Bad Request)} if the misionTransaccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the misionTransaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mision-transaccions/{id}")
    public ResponseEntity<MisionTransaccion> updateMisionTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MisionTransaccion misionTransaccion
    ) throws URISyntaxException {
        log.debug("REST request to update MisionTransaccion : {}, {}", id, misionTransaccion);
        if (misionTransaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misionTransaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionTransaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MisionTransaccion result = misionTransaccionRepository.save(misionTransaccion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misionTransaccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mision-transaccions/:id} : Partial updates given fields of an existing misionTransaccion, field will ignore if it is null
     *
     * @param id the id of the misionTransaccion to save.
     * @param misionTransaccion the misionTransaccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misionTransaccion,
     * or with status {@code 400 (Bad Request)} if the misionTransaccion is not valid,
     * or with status {@code 404 (Not Found)} if the misionTransaccion is not found,
     * or with status {@code 500 (Internal Server Error)} if the misionTransaccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mision-transaccions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MisionTransaccion> partialUpdateMisionTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MisionTransaccion misionTransaccion
    ) throws URISyntaxException {
        log.debug("REST request to partial update MisionTransaccion partially : {}, {}", id, misionTransaccion);
        if (misionTransaccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misionTransaccion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionTransaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MisionTransaccion> result = misionTransaccionRepository
            .findById(misionTransaccion.getId())
            .map(existingMisionTransaccion -> {
                if (misionTransaccion.getIdMision() != null) {
                    existingMisionTransaccion.setIdMision(misionTransaccion.getIdMision());
                }
                if (misionTransaccion.getIdTransaccion() != null) {
                    existingMisionTransaccion.setIdTransaccion(misionTransaccion.getIdTransaccion());
                }

                return existingMisionTransaccion;
            })
            .map(misionTransaccionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misionTransaccion.getId().toString())
        );
    }

    /**
     * {@code GET  /mision-transaccions} : get all the misionTransaccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of misionTransaccions in body.
     */
    @GetMapping("/mision-transaccions")
    public List<MisionTransaccion> getAllMisionTransaccions() {
        log.debug("REST request to get all MisionTransaccions");
        return misionTransaccionRepository.findAll();
    }

    /**
     * {@code GET  /mision-transaccions/:id} : get the "id" misionTransaccion.
     *
     * @param id the id of the misionTransaccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the misionTransaccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mision-transaccions/{id}")
    public ResponseEntity<MisionTransaccion> getMisionTransaccion(@PathVariable Long id) {
        log.debug("REST request to get MisionTransaccion : {}", id);
        Optional<MisionTransaccion> misionTransaccion = misionTransaccionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(misionTransaccion);
    }

    /**
     * {@code DELETE  /mision-transaccions/:id} : delete the "id" misionTransaccion.
     *
     * @param id the id of the misionTransaccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mision-transaccions/{id}")
    public ResponseEntity<Void> deleteMisionTransaccion(@PathVariable Long id) {
        log.debug("REST request to delete MisionTransaccion : {}", id);
        misionTransaccionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
