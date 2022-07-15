package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.DivisionCompetidor;
import cr.ac.cenfotec.appostado.repository.DivisionCompetidorRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.DivisionCompetidor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DivisionCompetidorResource {

    private final Logger log = LoggerFactory.getLogger(DivisionCompetidorResource.class);

    private static final String ENTITY_NAME = "divisionCompetidor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DivisionCompetidorRepository divisionCompetidorRepository;

    public DivisionCompetidorResource(DivisionCompetidorRepository divisionCompetidorRepository) {
        this.divisionCompetidorRepository = divisionCompetidorRepository;
    }

    /**
     * {@code POST  /division-competidors} : Create a new divisionCompetidor.
     *
     * @param divisionCompetidor the divisionCompetidor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new divisionCompetidor, or with status {@code 400 (Bad Request)} if the divisionCompetidor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/division-competidors")
    public ResponseEntity<DivisionCompetidor> createDivisionCompetidor(@Valid @RequestBody DivisionCompetidor divisionCompetidor)
        throws URISyntaxException {
        log.debug("REST request to save DivisionCompetidor : {}", divisionCompetidor);
        if (divisionCompetidor.getId() != null) {
            throw new BadRequestAlertException("A new divisionCompetidor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DivisionCompetidor result = divisionCompetidorRepository.save(divisionCompetidor);
        return ResponseEntity
            .created(new URI("/api/division-competidors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /division-competidors/:id} : Updates an existing divisionCompetidor.
     *
     * @param id the id of the divisionCompetidor to save.
     * @param divisionCompetidor the divisionCompetidor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated divisionCompetidor,
     * or with status {@code 400 (Bad Request)} if the divisionCompetidor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the divisionCompetidor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/division-competidors/{id}")
    public ResponseEntity<DivisionCompetidor> updateDivisionCompetidor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DivisionCompetidor divisionCompetidor
    ) throws URISyntaxException {
        log.debug("REST request to update DivisionCompetidor : {}, {}", id, divisionCompetidor);
        if (divisionCompetidor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, divisionCompetidor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!divisionCompetidorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DivisionCompetidor result = divisionCompetidorRepository.save(divisionCompetidor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, divisionCompetidor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /division-competidors/:id} : Partial updates given fields of an existing divisionCompetidor, field will ignore if it is null
     *
     * @param id the id of the divisionCompetidor to save.
     * @param divisionCompetidor the divisionCompetidor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated divisionCompetidor,
     * or with status {@code 400 (Bad Request)} if the divisionCompetidor is not valid,
     * or with status {@code 404 (Not Found)} if the divisionCompetidor is not found,
     * or with status {@code 500 (Internal Server Error)} if the divisionCompetidor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/division-competidors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DivisionCompetidor> partialUpdateDivisionCompetidor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DivisionCompetidor divisionCompetidor
    ) throws URISyntaxException {
        log.debug("REST request to partial update DivisionCompetidor partially : {}, {}", id, divisionCompetidor);
        if (divisionCompetidor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, divisionCompetidor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!divisionCompetidorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DivisionCompetidor> result = divisionCompetidorRepository
            .findById(divisionCompetidor.getId())
            .map(existingDivisionCompetidor -> {
                if (divisionCompetidor.getIdDivision() != null) {
                    existingDivisionCompetidor.setIdDivision(divisionCompetidor.getIdDivision());
                }
                if (divisionCompetidor.getIdCompetidor() != null) {
                    existingDivisionCompetidor.setIdCompetidor(divisionCompetidor.getIdCompetidor());
                }

                return existingDivisionCompetidor;
            })
            .map(divisionCompetidorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, divisionCompetidor.getId().toString())
        );
    }

    /**
     * {@code GET  /division-competidors} : get all the divisionCompetidors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of divisionCompetidors in body.
     */
    @GetMapping("/division-competidors")
    public List<DivisionCompetidor> getAllDivisionCompetidors() {
        log.debug("REST request to get all DivisionCompetidors");
        return divisionCompetidorRepository.findAll();
    }

    /**
     * {@code GET  /division-competidors/:id} : get the "id" divisionCompetidor.
     *
     * @param id the id of the divisionCompetidor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the divisionCompetidor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/division-competidors/{id}")
    public ResponseEntity<DivisionCompetidor> getDivisionCompetidor(@PathVariable Long id) {
        log.debug("REST request to get DivisionCompetidor : {}", id);
        Optional<DivisionCompetidor> divisionCompetidor = divisionCompetidorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(divisionCompetidor);
    }

    /**
     * {@code DELETE  /division-competidors/:id} : delete the "id" divisionCompetidor.
     *
     * @param id the id of the divisionCompetidor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/division-competidors/{id}")
    public ResponseEntity<Void> deleteDivisionCompetidor(@PathVariable Long id) {
        log.debug("REST request to delete DivisionCompetidor : {}", id);
        divisionCompetidorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
