package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.ProductoUsuario;
import cr.ac.cenfotec.appostado.repository.ProductoUsuarioRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.ProductoUsuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductoUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(ProductoUsuarioResource.class);

    private static final String ENTITY_NAME = "productoUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoUsuarioRepository productoUsuarioRepository;

    public ProductoUsuarioResource(ProductoUsuarioRepository productoUsuarioRepository) {
        this.productoUsuarioRepository = productoUsuarioRepository;
    }

    /**
     * {@code POST  /producto-usuarios} : Create a new productoUsuario.
     *
     * @param productoUsuario the productoUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoUsuario, or with status {@code 400 (Bad Request)} if the productoUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/producto-usuarios")
    public ResponseEntity<ProductoUsuario> createProductoUsuario(@Valid @RequestBody ProductoUsuario productoUsuario)
        throws URISyntaxException {
        log.debug("REST request to save ProductoUsuario : {}", productoUsuario);
        if (productoUsuario.getId() != null) {
            throw new BadRequestAlertException("A new productoUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoUsuario result = productoUsuarioRepository.save(productoUsuario);
        return ResponseEntity
            .created(new URI("/api/producto-usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /producto-usuarios/:id} : Updates an existing productoUsuario.
     *
     * @param id the id of the productoUsuario to save.
     * @param productoUsuario the productoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoUsuario,
     * or with status {@code 400 (Bad Request)} if the productoUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/producto-usuarios/{id}")
    public ResponseEntity<ProductoUsuario> updateProductoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductoUsuario productoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update ProductoUsuario : {}, {}", id, productoUsuario);
        if (productoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductoUsuario result = productoUsuarioRepository.save(productoUsuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoUsuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /producto-usuarios/:id} : Partial updates given fields of an existing productoUsuario, field will ignore if it is null
     *
     * @param id the id of the productoUsuario to save.
     * @param productoUsuario the productoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoUsuario,
     * or with status {@code 400 (Bad Request)} if the productoUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the productoUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the productoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/producto-usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductoUsuario> partialUpdateProductoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductoUsuario productoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductoUsuario partially : {}, {}", id, productoUsuario);
        if (productoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductoUsuario> result = productoUsuarioRepository
            .findById(productoUsuario.getId())
            .map(existingProductoUsuario -> {
                if (productoUsuario.getReclamado() != null) {
                    existingProductoUsuario.setReclamado(productoUsuario.getReclamado());
                }
                if (productoUsuario.getCodigo() != null) {
                    existingProductoUsuario.setCodigo(productoUsuario.getCodigo());
                }

                return existingProductoUsuario;
            })
            .map(productoUsuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /producto-usuarios} : get all the productoUsuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productoUsuarios in body.
     */
    @GetMapping("/producto-usuarios")
    public List<ProductoUsuario> getAllProductoUsuarios() {
        log.debug("REST request to get all ProductoUsuarios");
        return productoUsuarioRepository.findAll();
    }

    /**
     * {@code GET  /producto-usuarios/:id} : get the "id" productoUsuario.
     *
     * @param id the id of the productoUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/producto-usuarios/{id}")
    public ResponseEntity<ProductoUsuario> getProductoUsuario(@PathVariable Long id) {
        log.debug("REST request to get ProductoUsuario : {}", id);
        Optional<ProductoUsuario> productoUsuario = productoUsuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productoUsuario);
    }

    /**
     * {@code DELETE  /producto-usuarios/:id} : delete the "id" productoUsuario.
     *
     * @param id the id of the productoUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/producto-usuarios/{id}")
    public ResponseEntity<Void> deleteProductoUsuario(@PathVariable Long id) {
        log.debug("REST request to delete ProductoUsuario : {}", id);
        productoUsuarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
