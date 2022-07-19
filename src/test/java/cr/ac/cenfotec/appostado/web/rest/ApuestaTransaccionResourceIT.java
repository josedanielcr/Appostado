package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.ApuestaTransaccion;
import cr.ac.cenfotec.appostado.repository.ApuestaTransaccionRepository;
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
 * Integration tests for the {@link ApuestaTransaccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApuestaTransaccionResourceIT {

    private static final String ENTITY_API_URL = "/api/apuesta-transaccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApuestaTransaccionRepository apuestaTransaccionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApuestaTransaccionMockMvc;

    private ApuestaTransaccion apuestaTransaccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApuestaTransaccion createEntity(EntityManager em) {
        ApuestaTransaccion apuestaTransaccion = new ApuestaTransaccion();
        return apuestaTransaccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApuestaTransaccion createUpdatedEntity(EntityManager em) {
        ApuestaTransaccion apuestaTransaccion = new ApuestaTransaccion();
        return apuestaTransaccion;
    }

    @BeforeEach
    public void initTest() {
        apuestaTransaccion = createEntity(em);
    }

    @Test
    @Transactional
    void createApuestaTransaccion() throws Exception {
        int databaseSizeBeforeCreate = apuestaTransaccionRepository.findAll().size();
        // Create the ApuestaTransaccion
        restApuestaTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isCreated());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeCreate + 1);
        ApuestaTransaccion testApuestaTransaccion = apuestaTransaccionList.get(apuestaTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void createApuestaTransaccionWithExistingId() throws Exception {
        // Create the ApuestaTransaccion with an existing ID
        apuestaTransaccion.setId(1L);

        int databaseSizeBeforeCreate = apuestaTransaccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApuestaTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApuestaTransaccions() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        // Get all the apuestaTransaccionList
        restApuestaTransaccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apuestaTransaccion.getId().intValue())));
    }

    @Test
    @Transactional
    void getApuestaTransaccion() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        // Get the apuestaTransaccion
        restApuestaTransaccionMockMvc
            .perform(get(ENTITY_API_URL_ID, apuestaTransaccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apuestaTransaccion.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingApuestaTransaccion() throws Exception {
        // Get the apuestaTransaccion
        restApuestaTransaccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApuestaTransaccion() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();

        // Update the apuestaTransaccion
        ApuestaTransaccion updatedApuestaTransaccion = apuestaTransaccionRepository.findById(apuestaTransaccion.getId()).get();
        // Disconnect from session so that the updates on updatedApuestaTransaccion are not directly saved in db
        em.detach(updatedApuestaTransaccion);

        restApuestaTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApuestaTransaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApuestaTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
        ApuestaTransaccion testApuestaTransaccion = apuestaTransaccionList.get(apuestaTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apuestaTransaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApuestaTransaccionWithPatch() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();

        // Update the apuestaTransaccion using partial update
        ApuestaTransaccion partialUpdatedApuestaTransaccion = new ApuestaTransaccion();
        partialUpdatedApuestaTransaccion.setId(apuestaTransaccion.getId());

        restApuestaTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApuestaTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApuestaTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
        ApuestaTransaccion testApuestaTransaccion = apuestaTransaccionList.get(apuestaTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateApuestaTransaccionWithPatch() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();

        // Update the apuestaTransaccion using partial update
        ApuestaTransaccion partialUpdatedApuestaTransaccion = new ApuestaTransaccion();
        partialUpdatedApuestaTransaccion.setId(apuestaTransaccion.getId());

        restApuestaTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApuestaTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApuestaTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
        ApuestaTransaccion testApuestaTransaccion = apuestaTransaccionList.get(apuestaTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apuestaTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApuestaTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = apuestaTransaccionRepository.findAll().size();
        apuestaTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apuestaTransaccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApuestaTransaccion in the database
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApuestaTransaccion() throws Exception {
        // Initialize the database
        apuestaTransaccionRepository.saveAndFlush(apuestaTransaccion);

        int databaseSizeBeforeDelete = apuestaTransaccionRepository.findAll().size();

        // Delete the apuestaTransaccion
        restApuestaTransaccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, apuestaTransaccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApuestaTransaccion> apuestaTransaccionList = apuestaTransaccionRepository.findAll();
        assertThat(apuestaTransaccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
