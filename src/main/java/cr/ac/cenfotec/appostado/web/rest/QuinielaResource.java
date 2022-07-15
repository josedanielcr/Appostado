package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Quiniela;
import cr.ac.cenfotec.appostado.repository.QuinielaRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Quiniela}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuinielaResource {

    private final Logger log = LoggerFactory.getLogger(QuinielaResource.class);

    private static final String ENTITY_NAME = "quiniela";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuinielaRepository quinielaRepository;

    public QuinielaResource(QuinielaRepository quinielaRepository) {
        this.quinielaRepository = quinielaRepository;
    }

    /**
     * {@code POST  /quinielas} : Create a new quiniela.
     *
     * @param quiniela the quiniela to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quiniela, or with status {@code 400 (Bad Request)} if the quiniela has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quinielas")
    public ResponseEntity<Quiniela> createQuiniela(@Valid @RequestBody Quiniela quiniela) throws URISyntaxException {
        log.debug("REST request to save Quiniela : {}", quiniela);
        if (quiniela.getId() != null) {
            throw new BadRequestAlertException("A new quiniela cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Quiniela result = quinielaRepository.save(quiniela);
        return ResponseEntity
            .created(new URI("/api/quinielas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quinielas/:id} : Updates an existing quiniela.
     *
     * @param id the id of the quiniela to save.
     * @param quiniela the quiniela to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quiniela,
     * or with status {@code 400 (Bad Request)} if the quiniela is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quiniela couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quinielas/{id}")
    public ResponseEntity<Quiniela> updateQuiniela(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Quiniela quiniela
    ) throws URISyntaxException {
        log.debug("REST request to update Quiniela : {}, {}", id, quiniela);
        if (quiniela.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quiniela.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quinielaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Quiniela result = quinielaRepository.save(quiniela);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quiniela.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quinielas/:id} : Partial updates given fields of an existing quiniela, field will ignore if it is null
     *
     * @param id the id of the quiniela to save.
     * @param quiniela the quiniela to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quiniela,
     * or with status {@code 400 (Bad Request)} if the quiniela is not valid,
     * or with status {@code 404 (Not Found)} if the quiniela is not found,
     * or with status {@code 500 (Internal Server Error)} if the quiniela couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quinielas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Quiniela> partialUpdateQuiniela(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Quiniela quiniela
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quiniela partially : {}, {}", id, quiniela);
        if (quiniela.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quiniela.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quinielaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Quiniela> result = quinielaRepository
            .findById(quiniela.getId())
            .map(existingQuiniela -> {
                if (quiniela.getNombre() != null) {
                    existingQuiniela.setNombre(quiniela.getNombre());
                }
                if (quiniela.getDescripcion() != null) {
                    existingQuiniela.setDescripcion(quiniela.getDescripcion());
                }
                if (quiniela.getCostoParticipacion() != null) {
                    existingQuiniela.setCostoParticipacion(quiniela.getCostoParticipacion());
                }
                if (quiniela.getPrimerPremio() != null) {
                    existingQuiniela.setPrimerPremio(quiniela.getPrimerPremio());
                }
                if (quiniela.getSegundoPremio() != null) {
                    existingQuiniela.setSegundoPremio(quiniela.getSegundoPremio());
                }
                if (quiniela.getTercerPremio() != null) {
                    existingQuiniela.setTercerPremio(quiniela.getTercerPremio());
                }
                if (quiniela.getEstado() != null) {
                    existingQuiniela.setEstado(quiniela.getEstado());
                }

                return existingQuiniela;
            })
            .map(quinielaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quiniela.getId().toString())
        );
    }

    /**
     * {@code GET  /quinielas} : get all the quinielas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quinielas in body.
     */
    @GetMapping("/quinielas")
    public List<Quiniela> getAllQuinielas() {
        log.debug("REST request to get all Quinielas");
        return quinielaRepository.findAll();
    }

    /**
     * {@code GET  /quinielas/:id} : get the "id" quiniela.
     *
     * @param id the id of the quiniela to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quiniela, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quinielas/{id}")
    public ResponseEntity<Quiniela> getQuiniela(@PathVariable Long id) {
        log.debug("REST request to get Quiniela : {}", id);
        Optional<Quiniela> quiniela = quinielaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quiniela);
    }

    /**
     * {@code DELETE  /quinielas/:id} : delete the "id" quiniela.
     *
     * @param id the id of the quiniela to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quinielas/{id}")
    public ResponseEntity<Void> deleteQuiniela(@PathVariable Long id) {
        log.debug("REST request to delete Quiniela : {}", id);
        quinielaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
