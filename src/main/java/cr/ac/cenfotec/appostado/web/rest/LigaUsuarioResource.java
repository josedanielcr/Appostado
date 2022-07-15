package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.LigaUsuario;
import cr.ac.cenfotec.appostado.repository.LigaUsuarioRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.LigaUsuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LigaUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(LigaUsuarioResource.class);

    private static final String ENTITY_NAME = "ligaUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigaUsuarioRepository ligaUsuarioRepository;

    public LigaUsuarioResource(LigaUsuarioRepository ligaUsuarioRepository) {
        this.ligaUsuarioRepository = ligaUsuarioRepository;
    }

    /**
     * {@code POST  /liga-usuarios} : Create a new ligaUsuario.
     *
     * @param ligaUsuario the ligaUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligaUsuario, or with status {@code 400 (Bad Request)} if the ligaUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/liga-usuarios")
    public ResponseEntity<LigaUsuario> createLigaUsuario(@Valid @RequestBody LigaUsuario ligaUsuario) throws URISyntaxException {
        log.debug("REST request to save LigaUsuario : {}", ligaUsuario);
        if (ligaUsuario.getId() != null) {
            throw new BadRequestAlertException("A new ligaUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigaUsuario result = ligaUsuarioRepository.save(ligaUsuario);
        return ResponseEntity
            .created(new URI("/api/liga-usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /liga-usuarios/:id} : Updates an existing ligaUsuario.
     *
     * @param id the id of the ligaUsuario to save.
     * @param ligaUsuario the ligaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligaUsuario,
     * or with status {@code 400 (Bad Request)} if the ligaUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/liga-usuarios/{id}")
    public ResponseEntity<LigaUsuario> updateLigaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LigaUsuario ligaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update LigaUsuario : {}, {}", id, ligaUsuario);
        if (ligaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LigaUsuario result = ligaUsuarioRepository.save(ligaUsuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligaUsuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /liga-usuarios/:id} : Partial updates given fields of an existing ligaUsuario, field will ignore if it is null
     *
     * @param id the id of the ligaUsuario to save.
     * @param ligaUsuario the ligaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligaUsuario,
     * or with status {@code 400 (Bad Request)} if the ligaUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the ligaUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/liga-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LigaUsuario> partialUpdateLigaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LigaUsuario ligaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigaUsuario partially : {}, {}", id, ligaUsuario);
        if (ligaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligaUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigaUsuario> result = ligaUsuarioRepository
            .findById(ligaUsuario.getId())
            .map(existingLigaUsuario -> {
                if (ligaUsuario.getIdUsuario() != null) {
                    existingLigaUsuario.setIdUsuario(ligaUsuario.getIdUsuario());
                }
                if (ligaUsuario.getIdLiga() != null) {
                    existingLigaUsuario.setIdLiga(ligaUsuario.getIdLiga());
                }

                return existingLigaUsuario;
            })
            .map(ligaUsuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligaUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /liga-usuarios} : get all the ligaUsuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligaUsuarios in body.
     */
    @GetMapping("/liga-usuarios")
    public List<LigaUsuario> getAllLigaUsuarios() {
        log.debug("REST request to get all LigaUsuarios");
        return ligaUsuarioRepository.findAll();
    }

    /**
     * {@code GET  /liga-usuarios/:id} : get the "id" ligaUsuario.
     *
     * @param id the id of the ligaUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligaUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/liga-usuarios/{id}")
    public ResponseEntity<LigaUsuario> getLigaUsuario(@PathVariable Long id) {
        log.debug("REST request to get LigaUsuario : {}", id);
        Optional<LigaUsuario> ligaUsuario = ligaUsuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ligaUsuario);
    }

    /**
     * {@code DELETE  /liga-usuarios/:id} : delete the "id" ligaUsuario.
     *
     * @param id the id of the ligaUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/liga-usuarios/{id}")
    public ResponseEntity<Void> deleteLigaUsuario(@PathVariable Long id) {
        log.debug("REST request to delete LigaUsuario : {}", id);
        ligaUsuarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
