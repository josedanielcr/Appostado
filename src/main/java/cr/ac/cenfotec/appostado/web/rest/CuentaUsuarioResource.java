package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.CuentaUsuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CuentaUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(CuentaUsuarioResource.class);

    private static final String ENTITY_NAME = "cuentaUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    public CuentaUsuarioResource(CuentaUsuarioRepository cuentaUsuarioRepository) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
    }

    /**
     * {@code POST  /cuenta-usuarios} : Create a new cuentaUsuario.
     *
     * @param cuentaUsuario the cuentaUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuentaUsuario, or with status {@code 400 (Bad Request)} if the cuentaUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cuenta-usuarios")
    public ResponseEntity<CuentaUsuario> createCuentaUsuario(@Valid @RequestBody CuentaUsuario cuentaUsuario) throws URISyntaxException {
        log.debug("REST request to save CuentaUsuario : {}", cuentaUsuario);
        if (cuentaUsuario.getId() != null) {
            throw new BadRequestAlertException("A new cuentaUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuentaUsuario result = cuentaUsuarioRepository.save(cuentaUsuario);
        return ResponseEntity
            .created(new URI("/api/cuenta-usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cuenta-usuarios/:id} : Updates an existing cuentaUsuario.
     *
     * @param id the id of the cuentaUsuario to save.
     * @param cuentaUsuario the cuentaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuentaUsuario,
     * or with status {@code 400 (Bad Request)} if the cuentaUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuentaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cuenta-usuarios/{id}")
    public ResponseEntity<CuentaUsuario> updateCuentaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CuentaUsuario cuentaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update CuentaUsuario : {}, {}", id, cuentaUsuario);
        if (cuentaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuentaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuentaUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CuentaUsuario result = cuentaUsuarioRepository.save(cuentaUsuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuentaUsuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cuenta-usuarios/:id} : Partial updates given fields of an existing cuentaUsuario, field will ignore if it is null
     *
     * @param id the id of the cuentaUsuario to save.
     * @param cuentaUsuario the cuentaUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuentaUsuario,
     * or with status {@code 400 (Bad Request)} if the cuentaUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the cuentaUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuentaUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cuenta-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuentaUsuario> partialUpdateCuentaUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CuentaUsuario cuentaUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update CuentaUsuario partially : {}, {}", id, cuentaUsuario);
        if (cuentaUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuentaUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuentaUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuentaUsuario> result = cuentaUsuarioRepository
            .findById(cuentaUsuario.getId())
            .map(existingCuentaUsuario -> {
                if (cuentaUsuario.getBalance() != null) {
                    existingCuentaUsuario.setBalance(cuentaUsuario.getBalance());
                }
                if (cuentaUsuario.getNumCanjes() != null) {
                    existingCuentaUsuario.setNumCanjes(cuentaUsuario.getNumCanjes());
                }
                if (cuentaUsuario.getApuestasTotales() != null) {
                    existingCuentaUsuario.setApuestasTotales(cuentaUsuario.getApuestasTotales());
                }
                if (cuentaUsuario.getApuestasGanadas() != null) {
                    existingCuentaUsuario.setApuestasGanadas(cuentaUsuario.getApuestasGanadas());
                }

                return existingCuentaUsuario;
            })
            .map(cuentaUsuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuentaUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /cuenta-usuarios} : get all the cuentaUsuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuentaUsuarios in body.
     */
    @GetMapping("/cuenta-usuarios")
    public List<CuentaUsuario> getAllCuentaUsuarios() {
        log.debug("REST request to get all CuentaUsuarios");
        return cuentaUsuarioRepository.findAll();
    }

    /**
     * {@code GET  /cuenta-usuarios/:id} : get the "id" cuentaUsuario.
     *
     * @param id the id of the cuentaUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuentaUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuenta-usuarios/{id}")
    public ResponseEntity<CuentaUsuario> getCuentaUsuario(@PathVariable Long id) {
        log.debug("REST request to get CuentaUsuario : {}", id);
        Optional<CuentaUsuario> cuentaUsuario = cuentaUsuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cuentaUsuario);
    }

    /**
     * {@code DELETE  /cuenta-usuarios/:id} : delete the "id" cuentaUsuario.
     *
     * @param id the id of the cuentaUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuenta-usuarios/{id}")
    public ResponseEntity<Void> deleteCuentaUsuario(@PathVariable Long id) {
        log.debug("REST request to delete CuentaUsuario : {}", id);
        cuentaUsuarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}