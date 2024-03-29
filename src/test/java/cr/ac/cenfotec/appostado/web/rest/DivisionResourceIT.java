package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Division;
import cr.ac.cenfotec.appostado.repository.DivisionRepository;
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
 * Integration tests for the {@link DivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DivisionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDivisionMockMvc;

    private Division division;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createEntity(EntityManager em) {
        Division division = new Division().nombre(DEFAULT_NOMBRE).region(DEFAULT_REGION);
        return division;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createUpdatedEntity(EntityManager em) {
        Division division = new Division().nombre(UPDATED_NOMBRE).region(UPDATED_REGION);
        return division;
    }

    @BeforeEach
    public void initTest() {
        division = createEntity(em);
    }

    @Test
    @Transactional
    void createDivision() throws Exception {
        int databaseSizeBeforeCreate = divisionRepository.findAll().size();
        // Create the Division
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isCreated());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate + 1);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDivision.getRegion()).isEqualTo(DEFAULT_REGION);
    }

    @Test
    @Transactional
    void createDivisionWithExistingId() throws Exception {
        // Create the Division with an existing ID
        division.setId(1L);

        int databaseSizeBeforeCreate = divisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setNombre(null);

        // Create the Division, which fails.

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setRegion(null);

        // Create the Division, which fails.

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDivisions() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)));
    }

    @Test
    @Transactional
    void getDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get the division
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, division.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(division.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION));
    }

    @Test
    @Transactional
    void getNonExistingDivision() throws Exception {
        // Get the division
        restDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division
        Division updatedDivision = divisionRepository.findById(division.getId()).get();
        // Disconnect from session so that the updates on updatedDivision are not directly saved in db
        em.detach(updatedDivision);
        updatedDivision.nombre(UPDATED_NOMBRE).region(UPDATED_REGION);

        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDivision.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void putNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, division.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision.region(UPDATED_REGION);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDivision.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void fullUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision.nombre(UPDATED_NOMBRE).region(UPDATED_REGION);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDivision.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    void patchNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, division.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeDelete = divisionRepository.findAll().size();

        // Delete the division
        restDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, division.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
