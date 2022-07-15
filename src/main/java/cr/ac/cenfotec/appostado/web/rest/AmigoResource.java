package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Amigo;
import cr.ac.cenfotec.appostado.repository.AmigoRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Amigo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AmigoResource {

    private final Logger log = LoggerFactory.getLogger(AmigoResource.class);

    private static final String ENTITY_NAME = "amigo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmigoRepository amigoRepository;

    public AmigoResource(AmigoRepository amigoRepository) {
        this.amigoRepository = amigoRepository;
    }

    /**
     * {@code POST  /amigos} : Create a new amigo.
     *
     * @param amigo the amigo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amigo, or with status {@code 400 (Bad Request)} if the amigo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amigos")
    public ResponseEntity<Amigo> createAmigo(@Valid @RequestBody Amigo amigo) throws URISyntaxException {
        log.debug("REST request to save Amigo : {}", amigo);
        if (amigo.getId() != null) {
            throw new BadRequestAlertException("A new amigo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Amigo result = amigoRepository.save(amigo);
        return ResponseEntity
            .created(new URI("/api/amigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amigos/:id} : Updates an existing amigo.
     *
     * @param id the id of the amigo to save.
     * @param amigo the amigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amigo,
     * or with status {@code 400 (Bad Request)} if the amigo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amigos/{id}")
    public ResponseEntity<Amigo> updateAmigo(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Amigo amigo)
        throws URISyntaxException {
        log.debug("REST request to update Amigo : {}, {}", id, amigo);
        if (amigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Amigo result = amigoRepository.save(amigo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amigo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amigos/:id} : Partial updates given fields of an existing amigo, field will ignore if it is null
     *
     * @param id the id of the amigo to save.
     * @param amigo the amigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amigo,
     * or with status {@code 400 (Bad Request)} if the amigo is not valid,
     * or with status {@code 404 (Not Found)} if the amigo is not found,
     * or with status {@code 500 (Internal Server Error)} if the amigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amigos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Amigo> partialUpdateAmigo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Amigo amigo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Amigo partially : {}, {}", id, amigo);
        if (amigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Amigo> result = amigoRepository
            .findById(amigo.getId())
            .map(existingAmigo -> {
                if (amigo.getIdUsuario() != null) {
                    existingAmigo.setIdUsuario(amigo.getIdUsuario());
                }
                if (amigo.getIdAmigo() != null) {
                    existingAmigo.setIdAmigo(amigo.getIdAmigo());
                }

                return existingAmigo;
            })
            .map(amigoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amigo.getId().toString())
        );
    }

    /**
     * {@code GET  /amigos} : get all the amigos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amigos in body.
     */
    @GetMapping("/amigos")
    public List<Amigo> getAllAmigos() {
        log.debug("REST request to get all Amigos");
        return amigoRepository.findAll();
    }

    /**
     * {@code GET  /amigos/:id} : get the "id" amigo.
     *
     * @param id the id of the amigo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amigo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amigos/{id}")
    public ResponseEntity<Amigo> getAmigo(@PathVariable Long id) {
        log.debug("REST request to get Amigo : {}", id);
        Optional<Amigo> amigo = amigoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(amigo);
    }

    /**
     * {@code DELETE  /amigos/:id} : delete the "id" amigo.
     *
     * @param id the id of the amigo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amigos/{id}")
    public ResponseEntity<Void> deleteAmigo(@PathVariable Long id) {
        log.debug("REST request to delete Amigo : {}", id);
        amigoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
