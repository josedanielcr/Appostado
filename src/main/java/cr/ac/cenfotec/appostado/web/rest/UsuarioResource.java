package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Usuario;
import cr.ac.cenfotec.appostado.repository.UserRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
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
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Usuario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UsuarioResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioResource.class);

    private static final String ENTITY_NAME = "usuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsuarioRepository usuarioRepository;

    private final UserRepository userRepository;

    public UsuarioResource(UsuarioRepository usuarioRepository, UserRepository userRepository) {
        this.usuarioRepository = usuarioRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /usuarios} : Create a new usuario.
     *
     * @param usuario the usuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new usuario, or with status {@code 400 (Bad Request)} if the usuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody Usuario usuario) throws URISyntaxException {
        log.debug("REST request to save Usuario : {}", usuario);
        if (usuario.getId() != null) {
            throw new BadRequestAlertException("A new usuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(usuario.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        Long userId = usuario.getUser().getId();
        userRepository.findById(userId).ifPresent(usuario::user);
        Usuario result = usuarioRepository.save(usuario);
        return ResponseEntity
            .created(new URI("/api/usuarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /usuarios/:id} : Updates an existing usuario.
     *
     * @param id the id of the usuario to save.
     * @param usuario the usuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuario,
     * or with status {@code 400 (Bad Request)} if the usuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the usuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> updateUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Usuario usuario
    ) throws URISyntaxException {
        log.debug("REST request to update Usuario : {}, {}", id, usuario);
        if (usuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Usuario result = usuarioRepository.save(usuario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /usuarios/:id} : Partial updates given fields of an existing usuario, field will ignore if it is null
     *
     * @param id the id of the usuario to save.
     * @param usuario the usuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated usuario,
     * or with status {@code 400 (Bad Request)} if the usuario is not valid,
     * or with status {@code 404 (Not Found)} if the usuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the usuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/usuarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Usuario> partialUpdateUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Usuario usuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update Usuario partially : {}, {}", id, usuario);
        if (usuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, usuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Usuario> result = usuarioRepository
            .findById(usuario.getId())
            .map(existingUsuario -> {
                if (usuario.getNombrePerfil() != null) {
                    existingUsuario.setNombrePerfil(usuario.getNombrePerfil());
                }
                if (usuario.getPais() != null) {
                    existingUsuario.setPais(usuario.getPais());
                }
                if (usuario.getFechaNacimiento() != null) {
                    existingUsuario.setFechaNacimiento(usuario.getFechaNacimiento());
                }

                return existingUsuario;
            })
            .map(usuarioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, usuario.getId().toString())
        );
    }

    /**
     * {@code GET  /usuarios} : get all the usuarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of usuarios in body.
     */
    @GetMapping("/usuarios")
    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios() {
        log.debug("REST request to get all Usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * {@code GET  /usuarios/:id} : get the "id" usuario.
     *
     * @param id the id of the usuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the usuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/usuarios/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        log.debug("REST request to get Usuario : {}", id);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(usuario);
    }

    /**
     * {@code DELETE  /usuarios/:id} : delete the "id" usuario.
     *
     * @param id the id of the usuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        log.debug("REST request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
