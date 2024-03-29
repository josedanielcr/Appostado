package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Compra;
import cr.ac.cenfotec.appostado.repository.CompraRepository;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Compra}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CompraResource {

    private final Logger log = LoggerFactory.getLogger(CompraResource.class);

    private static final String ENTITY_NAME = "compra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompraRepository compraRepository;

    public CompraResource(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    /**
     * {@code POST  /compras} : Create a new compra.
     *
     * @param compra the compra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compra, or with status {@code 400 (Bad Request)} if the compra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compras")
    public ResponseEntity<Compra> createCompra(@RequestBody Compra compra) throws URISyntaxException {
        log.debug("REST request to save Compra : {}", compra);
        if (compra.getId() != null) {
            throw new BadRequestAlertException("A new compra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Compra result = compraRepository.save(compra);
        return ResponseEntity
            .created(new URI("/api/compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compras/:id} : Updates an existing compra.
     *
     * @param id the id of the compra to save.
     * @param compra the compra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compra,
     * or with status {@code 400 (Bad Request)} if the compra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compras/{id}")
    public ResponseEntity<Compra> updateCompra(@PathVariable(value = "id", required = false) final Long id, @RequestBody Compra compra)
        throws URISyntaxException {
        log.debug("REST request to update Compra : {}, {}", id, compra);
        if (compra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Compra result = compraRepository.save(compra);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compra.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compras/:id} : Partial updates given fields of an existing compra, field will ignore if it is null
     *
     * @param id the id of the compra to save.
     * @param compra the compra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compra,
     * or with status {@code 400 (Bad Request)} if the compra is not valid,
     * or with status {@code 404 (Not Found)} if the compra is not found,
     * or with status {@code 500 (Internal Server Error)} if the compra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Compra> partialUpdateCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Compra compra
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compra partially : {}, {}", id, compra);
        if (compra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Compra> result = compraRepository
            .findById(compra.getId())
            .map(existingCompra -> {
                return existingCompra;
            })
            .map(compraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compra.getId().toString())
        );
    }

    /**
     * {@code GET  /compras} : get all the compras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compras in body.
     */
    @GetMapping("/compras")
    public List<Compra> getAllCompras() {
        log.debug("REST request to get all Compras");
        return compraRepository.findAll();
    }

    /**
     * {@code GET  /compras/:id} : get the "id" compra.
     *
     * @param id the id of the compra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compras/{id}")
    public ResponseEntity<Compra> getCompra(@PathVariable Long id) {
        log.debug("REST request to get Compra : {}", id);
        Optional<Compra> compra = compraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(compra);
    }

    /**
     * {@code DELETE  /compras/:id} : delete the "id" compra.
     *
     * @param id the id of the compra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compras/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        log.debug("REST request to delete Compra : {}", id);
        compraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
