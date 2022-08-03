package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.*;
import cr.ac.cenfotec.appostado.security.SecurityUtils;
import cr.ac.cenfotec.appostado.service.TwilioMailService;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
    private final CuentaUsuarioRepository cuentaUsuarioRepository;
    private final TransaccionRepository transaccionRepository;
    private final UserRepository userRepository;
    private final CompraRepository compraRepository;
    private final TwilioMailService twilioMailService;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProductoUsuarioResource(
        ProductoUsuarioRepository productoUsuarioRepository,
        CuentaUsuarioRepository cuentaUsuarioRepository,
        TransaccionRepository transaccionRepository,
        UserRepository userRepository,
        CompraRepository compraRepository,
        TwilioMailService twilioMailService,
        ProductoRepository productoRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.productoUsuarioRepository = productoUsuarioRepository;
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.transaccionRepository = transaccionRepository;
        this.userRepository = userRepository;
        this.compraRepository = compraRepository;
        this.twilioMailService = twilioMailService;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
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

    @GetMapping("/producto-usuarios/bonificacion/{idProducto}")
    public String getBonificacion(@PathVariable("idProducto") Long idProducto) throws IOException {
        log.debug("REST request to buy product : {}", idProducto);
        String respuesta = "";
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        Optional<User> user = userRepository.findOneByLogin(userLogin.get());
        Optional<CuentaUsuario> cuentaUsuario = cuentaUsuarioRepository.findByUsuarioId(user.get().getId());
        Optional<Producto> producto = productoRepository.findById(idProducto);
        Optional<Usuario> usuario = usuarioRepository.findById(user.get().getId());

        producto.ifPresent(producto1 -> producto1.setNumCompras(producto.get().getNumCompras() + 1));

        Transaccion transaccion = new Transaccion();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();

        transaccion.setDescripcion("Nombre del producto: " + producto.get().getNombre());
        transaccion.setTipo("Bono");
        transaccion.setMonto(producto.get().getCosto());
        transaccion.setCuenta(cuentaUsuario.get());
        transaccion.setFecha(localDate);
        transaccionRepository.save(transaccion);

        char[] codigo = generatePassword(8);

        ProductoUsuario respuestaC = productoUsuarioRepository.findByCodigo(String.valueOf(codigo));

        while (respuestaC != null) {
            codigo = generatePassword(8);
            respuestaC = productoUsuarioRepository.findByCodigo(String.valueOf(codigo));
        }

        ProductoUsuario productoUsuario = new ProductoUsuario();

        productoUsuario.setReclamado(false);
        productoUsuario.setUsuario(usuario.get());
        productoUsuario.setProducto(producto.get());
        productoUsuario.setCodigo(String.valueOf(codigo));

        productoUsuarioRepository.save(productoUsuario);

        Compra compra = new Compra();

        compra.setProducto(producto.get());
        compra.setTransaccion(transaccion);

        compraRepository.save(compra);

        String userCorreo;
        String usuarioName;
        String transaccionInfo;
        String detalle;

        userCorreo = user.get().getEmail();
        usuarioName = usuario.get().getNombrePerfil();
        transaccionInfo = " Y el número de transacción es el: " + transaccion.getId();
        detalle =
            "El producto adquirido fue: " +
            producto.get().getNombre() +
            " y el código para realizar el canje es el siguiente: " +
            String.valueOf(codigo);

        twilioMailService.sendRedeemCodeMail(userCorreo, usuarioName, detalle, transaccionInfo);

        respuesta = "si";

        return respuesta;
    }

    @GetMapping("/producto-usuarios/bono/{codigo}")
    public String getCanjeProducto(@PathVariable("codigo") String codigo) throws IOException {
        log.debug("REST para hacer el canje con el siguiente codigo", codigo);
        String respuesta = "";
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> user = userRepository.findOneByLogin(userLogin.get());
        Optional<CuentaUsuario> cuentaUsuario = cuentaUsuarioRepository.findByUsuarioId(user.get().getId());

        ProductoUsuario productoUsuario = productoUsuarioRepository.findByCodigo(codigo);
        if (productoUsuario == null) {
            respuesta = "codInv";
        } else {
            if (productoUsuario.getReclamado() == false) {
                productoUsuario.setReclamado(true);
                Optional<Producto> producto = productoRepository.findById(productoUsuario.getProducto().getId());
                float bono = cuentaUsuario.get().getBalance() + producto.get().getCosto();

                cuentaUsuario.ifPresent(cuentaUsuario1 -> cuentaUsuario1.setBalance(bono));

                respuesta = "si";
            } else {
                respuesta = "no";
            }
        }

        return respuesta;
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

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}
