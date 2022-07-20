package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Competidor;
import cr.ac.cenfotec.appostado.repository.CompetidorRepository;
import cr.ac.cenfotec.appostado.service.CloudDynaryService;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Competidor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CompetidorResource {

    @Autowired
    CloudDynaryService cloudinaryService;

    private final Logger log = LoggerFactory.getLogger(CompetidorResource.class);

    private static final String ENTITY_NAME = "competidor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetidorRepository competidorRepository;

    public CompetidorResource(CompetidorRepository competidorRepository) {
        this.competidorRepository = competidorRepository;
    }

    /**
     * {@code POST  /competidors} : Create a new competidor.
     *
     * @param competidor the competidor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competidor, or with status {@code 400 (Bad Request)} if the competidor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competidors")
    public ResponseEntity<Competidor> createCompetidor(@Valid @RequestBody Competidor competidor) throws URISyntaxException, IOException {
        log.debug("REST request to save Competidor : {}", competidor);
        if (competidor.getId() != null) {
            throw new BadRequestAlertException("A new competidor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String pathOficial[] = competidor.getFoto().split("blob:");
        Map resultMap = cloudinaryService.upload(pathOficial[1]);
        competidor.setFoto(String.valueOf(resultMap.get("url")));
        Competidor result = competidorRepository.save(competidor);
        return ResponseEntity
            .created(new URI("/api/competidors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competidors/:id} : Updates an existing competidor.
     *
     * @param id the id of the competidor to save.
     * @param competidor the competidor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competidor,
     * or with status {@code 400 (Bad Request)} if the competidor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competidor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competidors/{id}")
    public ResponseEntity<Competidor> updateCompetidor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Competidor competidor
    ) throws URISyntaxException {
        log.debug("REST request to update Competidor : {}, {}", id, competidor);
        if (competidor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competidor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competidorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Competidor result = competidorRepository.save(competidor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competidor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /competidors/:id} : Partial updates given fields of an existing competidor, field will ignore if it is null
     *
     * @param id the id of the competidor to save.
     * @param competidor the competidor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competidor,
     * or with status {@code 400 (Bad Request)} if the competidor is not valid,
     * or with status {@code 404 (Not Found)} if the competidor is not found,
     * or with status {@code 500 (Internal Server Error)} if the competidor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/competidors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Competidor> partialUpdateCompetidor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Competidor competidor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Competidor partially : {}, {}", id, competidor);
        if (competidor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competidor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competidorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Competidor> result = competidorRepository
            .findById(competidor.getId())
            .map(existingCompetidor -> {
                if (competidor.getNombre() != null) {
                    existingCompetidor.setNombre(competidor.getNombre());
                }
                if (competidor.getFoto() != null) {
                    existingCompetidor.setFoto(competidor.getFoto());
                }

                return existingCompetidor;
            })
            .map(competidorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competidor.getId().toString())
        );
    }

    /**
     * {@code GET  /competidors} : get all the competidors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competidors in body.
     */
    @GetMapping("/competidors")
    public List<Competidor> getAllCompetidors() {
        log.debug("REST request to get all Competidors");
        return competidorRepository.findAll();
    }

    /**
     * {@code GET  /competidors/:id} : get the "id" competidor.
     *
     * @param id the id of the competidor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competidor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competidors/{id}")
    public ResponseEntity<Competidor> getCompetidor(@PathVariable Long id) {
        log.debug("REST request to get Competidor : {}", id);
        Optional<Competidor> competidor = competidorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(competidor);
    }

    /**
     * {@code DELETE  /competidors/:id} : delete the "id" competidor.
     *
     * @param id the id of the competidor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competidors/{id}")
    public ResponseEntity<Void> deleteCompetidor(@PathVariable Long id) {
        log.debug("REST request to delete Competidor : {}", id);
        competidorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
