package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.MisionUsuario;
import cr.ac.cenfotec.appostado.repository.MisionUsuarioRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.MisionUsuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MisionUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(MisionUsuarioResource.class);

    private static final String ENTITY_NAME = "misionUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MisionUsuarioRepository misionUsuarioRepository;

    public MisionUsuarioResource(MisionUsuarioRepository misionUsuarioRepository) {
        this.misionUsuarioRepository = misionUsuarioRepository;
    }

    /**
     * {@code POST  /mision-usuarios} : Create a new misionUsuario.
     *
     * @param misionUsuario the misionUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new misionUsuario, or with status {@code 400 (Bad Request)} if the misionUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mision-usuarios")
    public ResponseEntity<MisionUsuario> createMisionUsuario(@Valid @RequestBody MisionUsuario misionUsuario) throws URISyntaxException {
        log.debug("REST request to save MisionUsuario : {}", misionUsuario);
        if (misionUsuario.getId() != null) {
            throw new BadRequestAlertException("A new misionUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MisionUsuario result = misionUsuarioRepository.save(misionUsuario);
        return ResponseEntity
            .created(new URI("/api/mision-usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mision-usuarios/:id} : Updates an existing misionUsuario.
     *
     * @param id the id of the misionUsuario to save.
     * @param misionUsuario the misionUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misionUsuario,
     * or with status {@code 400 (Bad Request)} if the misionUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the misionUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mision-usuarios/{id}")
    public ResponseEntity<MisionUsuario> updateMisionUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MisionUsuario misionUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update MisionUsuario : {}, {}", id, misionUsuario);
        if (misionUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misionUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MisionUsuario result = misionUsuarioRepository.save(misionUsuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misionUsuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mision-usuarios/:id} : Partial updates given fields of an existing misionUsuario, field will ignore if it is null
     *
     * @param id the id of the misionUsuario to save.
     * @param misionUsuario the misionUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated misionUsuario,
     * or with status {@code 400 (Bad Request)} if the misionUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the misionUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the misionUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mision-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MisionUsuario> partialUpdateMisionUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MisionUsuario misionUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update MisionUsuario partially : {}, {}", id, misionUsuario);
        if (misionUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, misionUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!misionUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MisionUsuario> result = misionUsuarioRepository
            .findById(misionUsuario.getId())
            .map(existingMisionUsuario -> {
                if (misionUsuario.getIdMision() != null) {
                    existingMisionUsuario.setIdMision(misionUsuario.getIdMision());
                }
                if (misionUsuario.getIdUsuario() != null) {
                    existingMisionUsuario.setIdUsuario(misionUsuario.getIdUsuario());
                }
                if (misionUsuario.getCompletado() != null) {
                    existingMisionUsuario.setCompletado(misionUsuario.getCompletado());
                }

                return existingMisionUsuario;
            })
            .map(misionUsuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, misionUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /mision-usuarios} : get all the misionUsuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of misionUsuarios in body.
     */
    @GetMapping("/mision-usuarios")
    public List<MisionUsuario> getAllMisionUsuarios() {
        log.debug("REST request to get all MisionUsuarios");
        return misionUsuarioRepository.findAll();
    }

    /**
     * {@code GET  /mision-usuarios/:id} : get the "id" misionUsuario.
     *
     * @param id the id of the misionUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the misionUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mision-usuarios/{id}")
    public ResponseEntity<MisionUsuario> getMisionUsuario(@PathVariable Long id) {
        log.debug("REST request to get MisionUsuario : {}", id);
        Optional<MisionUsuario> misionUsuario = misionUsuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(misionUsuario);
    }

    /**
     * {@code DELETE  /mision-usuarios/:id} : delete the "id" misionUsuario.
     *
     * @param id the id of the misionUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mision-usuarios/{id}")
    public ResponseEntity<Void> deleteMisionUsuario(@PathVariable Long id) {
        log.debug("REST request to delete MisionUsuario : {}", id);
        misionUsuarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
