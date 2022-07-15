package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Liga;
import cr.ac.cenfotec.appostado.repository.LigaRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Liga}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LigaResource {

    private final Logger log = LoggerFactory.getLogger(LigaResource.class);

    private static final String ENTITY_NAME = "liga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigaRepository ligaRepository;

    public LigaResource(LigaRepository ligaRepository) {
        this.ligaRepository = ligaRepository;
    }

    /**
     * {@code POST  /ligas} : Create a new liga.
     *
     * @param liga the liga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liga, or with status {@code 400 (Bad Request)} if the liga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligas")
    public ResponseEntity<Liga> createLiga(@Valid @RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to save Liga : {}", liga);
        if (liga.getId() != null) {
            throw new BadRequestAlertException("A new liga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Liga result = ligaRepository.save(liga);
        return ResponseEntity
            .created(new URI("/api/ligas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligas/:id} : Updates an existing liga.
     *
     * @param id the id of the liga to save.
     * @param liga the liga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liga,
     * or with status {@code 400 (Bad Request)} if the liga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligas/{id}")
    public ResponseEntity<Liga> updateLiga(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Liga liga)
        throws URISyntaxException {
        log.debug("REST request to update Liga : {}, {}", id, liga);
        if (liga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Liga result = ligaRepository.save(liga);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liga.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligas/:id} : Partial updates given fields of an existing liga, field will ignore if it is null
     *
     * @param id the id of the liga to save.
     * @param liga the liga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liga,
     * or with status {@code 400 (Bad Request)} if the liga is not valid,
     * or with status {@code 404 (Not Found)} if the liga is not found,
     * or with status {@code 500 (Internal Server Error)} if the liga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Liga> partialUpdateLiga(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Liga liga
    ) throws URISyntaxException {
        log.debug("REST request to partial update Liga partially : {}, {}", id, liga);
        if (liga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Liga> result = ligaRepository
            .findById(liga.getId())
            .map(existingLiga -> {
                if (liga.getNombre() != null) {
                    existingLiga.setNombre(liga.getNombre());
                }
                if (liga.getDescripcion() != null) {
                    existingLiga.setDescripcion(liga.getDescripcion());
                }
                if (liga.getFoto() != null) {
                    existingLiga.setFoto(liga.getFoto());
                }

                return existingLiga;
            })
            .map(ligaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liga.getId().toString())
        );
    }

    /**
     * {@code GET  /ligas} : get all the ligas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligas in body.
     */
    @GetMapping("/ligas")
    public List<Liga> getAllLigas() {
        log.debug("REST request to get all Ligas");
        return ligaRepository.findAll();
    }

    /**
     * {@code GET  /ligas/:id} : get the "id" liga.
     *
     * @param id the id of the liga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligas/{id}")
    public ResponseEntity<Liga> getLiga(@PathVariable Long id) {
        log.debug("REST request to get Liga : {}", id);
        Optional<Liga> liga = ligaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(liga);
    }

    /**
     * {@code DELETE  /ligas/:id} : delete the "id" liga.
     *
     * @param id the id of the liga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligas/{id}")
    public ResponseEntity<Void> deleteLiga(@PathVariable Long id) {
        log.debug("REST request to delete Liga : {}", id);
        ligaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
