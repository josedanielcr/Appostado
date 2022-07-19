package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Compra;
import cr.ac.cenfotec.appostado.repository.CompraRepository;
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
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity(EntityManager em) {
        Compra compra = new Compra();
        return compra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity(EntityManager em) {
        Compra compra = new Compra();
        return compra;
    }

    @BeforeEach
    public void initTest() {
        compra = createEntity(em);
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        int databaseSizeBeforeCreate = compraRepository.findAll().size();
        // Create the Compra
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compra)))
            .andExpect(status().isCreated());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate + 1);
        Compra testCompra = compraList.get(compraList.size() - 1);
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);

        int databaseSizeBeforeCreate = compraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compra)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())));
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).get();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeDelete = compraRepository.findAll().size();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
