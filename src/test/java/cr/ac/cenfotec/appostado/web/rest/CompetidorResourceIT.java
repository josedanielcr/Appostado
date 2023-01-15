package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Competidor;
import cr.ac.cenfotec.appostado.repository.CompetidorRepository;
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
 * Integration tests for the {@link CompetidorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompetidorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/competidors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompetidorRepository competidorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetidorMockMvc;

    private Competidor competidor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competidor createEntity(EntityManager em) {
        Competidor competidor = new Competidor().nombre(DEFAULT_NOMBRE).foto(DEFAULT_FOTO);
        return competidor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competidor createUpdatedEntity(EntityManager em) {
        Competidor competidor = new Competidor().nombre(UPDATED_NOMBRE).foto(UPDATED_FOTO);
        return competidor;
    }

    @BeforeEach
    public void initTest() {
        competidor = createEntity(em);
    }

    @Test
    @Transactional
    void createCompetidor() throws Exception {
        int databaseSizeBeforeCreate = competidorRepository.findAll().size();
        // Create the Competidor
        restCompetidorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(competidor)))
            .andExpect(status().isCreated());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeCreate + 1);
        Competidor testCompetidor = competidorList.get(competidorList.size() - 1);
        assertThat(testCompetidor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCompetidor.getFoto()).isEqualTo(DEFAULT_FOTO);
    }

    @Test
    @Transactional
    void createCompetidorWithExistingId() throws Exception {
        // Create the Competidor with an existing ID
        competidor.setId(1L);

        int databaseSizeBeforeCreate = competidorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetidorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(competidor)))
            .andExpect(status().isBadRequest());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = competidorRepository.findAll().size();
        // set the field null
        competidor.setNombre(null);

        // Create the Competidor, which fails.

        restCompetidorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(competidor)))
            .andExpect(status().isBadRequest());

        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFotoIsRequired() throws Exception {
        int databaseSizeBeforeTest = competidorRepository.findAll().size();
        // set the field null
        competidor.setFoto(null);

        // Create the Competidor, which fails.

        restCompetidorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(competidor)))
            .andExpect(status().isBadRequest());

        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompetidors() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        // Get all the competidorList
        restCompetidorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competidor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getCompetidor() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        // Get the competidor
        restCompetidorMockMvc
            .perform(get(ENTITY_API_URL_ID, competidor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competidor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.foto").value(DEFAULT_FOTO));
    }

    @Test
    @Transactional
    void getNonExistingCompetidor() throws Exception {
        // Get the competidor
        restCompetidorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompetidor() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();

        // Update the competidor
        Competidor updatedCompetidor = competidorRepository.findById(competidor.getId()).get();
        // Disconnect from session so that the updates on updatedCompetidor are not directly saved in db
        em.detach(updatedCompetidor);
        updatedCompetidor.nombre(UPDATED_NOMBRE).foto(UPDATED_FOTO);

        restCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompetidor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
        Competidor testCompetidor = competidorList.get(competidorList.size() - 1);
        assertThat(testCompetidor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCompetidor.getFoto()).isEqualTo(UPDATED_FOTO);
    }

    @Test
    @Transactional
    void putNonExistingCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competidor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(competidor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetidorWithPatch() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();

        // Update the competidor using partial update
        Competidor partialUpdatedCompetidor = new Competidor();
        partialUpdatedCompetidor.setId(competidor.getId());

        partialUpdatedCompetidor.nombre(UPDATED_NOMBRE).foto(UPDATED_FOTO);

        restCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
        Competidor testCompetidor = competidorList.get(competidorList.size() - 1);
        assertThat(testCompetidor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCompetidor.getFoto()).isEqualTo(UPDATED_FOTO);
    }

    @Test
    @Transactional
    void fullUpdateCompetidorWithPatch() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();

        // Update the competidor using partial update
        Competidor partialUpdatedCompetidor = new Competidor();
        partialUpdatedCompetidor.setId(competidor.getId());

        partialUpdatedCompetidor.nombre(UPDATED_NOMBRE).foto(UPDATED_FOTO);

        restCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetidor))
            )
            .andExpect(status().isOk());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
        Competidor testCompetidor = competidorList.get(competidorList.size() - 1);
        assertThat(testCompetidor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCompetidor.getFoto()).isEqualTo(UPDATED_FOTO);
    }

    @Test
    @Transactional
    void patchNonExistingCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competidor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competidor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetidor() throws Exception {
        int databaseSizeBeforeUpdate = competidorRepository.findAll().size();
        competidor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetidorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(competidor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competidor in the database
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetidor() throws Exception {
        // Initialize the database
        competidorRepository.saveAndFlush(competidor);

        int databaseSizeBeforeDelete = competidorRepository.findAll().size();

        // Delete the competidor
        restCompetidorMockMvc
            .perform(delete(ENTITY_API_URL_ID, competidor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Competidor> competidorList = competidorRepository.findAll();
        assertThat(competidorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
