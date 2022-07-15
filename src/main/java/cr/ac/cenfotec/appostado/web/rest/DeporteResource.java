package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Deporte;
import cr.ac.cenfotec.appostado.repository.DeporteRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Deporte}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeporteResource {

    private final Logger log = LoggerFactory.getLogger(DeporteResource.class);

    private static final String ENTITY_NAME = "deporte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeporteRepository deporteRepository;

    public DeporteResource(DeporteRepository deporteRepository) {
        this.deporteRepository = deporteRepository;
    }

    /**
     * {@code POST  /deportes} : Create a new deporte.
     *
     * @param deporte the deporte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deporte, or with status {@code 400 (Bad Request)} if the deporte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deportes")
    public ResponseEntity<Deporte> createDeporte(@Valid @RequestBody Deporte deporte) throws URISyntaxException {
        log.debug("REST request to save Deporte : {}", deporte);
        if (deporte.getId() != null) {
            throw new BadRequestAlertException("A new deporte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deporte result = deporteRepository.save(deporte);
        return ResponseEntity
            .created(new URI("/api/deportes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deportes/:id} : Updates an existing deporte.
     *
     * @param id the id of the deporte to save.
     * @param deporte the deporte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deporte,
     * or with status {@code 400 (Bad Request)} if the deporte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deporte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deportes/{id}")
    public ResponseEntity<Deporte> updateDeporte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Deporte deporte
    ) throws URISyntaxException {
        log.debug("REST request to update Deporte : {}, {}", id, deporte);
        if (deporte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deporte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deporte result = deporteRepository.save(deporte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deporte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deportes/:id} : Partial updates given fields of an existing deporte, field will ignore if it is null
     *
     * @param id the id of the deporte to save.
     * @param deporte the deporte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deporte,
     * or with status {@code 400 (Bad Request)} if the deporte is not valid,
     * or with status {@code 404 (Not Found)} if the deporte is not found,
     * or with status {@code 500 (Internal Server Error)} if the deporte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deportes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deporte> partialUpdateDeporte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deporte deporte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deporte partially : {}, {}", id, deporte);
        if (deporte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deporte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deporteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deporte> result = deporteRepository
            .findById(deporte.getId())
            .map(existingDeporte -> {
                if (deporte.getNombre() != null) {
                    existingDeporte.setNombre(deporte.getNombre());
                }

                return existingDeporte;
            })
            .map(deporteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deporte.getId().toString())
        );
    }

    /**
     * {@code GET  /deportes} : get all the deportes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deportes in body.
     */
    @GetMapping("/deportes")
    public List<Deporte> getAllDeportes() {
        log.debug("REST request to get all Deportes");
        return deporteRepository.findAll();
    }

    /**
     * {@code GET  /deportes/:id} : get the "id" deporte.
     *
     * @param id the id of the deporte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deporte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deportes/{id}")
    public ResponseEntity<Deporte> getDeporte(@PathVariable Long id) {
        log.debug("REST request to get Deporte : {}", id);
        Optional<Deporte> deporte = deporteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deporte);
    }

    /**
     * {@code DELETE  /deportes/:id} : delete the "id" deporte.
     *
     * @param id the id of the deporte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deportes/{id}")
    public ResponseEntity<Void> deleteDeporte(@PathVariable Long id) {
        log.debug("REST request to delete Deporte : {}", id);
        deporteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
