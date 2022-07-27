package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Premio;
import cr.ac.cenfotec.appostado.repository.PremioRepository;
import cr.ac.cenfotec.appostado.service.CloudDynaryService;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Premio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PremioResource {

    @Autowired
    CloudDynaryService cloudinaryService;

    private final Logger log = LoggerFactory.getLogger(PremioResource.class);

    private static final String ENTITY_NAME = "premio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PremioRepository premioRepository;

    public PremioResource(PremioRepository premioRepository) {
        this.premioRepository = premioRepository;
    }

    /**
     * {@code POST  /premios} : Create a new premio.
     *
     * @param premio the premio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new premio, or with status {@code 400 (Bad Request)} if the premio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/premios")
    public ResponseEntity<Premio> createPremio(@Valid @RequestBody Premio premio) throws URISyntaxException, IOException {
        log.debug("REST request to save Premio : {}", premio);
        if (premio.getId() != null) {
            throw new BadRequestAlertException("A new premio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String pathOficial = premio.getFoto();
        Map resultMap = cloudinaryService.upload(pathOficial);
        premio.setFoto(String.valueOf(resultMap.get("url")));
        Premio result = premioRepository.save(premio);
        return ResponseEntity
            .created(new URI("/api/premios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /premios/:id} : Updates an existing premio.
     *
     * @param id the id of the premio to save.
     * @param premio the premio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated premio,
     * or with status {@code 400 (Bad Request)} if the premio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the premio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/premios/{id}")
    public ResponseEntity<Premio> updatePremio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Premio premio
    ) throws URISyntaxException {
        log.debug("REST request to update Premio : {}, {}", id, premio);
        if (premio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, premio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!premioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Premio result = premioRepository.save(premio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, premio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /premios/:id} : Partial updates given fields of an existing premio, field will ignore if it is null
     *
     * @param id the id of the premio to save.
     * @param premio the premio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated premio,
     * or with status {@code 400 (Bad Request)} if the premio is not valid,
     * or with status {@code 404 (Not Found)} if the premio is not found,
     * or with status {@code 500 (Internal Server Error)} if the premio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/premios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Premio> partialUpdatePremio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Premio premio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Premio partially : {}, {}", id, premio);
        if (premio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, premio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!premioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Premio> result = premioRepository
            .findById(premio.getId())
            .map(existingPremio -> {
                if (premio.getNombre() != null) {
                    existingPremio.setNombre(premio.getNombre());
                }
                if (premio.getDescripcion() != null) {
                    existingPremio.setDescripcion(premio.getDescripcion());
                }
                if (premio.getFoto() != null) {
                    existingPremio.setFoto(premio.getFoto());
                }
                if (premio.getCosto() != null) {
                    existingPremio.setCosto(premio.getCosto());
                }
                if (premio.getEstado() != null) {
                    existingPremio.setEstado(premio.getEstado());
                }
                if (premio.getStock() != null) {
                    existingPremio.setStock(premio.getStock());
                }
                if (premio.getNumCanjes() != null) {
                    existingPremio.setNumCanjes(premio.getNumCanjes());
                }

                return existingPremio;
            })
            .map(premioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, premio.getId().toString())
        );
    }

    /**
     * {@code GET  /premios} : get all the premios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of premios in body.
     */
    @GetMapping("/premios")
    public List<Premio> getAllPremios() {
        log.debug("REST request to get all Premios");
        return premioRepository.findAll();
    }

    /**
     * {@code GET  /premios/:id} : get the "id" premio.
     *
     * @param id the id of the premio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the premio, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("/premios/{id}")
    public ResponseEntity<Premio> getPremio(@PathVariable Long id) {
        log.debug("REST request to get Premio : {}", id);
        Optional<Premio> premio = premioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(premio);
    }

    @GetMapping("/premios/activos")
    public List<Premio> getPremioActivo() {
        log.debug("REST request to get Premio activo ");
        List<Premio> premio = premioRepository.findByEstado("Activo");
        return premio;
    }

    @GetMapping("/premios/activos/{acomodo}")
    public List<Premio> getPremioActivoCosto(@PathVariable int acomodo) {
        log.debug("REST request to get Premios activso segun los costos ");
        List<Premio> premio = null;
        if (acomodo == 1) {
            premio = premioRepository.findByCostoA();
            return premio;
        }
        if (acomodo == 2) {
            premio = premioRepository.findByCostoD();
            return premio;
        }
        if (acomodo == 3) {
            premio = premioRepository.findByPopularidadA();
            return premio;
        }
        if (acomodo == 4) {
            premio = premioRepository.findByPopularidadD();
            return premio;
        }

        return premio;
    }

    /**
     * {@code DELETE  /premios/:id} : delete the "id" premio.
     *
     * @param id the id of the premio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/premios/{id}")
    public ResponseEntity<Void> deletePremio(@PathVariable Long id) {
        log.debug("REST request to delete Premio : {}", id);
        premioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
