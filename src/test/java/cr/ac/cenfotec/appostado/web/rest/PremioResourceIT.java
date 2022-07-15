package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Premio;
import cr.ac.cenfotec.appostado.repository.PremioRepository;
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
 * Integration tests for the {@link PremioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PremioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO = "BBBBBBBBBB";

    private static final Float DEFAULT_COSTO = 1F;
    private static final Float UPDATED_COSTO = 2F;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final Integer DEFAULT_NUM_CANJES = 1;
    private static final Integer UPDATED_NUM_CANJES = 2;

    private static final String ENTITY_API_URL = "/api/premios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PremioRepository premioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPremioMockMvc;

    private Premio premio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Premio createEntity(EntityManager em) {
        Premio premio = new Premio()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .foto(DEFAULT_FOTO)
            .costo(DEFAULT_COSTO)
            .estado(DEFAULT_ESTADO)
            .stock(DEFAULT_STOCK)
            .numCanjes(DEFAULT_NUM_CANJES);
        return premio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Premio createUpdatedEntity(EntityManager em) {
        Premio premio = new Premio()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .costo(UPDATED_COSTO)
            .estado(UPDATED_ESTADO)
            .stock(UPDATED_STOCK)
            .numCanjes(UPDATED_NUM_CANJES);
        return premio;
    }

    @BeforeEach
    public void initTest() {
        premio = createEntity(em);
    }

    @Test
    @Transactional
    void createPremio() throws Exception {
        int databaseSizeBeforeCreate = premioRepository.findAll().size();
        // Create the Premio
        restPremioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(premio)))
            .andExpect(status().isCreated());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeCreate + 1);
        Premio testPremio = premioList.get(premioList.size() - 1);
        assertThat(testPremio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPremio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPremio.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testPremio.getCosto()).isEqualTo(DEFAULT_COSTO);
        assertThat(testPremio.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPremio.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testPremio.getNumCanjes()).isEqualTo(DEFAULT_NUM_CANJES);
    }

    @Test
    @Transactional
    void createPremioWithExistingId() throws Exception {
        // Create the Premio with an existing ID
        premio.setId(1L);

        int databaseSizeBeforeCreate = premioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPremioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(premio)))
            .andExpect(status().isBadRequest());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPremios() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        // Get all the premioList
        restPremioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(premio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].numCanjes").value(hasItem(DEFAULT_NUM_CANJES)));
    }

    @Test
    @Transactional
    void getPremio() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        // Get the premio
        restPremioMockMvc
            .perform(get(ENTITY_API_URL_ID, premio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(premio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.foto").value(DEFAULT_FOTO))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO.doubleValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.numCanjes").value(DEFAULT_NUM_CANJES));
    }

    @Test
    @Transactional
    void getNonExistingPremio() throws Exception {
        // Get the premio
        restPremioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPremio() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        int databaseSizeBeforeUpdate = premioRepository.findAll().size();

        // Update the premio
        Premio updatedPremio = premioRepository.findById(premio.getId()).get();
        // Disconnect from session so that the updates on updatedPremio are not directly saved in db
        em.detach(updatedPremio);
        updatedPremio
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .costo(UPDATED_COSTO)
            .estado(UPDATED_ESTADO)
            .stock(UPDATED_STOCK)
            .numCanjes(UPDATED_NUM_CANJES);

        restPremioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPremio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPremio))
            )
            .andExpect(status().isOk());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
        Premio testPremio = premioList.get(premioList.size() - 1);
        assertThat(testPremio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPremio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPremio.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPremio.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testPremio.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPremio.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testPremio.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
    }

    @Test
    @Transactional
    void putNonExistingPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, premio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(premio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(premio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(premio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePremioWithPatch() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        int databaseSizeBeforeUpdate = premioRepository.findAll().size();

        // Update the premio using partial update
        Premio partialUpdatedPremio = new Premio();
        partialUpdatedPremio.setId(premio.getId());

        partialUpdatedPremio.foto(UPDATED_FOTO).costo(UPDATED_COSTO).estado(UPDATED_ESTADO).numCanjes(UPDATED_NUM_CANJES);

        restPremioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPremio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPremio))
            )
            .andExpect(status().isOk());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
        Premio testPremio = premioList.get(premioList.size() - 1);
        assertThat(testPremio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPremio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPremio.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPremio.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testPremio.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPremio.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testPremio.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
    }

    @Test
    @Transactional
    void fullUpdatePremioWithPatch() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        int databaseSizeBeforeUpdate = premioRepository.findAll().size();

        // Update the premio using partial update
        Premio partialUpdatedPremio = new Premio();
        partialUpdatedPremio.setId(premio.getId());

        partialUpdatedPremio
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .foto(UPDATED_FOTO)
            .costo(UPDATED_COSTO)
            .estado(UPDATED_ESTADO)
            .stock(UPDATED_STOCK)
            .numCanjes(UPDATED_NUM_CANJES);

        restPremioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPremio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPremio))
            )
            .andExpect(status().isOk());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
        Premio testPremio = premioList.get(premioList.size() - 1);
        assertThat(testPremio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPremio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPremio.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testPremio.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testPremio.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPremio.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testPremio.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
    }

    @Test
    @Transactional
    void patchNonExistingPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, premio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(premio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(premio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPremio() throws Exception {
        int databaseSizeBeforeUpdate = premioRepository.findAll().size();
        premio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPremioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(premio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Premio in the database
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePremio() throws Exception {
        // Initialize the database
        premioRepository.saveAndFlush(premio);

        int databaseSizeBeforeDelete = premioRepository.findAll().size();

        // Delete the premio
        restPremioMockMvc
            .perform(delete(ENTITY_API_URL_ID, premio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Premio> premioList = premioRepository.findAll();
        assertThat(premioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
