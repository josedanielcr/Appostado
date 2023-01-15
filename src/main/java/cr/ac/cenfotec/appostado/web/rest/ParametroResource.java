package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Parametro;
import cr.ac.cenfotec.appostado.repository.ParametroRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Parametro}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ParametroResource {

    private final Logger log = LoggerFactory.getLogger(ParametroResource.class);

    private static final String ENTITY_NAME = "parametro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametroRepository parametroRepository;

    public ParametroResource(ParametroRepository parametroRepository) {
        this.parametroRepository = parametroRepository;
    }

    /**
     * {@code POST  /parametros} : Create a new parametro.
     *
     * @param parametro the parametro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametro, or with status {@code 400 (Bad Request)} if the parametro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parametros")
    public ResponseEntity<Parametro> createParametro(@Valid @RequestBody Parametro parametro) throws URISyntaxException {
        log.debug("REST request to save Parametro : {}", parametro);
        if (parametro.getId() != null) {
            throw new BadRequestAlertException("A new parametro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parametro result = parametroRepository.save(parametro);
        return ResponseEntity
            .created(new URI("/api/parametros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parametros/:id} : Updates an existing parametro.
     *
     * @param id the id of the parametro to save.
     * @param parametro the parametro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametro,
     * or with status {@code 400 (Bad Request)} if the parametro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parametros/{id}")
    public ResponseEntity<Parametro> updateParametro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Parametro parametro
    ) throws URISyntaxException {
        log.debug("REST request to update Parametro : {}, {}", id, parametro);
        if (parametro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parametro result = parametroRepository.save(parametro);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametro.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parametros/:id} : Partial updates given fields of an existing parametro, field will ignore if it is null
     *
     * @param id the id of the parametro to save.
     * @param parametro the parametro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametro,
     * or with status {@code 400 (Bad Request)} if the parametro is not valid,
     * or with status {@code 404 (Not Found)} if the parametro is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parametros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parametro> partialUpdateParametro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Parametro parametro
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parametro partially : {}, {}", id, parametro);
        if (parametro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parametro> result = parametroRepository
            .findById(parametro.getId())
            .map(existingParametro -> {
                if (parametro.getTelefono() != null) {
                    existingParametro.setTelefono(parametro.getTelefono());
                }
                if (parametro.getCorreo() != null) {
                    existingParametro.setCorreo(parametro.getCorreo());
                }
                if (parametro.getDireccion() != null) {
                    existingParametro.setDireccion(parametro.getDireccion());
                }

                return existingParametro;
            })
            .map(parametroRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametro.getId().toString())
        );
    }

    /**
     * {@code GET  /parametros} : get all the parametros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametros in body.
     */
    @GetMapping("/parametros")
    public List<Parametro> getAllParametros() {
        log.debug("REST request to get all Parametros");
        return parametroRepository.findAll();
    }

    /**
     * {@code GET  /parametros/:id} : get the "id" parametro.
     *
     * @param id the id of the parametro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parametros/{id}")
    public ResponseEntity<Parametro> getParametro(@PathVariable Long id) {
        log.debug("REST request to get Parametro : {}", id);
        Optional<Parametro> parametro = parametroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(parametro);
    }

    /**
     * {@code DELETE  /parametros/:id} : delete the "id" parametro.
     *
     * @param id the id of the parametro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parametros/{id}")
    public ResponseEntity<Void> deleteParametro(@PathVariable Long id) {
        log.debug("REST request to delete Parametro : {}", id);
        parametroRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
