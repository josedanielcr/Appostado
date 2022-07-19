package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Canje;
import cr.ac.cenfotec.appostado.repository.CanjeRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Canje}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CanjeResource {

    private final Logger log = LoggerFactory.getLogger(CanjeResource.class);

    private static final String ENTITY_NAME = "canje";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CanjeRepository canjeRepository;

    public CanjeResource(CanjeRepository canjeRepository) {
        this.canjeRepository = canjeRepository;
    }

    /**
     * {@code POST  /canjes} : Create a new canje.
     *
     * @param canje the canje to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canje, or with status {@code 400 (Bad Request)} if the canje has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/canjes")
    public ResponseEntity<Canje> createCanje(@Valid @RequestBody Canje canje) throws URISyntaxException {
        log.debug("REST request to save Canje : {}", canje);
        if (canje.getId() != null) {
            throw new BadRequestAlertException("A new canje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Canje result = canjeRepository.save(canje);
        return ResponseEntity
            .created(new URI("/api/canjes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /canjes/:id} : Updates an existing canje.
     *
     * @param id the id of the canje to save.
     * @param canje the canje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canje,
     * or with status {@code 400 (Bad Request)} if the canje is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/canjes/{id}")
    public ResponseEntity<Canje> updateCanje(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Canje canje)
        throws URISyntaxException {
        log.debug("REST request to update Canje : {}, {}", id, canje);
        if (canje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canje.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canjeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Canje result = canjeRepository.save(canje);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, canje.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /canjes/:id} : Partial updates given fields of an existing canje, field will ignore if it is null
     *
     * @param id the id of the canje to save.
     * @param canje the canje to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canje,
     * or with status {@code 400 (Bad Request)} if the canje is not valid,
     * or with status {@code 404 (Not Found)} if the canje is not found,
     * or with status {@code 500 (Internal Server Error)} if the canje couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/canjes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Canje> partialUpdateCanje(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Canje canje
    ) throws URISyntaxException {
        log.debug("REST request to partial update Canje partially : {}, {}", id, canje);
        if (canje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canje.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canjeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Canje> result = canjeRepository
            .findById(canje.getId())
            .map(existingCanje -> {
                if (canje.getEstado() != null) {
                    existingCanje.setEstado(canje.getEstado());
                }
                if (canje.getDetalle() != null) {
                    existingCanje.setDetalle(canje.getDetalle());
                }

                return existingCanje;
            })
            .map(canjeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, canje.getId().toString())
        );
    }

    /**
     * {@code GET  /canjes} : get all the canjes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of canjes in body.
     */
    @GetMapping("/canjes")
    public List<Canje> getAllCanjes() {
        log.debug("REST request to get all Canjes");
        return canjeRepository.findAll();
    }

    /**
     * {@code GET  /canjes/:id} : get the "id" canje.
     *
     * @param id the id of the canje to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canje, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/canjes/{id}")
    public ResponseEntity<Canje> getCanje(@PathVariable Long id) {
        log.debug("REST request to get Canje : {}", id);
        Optional<Canje> canje = canjeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(canje);
    }

    /**
     * {@code DELETE  /canjes/:id} : delete the "id" canje.
     *
     * @param id the id of the canje to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/canjes/{id}")
    public ResponseEntity<Void> deleteCanje(@PathVariable Long id) {
        log.debug("REST request to delete Canje : {}", id);
        canjeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
