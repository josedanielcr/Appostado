package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Deporte;
import cr.ac.cenfotec.appostado.repository.DeporteRepository;
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
 * Integration tests for the {@link DeporteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeporteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deportes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeporteRepository deporteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeporteMockMvc;

    private Deporte deporte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deporte createEntity(EntityManager em) {
        Deporte deporte = new Deporte().nombre(DEFAULT_NOMBRE);
        return deporte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deporte createUpdatedEntity(EntityManager em) {
        Deporte deporte = new Deporte().nombre(UPDATED_NOMBRE);
        return deporte;
    }

    @BeforeEach
    public void initTest() {
        deporte = createEntity(em);
    }

    @Test
    @Transactional
    void createDeporte() throws Exception {
        int databaseSizeBeforeCreate = deporteRepository.findAll().size();
        // Create the Deporte
        restDeporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deporte)))
            .andExpect(status().isCreated());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeCreate + 1);
        Deporte testDeporte = deporteList.get(deporteList.size() - 1);
        assertThat(testDeporte.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createDeporteWithExistingId() throws Exception {
        // Create the Deporte with an existing ID
        deporte.setId(1L);

        int databaseSizeBeforeCreate = deporteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deporte)))
            .andExpect(status().isBadRequest());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = deporteRepository.findAll().size();
        // set the field null
        deporte.setNombre(null);

        // Create the Deporte, which fails.

        restDeporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deporte)))
            .andExpect(status().isBadRequest());

        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeportes() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        // Get all the deporteList
        restDeporteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deporte.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getDeporte() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        // Get the deporte
        restDeporteMockMvc
            .perform(get(ENTITY_API_URL_ID, deporte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deporte.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingDeporte() throws Exception {
        // Get the deporte
        restDeporteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeporte() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();

        // Update the deporte
        Deporte updatedDeporte = deporteRepository.findById(deporte.getId()).get();
        // Disconnect from session so that the updates on updatedDeporte are not directly saved in db
        em.detach(updatedDeporte);
        updatedDeporte.nombre(UPDATED_NOMBRE);

        restDeporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeporte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeporte))
            )
            .andExpect(status().isOk());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
        Deporte testDeporte = deporteList.get(deporteList.size() - 1);
        assertThat(testDeporte.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deporte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deporte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deporte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deporte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeporteWithPatch() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();

        // Update the deporte using partial update
        Deporte partialUpdatedDeporte = new Deporte();
        partialUpdatedDeporte.setId(deporte.getId());

        restDeporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeporte))
            )
            .andExpect(status().isOk());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
        Deporte testDeporte = deporteList.get(deporteList.size() - 1);
        assertThat(testDeporte.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateDeporteWithPatch() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();

        // Update the deporte using partial update
        Deporte partialUpdatedDeporte = new Deporte();
        partialUpdatedDeporte.setId(deporte.getId());

        partialUpdatedDeporte.nombre(UPDATED_NOMBRE);

        restDeporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeporte))
            )
            .andExpect(status().isOk());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
        Deporte testDeporte = deporteList.get(deporteList.size() - 1);
        assertThat(testDeporte.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deporte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deporte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeporte() throws Exception {
        int databaseSizeBeforeUpdate = deporteRepository.findAll().size();
        deporte.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeporteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deporte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deporte in the database
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeporte() throws Exception {
        // Initialize the database
        deporteRepository.saveAndFlush(deporte);

        int databaseSizeBeforeDelete = deporteRepository.findAll().size();

        // Delete the deporte
        restDeporteMockMvc
            .perform(delete(ENTITY_API_URL_ID, deporte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deporte> deporteList = deporteRepository.findAll();
        assertThat(deporteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
