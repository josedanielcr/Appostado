package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.DivisionCompetidor;
import cr.ac.cenfotec.appostado.repository.DivisionCompetidorRepository;
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
 * Integration tests for the {@link DivisionCompetidorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DivisionCompetidorResourceIT {

    private static final String ENTITY_API_URL = "/api/division-competidors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DivisionCompetidorRepository divisionCompetidorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDivisionCompetidorMockMvc;

    private DivisionCompetidor divisionCompetidor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DivisionCompetidor createEntity(EntityManager em) {
        DivisionCompetidor divisionCompetidor = new DivisionCompetidor();
        return divisionCompetidor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DivisionCompetidor createUpdatedEntity(EntityManager em) {
        DivisionCompetidor divisionCompetidor = new DivisionCompetidor();
        return divisionCompetidor;
    }

    @BeforeEach
    public void initTest() {
        divisionCompetidor = createEntity(em);
    }

    @Test
    @Transactional
    void createDivisionCompetidor() throws Exception {
        int databaseSizeBeforeCreate = divisionCompetidorRepository.findAll().size();
        // Create the DivisionCompetidor
        restDivisionCompetidorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isCreated());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeCreate + 1);
        DivisionCompetidor testDivisionCompetidor = divisionCompetidorList.get(divisionCompetidorList.size() - 1);
    }

    @Test
    @Transactional
    void createDivisionCompetidorWithExistingId() throws Exception {
        // Create the DivisionCompetidor with an existing ID
        divisionCompetidor.setId(1L);

        int databaseSizeBeforeCreate = divisionCompetidorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDivisionCompetidorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDivisionCompetidors() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        // Get all the divisionCompetidorList
        restDivisionCompetidorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(divisionCompetidor.getId().intValue())));
    }

    @Test
    @Transactional
    void getDivisionCompetidor() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        // Get the divisionCompetidor
        restDivisionCompetidorMockMvc
            .perform(get(ENTITY_API_URL_ID, divisionCompetidor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(divisionCompetidor.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDivisionCompetidor() throws Exception {
        // Get the divisionCompetidor
        restDivisionCompetidorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDivisionCompetidor() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();

        // Update the divisionCompetidor
        DivisionCompetidor updatedDivisionCompetidor = divisionCompetidorRepository.findById(divisionCompetidor.getId()).get();
        // Disconnect from session so that the updates on updatedDivisionCompetidor are not directly saved in db
        em.detach(updatedDivisionCompetidor);

        restDivisionCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDivisionCompetidor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDivisionCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
        DivisionCompetidor testDivisionCompetidor = divisionCompetidorList.get(divisionCompetidorList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, divisionCompetidor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDivisionCompetidorWithPatch() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();

        // Update the divisionCompetidor using partial update
        DivisionCompetidor partialUpdatedDivisionCompetidor = new DivisionCompetidor();
        partialUpdatedDivisionCompetidor.setId(divisionCompetidor.getId());

        restDivisionCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivisionCompetidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivisionCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
        DivisionCompetidor testDivisionCompetidor = divisionCompetidorList.get(divisionCompetidorList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDivisionCompetidorWithPatch() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();

        // Update the divisionCompetidor using partial update
        DivisionCompetidor partialUpdatedDivisionCompetidor = new DivisionCompetidor();
        partialUpdatedDivisionCompetidor.setId(divisionCompetidor.getId());

        restDivisionCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivisionCompetidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivisionCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
        DivisionCompetidor testDivisionCompetidor = divisionCompetidorList.get(divisionCompetidorList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, divisionCompetidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDivisionCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = divisionCompetidorRepository.findAll().size();
        divisionCompetidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(divisionCompetidor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DivisionCompetidor in the database
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDivisionCompetidor() throws Exception {
        // Initialize the database
        divisionCompetidorRepository.saveAndFlush(divisionCompetidor);

        int databaseSizeBeforeDelete = divisionCompetidorRepository.findAll().size();

        // Delete the divisionCompetidor
        restDivisionCompetidorMockMvc
            .perform(delete(ENTITY_API_URL_ID, divisionCompetidor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DivisionCompetidor> divisionCompetidorList = divisionCompetidorRepository.findAll();
        assertThat(divisionCompetidorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
