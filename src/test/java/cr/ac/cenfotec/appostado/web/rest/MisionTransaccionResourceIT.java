package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.MisionTransaccion;
import cr.ac.cenfotec.appostado.repository.MisionTransaccionRepository;
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
 * Integration tests for the {@link MisionTransaccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MisionTransaccionResourceIT {

    private static final String ENTITY_API_URL = "/api/mision-transaccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MisionTransaccionRepository misionTransaccionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMisionTransaccionMockMvc;

    private MisionTransaccion misionTransaccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisionTransaccion createEntity(EntityManager em) {
        MisionTransaccion misionTransaccion = new MisionTransaccion();
        return misionTransaccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisionTransaccion createUpdatedEntity(EntityManager em) {
        MisionTransaccion misionTransaccion = new MisionTransaccion();
        return misionTransaccion;
    }

    @BeforeEach
    public void initTest() {
        misionTransaccion = createEntity(em);
    }

    @Test
    @Transactional
    void createMisionTransaccion() throws Exception {
        int databaseSizeBeforeCreate = misionTransaccionRepository.findAll().size();
        // Create the MisionTransaccion
        restMisionTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isCreated());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeCreate + 1);
        MisionTransaccion testMisionTransaccion = misionTransaccionList.get(misionTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void createMisionTransaccionWithExistingId() throws Exception {
        // Create the MisionTransaccion with an existing ID
        misionTransaccion.setId(1L);

        int databaseSizeBeforeCreate = misionTransaccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMisionTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMisionTransaccions() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        // Get all the misionTransaccionList
        restMisionTransaccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misionTransaccion.getId().intValue())));
    }

    @Test
    @Transactional
    void getMisionTransaccion() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        // Get the misionTransaccion
        restMisionTransaccionMockMvc
            .perform(get(ENTITY_API_URL_ID, misionTransaccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(misionTransaccion.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMisionTransaccion() throws Exception {
        // Get the misionTransaccion
        restMisionTransaccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMisionTransaccion() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();

        // Update the misionTransaccion
        MisionTransaccion updatedMisionTransaccion = misionTransaccionRepository.findById(misionTransaccion.getId()).get();
        // Disconnect from session so that the updates on updatedMisionTransaccion are not directly saved in db
        em.detach(updatedMisionTransaccion);

        restMisionTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMisionTransaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMisionTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
        MisionTransaccion testMisionTransaccion = misionTransaccionList.get(misionTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misionTransaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMisionTransaccionWithPatch() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();

        // Update the misionTransaccion using partial update
        MisionTransaccion partialUpdatedMisionTransaccion = new MisionTransaccion();
        partialUpdatedMisionTransaccion.setId(misionTransaccion.getId());

        restMisionTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisionTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisionTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
        MisionTransaccion testMisionTransaccion = misionTransaccionList.get(misionTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMisionTransaccionWithPatch() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();

        // Update the misionTransaccion using partial update
        MisionTransaccion partialUpdatedMisionTransaccion = new MisionTransaccion();
        partialUpdatedMisionTransaccion.setId(misionTransaccion.getId());

        restMisionTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisionTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisionTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
        MisionTransaccion testMisionTransaccion = misionTransaccionList.get(misionTransaccionList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, misionTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMisionTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = misionTransaccionRepository.findAll().size();
        misionTransaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misionTransaccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisionTransaccion in the database
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMisionTransaccion() throws Exception {
        // Initialize the database
        misionTransaccionRepository.saveAndFlush(misionTransaccion);

        int databaseSizeBeforeDelete = misionTransaccionRepository.findAll().size();

        // Delete the misionTransaccion
        restMisionTransaccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, misionTransaccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MisionTransaccion> misionTransaccionList = misionTransaccionRepository.findAll();
        assertThat(misionTransaccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
