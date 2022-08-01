package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Apuesta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApuestaResource {

    private final Logger log = LoggerFactory.getLogger(ApuestaResource.class);

    private static final String ENTITY_NAME = "apuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApuestaRepository apuestaRepository;

    public ApuestaResource(ApuestaRepository apuestaRepository) {
        this.apuestaRepository = apuestaRepository;
    }

    /**
     * {@code POST  /apuestas} : Create a new apuesta.
     *
     * @param apuesta the apuesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apuesta, or with status {@code 400 (Bad Request)} if the apuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apuestas")
    public ResponseEntity<Apuesta> createApuesta(@Valid @RequestBody Apuesta apuesta) throws URISyntaxException {
        log.debug("REST request to save Apuesta : {}", apuesta);
        if (apuesta.getId() != null) {
            throw new BadRequestAlertException("A new apuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apuesta result = apuestaRepository.save(apuesta);
        return ResponseEntity
            .created(new URI("/api/apuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apuestas/:id} : Updates an existing apuesta.
     *
     * @param id the id of the apuesta to save.
     * @param apuesta the apuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuesta,
     * or with status {@code 400 (Bad Request)} if the apuesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apuestas/{id}")
    public ResponseEntity<Apuesta> updateApuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Apuesta apuesta
    ) throws URISyntaxException {
        log.debug("REST request to update Apuesta : {}, {}", id, apuesta);
        if (apuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apuesta result = apuestaRepository.save(apuesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apuestas/:id} : Partial updates given fields of an existing apuesta, field will ignore if it is null
     *
     * @param id the id of the apuesta to save.
     * @param apuesta the apuesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apuesta,
     * or with status {@code 400 (Bad Request)} if the apuesta is not valid,
     * or with status {@code 404 (Not Found)} if the apuesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the apuesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apuestas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apuesta> partialUpdateApuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Apuesta apuesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apuesta partially : {}, {}", id, apuesta);
        if (apuesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apuesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apuesta> result = apuestaRepository
            .findById(apuesta.getId())
            .map(existingApuesta -> {
                if (apuesta.getCreditosApostados() != null) {
                    existingApuesta.setCreditosApostados(apuesta.getCreditosApostados());
                }
                if (apuesta.getHaGanado() != null) {
                    existingApuesta.setHaGanado(apuesta.getHaGanado());
                }
                if (apuesta.getEstado() != null) {
                    existingApuesta.setEstado(apuesta.getEstado());
                }

                return existingApuesta;
            })
            .map(apuestaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apuesta.getId().toString())
        );
    }

    /**
     * {@code GET  /apuestas} : get all the apuestas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apuestas in body.
     */
    @GetMapping("/apuestas")
    public List<Apuesta> getAllApuestas() {
        log.debug("REST request to get all Apuestas");
        return apuestaRepository.findAll();
    }

    /**
     * {@code GET  /apuestas/:id} : get the "id" apuesta.
     *
     * @param id the id of the apuesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apuesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apuestas/{id}")
    public ResponseEntity<Apuesta> getApuesta(@PathVariable Long id) {
        log.debug("REST request to get Apuesta : {}", id);
        Optional<Apuesta> apuesta = apuestaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apuesta);
    }

    /**
     * {@code DELETE  /apuestas/:id} : delete the "id" apuesta.
     *
     * @param id the id of the apuesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apuestas/{id}")
    public ResponseEntity<Void> deleteApuesta(@PathVariable Long id) {
        log.debug("REST request to delete Apuesta : {}", id);
        apuestaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /apuestas} : get apuestas por evento.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apuestas in body.
     */
    @GetMapping("/apuestas/evento/{idEvento}")
    public List<Apuesta> getApuestasByEvento(@PathVariable Long idEvento) {
        log.debug("REST request to getApuestasByEvento");
        return apuestaRepository.findApuestaByEventoId(idEvento);
    }
}
