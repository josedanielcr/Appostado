package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.ProductoUsuario;
import cr.ac.cenfotec.appostado.repository.ProductoUsuarioRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductoUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoUsuarioResourceIT {

    private static final Boolean DEFAULT_RECLAMADO = false;
    private static final Boolean UPDATED_RECLAMADO = true;

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/producto-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductoUsuarioRepository productoUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoUsuarioMockMvc;

    private ProductoUsuario productoUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoUsuario createEntity(EntityManager em) {
        ProductoUsuario productoUsuario = new ProductoUsuario().reclamado(DEFAULT_RECLAMADO).codigo(DEFAULT_CODIGO);
        return productoUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoUsuario createUpdatedEntity(EntityManager em) {
        ProductoUsuario productoUsuario = new ProductoUsuario().reclamado(UPDATED_RECLAMADO).codigo(UPDATED_CODIGO);
        return productoUsuario;
    }

    @BeforeEach
    public void initTest() {
        productoUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createProductoUsuario() throws Exception {
        int databaseSizeBeforeCreate = productoUsuarioRepository.findAll().size();
        // Create the ProductoUsuario
        restProductoUsuarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isCreated());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoUsuario testProductoUsuario = productoUsuarioList.get(productoUsuarioList.size() - 1);
        assertThat(testProductoUsuario.getReclamado()).isEqualTo(DEFAULT_RECLAMADO);
        assertThat(testProductoUsuario.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void createProductoUsuarioWithExistingId() throws Exception {
        // Create the ProductoUsuario with an existing ID
        productoUsuario.setId(1L);

        int databaseSizeBeforeCreate = productoUsuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoUsuarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReclamadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoUsuarioRepository.findAll().size();
        // set the field null
        productoUsuario.setReclamado(null);

        // Create the ProductoUsuario, which fails.

        restProductoUsuarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoUsuarioRepository.findAll().size();
        // set the field null
        productoUsuario.setCodigo(null);

        // Create the ProductoUsuario, which fails.

        restProductoUsuarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductoUsuarios() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        // Get all the productoUsuarioList
        restProductoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].reclamado").value(hasItem(DEFAULT_RECLAMADO.booleanValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getProductoUsuario() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        // Get the productoUsuario
        restProductoUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, productoUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productoUsuario.getId().intValue()))
            .andExpect(jsonPath("$.reclamado").value(DEFAULT_RECLAMADO.booleanValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getNonExistingProductoUsuario() throws Exception {
        // Get the productoUsuario
        restProductoUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductoUsuario() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();

        // Update the productoUsuario
        ProductoUsuario updatedProductoUsuario = productoUsuarioRepository.findById(productoUsuario.getId()).get();
        // Disconnect from session so that the updates on updatedProductoUsuario are not directly saved in db
        em.detach(updatedProductoUsuario);
        updatedProductoUsuario.reclamado(UPDATED_RECLAMADO).codigo(UPDATED_CODIGO);

        restProductoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        ProductoUsuario testProductoUsuario = productoUsuarioList.get(productoUsuarioList.size() - 1);
        assertThat(testProductoUsuario.getReclamado()).isEqualTo(UPDATED_RECLAMADO);
        assertThat(testProductoUsuario.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void putNonExistingProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoUsuarioWithPatch() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();

        // Update the productoUsuario using partial update
        ProductoUsuario partialUpdatedProductoUsuario = new ProductoUsuario();
        partialUpdatedProductoUsuario.setId(productoUsuario.getId());

        restProductoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        ProductoUsuario testProductoUsuario = productoUsuarioList.get(productoUsuarioList.size() - 1);
        assertThat(testProductoUsuario.getReclamado()).isEqualTo(DEFAULT_RECLAMADO);
        assertThat(testProductoUsuario.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void fullUpdateProductoUsuarioWithPatch() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();

        // Update the productoUsuario using partial update
        ProductoUsuario partialUpdatedProductoUsuario = new ProductoUsuario();
        partialUpdatedProductoUsuario.setId(productoUsuario.getId());

        partialUpdatedProductoUsuario.reclamado(UPDATED_RECLAMADO).codigo(UPDATED_CODIGO);

        restProductoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
        ProductoUsuario testProductoUsuario = productoUsuarioList.get(productoUsuarioList.size() - 1);
        assertThat(testProductoUsuario.getReclamado()).isEqualTo(UPDATED_RECLAMADO);
        assertThat(testProductoUsuario.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void patchNonExistingProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductoUsuario() throws Exception {
        int databaseSizeBeforeUpdate = productoUsuarioRepository.findAll().size();
        productoUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productoUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoUsuario in the database
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductoUsuario() throws Exception {
        // Initialize the database
        productoUsuarioRepository.saveAndFlush(productoUsuario);

        int databaseSizeBeforeDelete = productoUsuarioRepository.findAll().size();

        // Delete the productoUsuario
        restProductoUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, productoUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoUsuario> productoUsuarioList = productoUsuarioRepository.findAll();
        assertThat(productoUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
