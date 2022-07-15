package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Mision;
import cr.ac.cenfotec.appostado.repository.MisionRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Mision}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MisionResource {

    private final Logger log = LoggerFactory.getLogger(MisionResource.class);

    private static final String ENTITY_NAME = "mision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MisionRepository misionRepository;

    public MisionResource(MisionRepository misionRepository) {
        this.misionRepository = misionRepository;
    }

    /**
     * {@code POST  /misions} : Create a new mision.
     *
     * @param mision the mision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mision, or with status {@code 400 (Bad Request)} if the mision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/misions")
    public ResponseEntity<Mision> createMision(@Valid @RequestBody Mision mision) throws URISyntaxException {
        log.debug("REST request to save Mision : {}", mision);
        if (mision.getId() != null) {
            throw new BadRequestAlertException("A new mision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mision result = misionRepository.save(mision);
        return ResponseEntity
            .created(new URI("/api/misions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /misions/:id} : Updates an existing mision.
     *
     * @param id the id of the mision to save.
     * @param mision the mision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mision,
     * or with status {@code 400 (Bad Request)} if the mision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/misions/{id}")
    public ResponseEntity<Mision> updateMision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mision mision
    ) throws URISyntaxException {
        log.debug("REST request to update Mision : {}, {}", id, mision);
        if (mision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mision result = misionRepository.save(mision);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mision.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /misions/:id} : Partial updates given fields of an existing mision, field will ignore if it is null
     *
     * @param id the id of the mision to save.
     * @param mision the mision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mision,
     * or with status {@code 400 (Bad Request)} if the mision is not valid,
     * or with status {@code 404 (Not Found)} if the mision is not found,
     * or with status {@code 500 (Internal Server Error)} if the mision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/misions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mision> partialUpdateMision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mision mision
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mision partially : {}, {}", id, mision);
        if (mision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mision> result = misionRepository
            .findById(mision.getId())
            .map(existingMision -> {
                if (mision.getNombre() != null) {
                    existingMision.setNombre(mision.getNombre());
                }
                if (mision.getDescripcion() != null) {
                    existingMision.setDescripcion(mision.getDescripcion());
                }
                if (mision.getBonoCreditos() != null) {
                    existingMision.setBonoCreditos(mision.getBonoCreditos());
                }
                if (mision.getDia() != null) {
                    existingMision.setDia(mision.getDia());
                }

                return existingMision;
            })
            .map(misionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mision.getId().toString())
        );
    }

    /**
     * {@code GET  /misions} : get all the misions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of misions in body.
     */
    @GetMapping("/misions")
    public List<Mision> getAllMisions() {
        log.debug("REST request to get all Misions");
        return misionRepository.findAll();
    }

    /**
     * {@code GET  /misions/:id} : get the "id" mision.
     *
     * @param id the id of the mision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/misions/{id}")
    public ResponseEntity<Mision> getMision(@PathVariable Long id) {
        log.debug("REST request to get Mision : {}", id);
        Optional<Mision> mision = misionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mision);
    }

    /**
     * {@code DELETE  /misions/:id} : delete the "id" mision.
     *
     * @param id the id of the mision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/misions/{id}")
    public ResponseEntity<Void> deleteMision(@PathVariable Long id) {
        log.debug("REST request to delete Mision : {}", id);
        misionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
