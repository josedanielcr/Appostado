package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Liga;
import cr.ac.cenfotec.appostado.repository.LigaRepository;
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
 * Integration tests for the {@link LigaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ligas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigaRepository ligaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigaMockMvc;

    private Liga liga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liga createEntity(EntityManager em) {
        Liga liga = new Liga().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).foto(DEFAULT_FOTO);
        return liga;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Liga createUpdatedEntity(EntityManager em) {
        Liga liga = new Liga().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).foto(UPDATED_FOTO);
        return liga;
    }

    @BeforeEach
    public void initTest() {
        liga = createEntity(em);
    }

    @Test
    @Transactional
    void createLiga() throws Exception {
        int databaseSizeBeforeCreate = ligaRepository.findAll().size();
        // Create the Liga
        restLigaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isCreated());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeCreate + 1);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testLiga.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testLiga.getFoto()).isEqualTo(DEFAULT_FOTO);
    }

    @Test
    @Transactional
    void createLigaWithExistingId() throws Exception {
        // Create the Liga with an existing ID
        liga.setId(1L);

        int databaseSizeBeforeCreate = ligaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligaRepository.findAll().size();
        // set the field null
        liga.setNombre(null);

        // Create the Liga, which fails.

        restLigaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isBadRequest());

        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLigas() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        // Get all the ligaList
        restLigaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(liga.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(DEFAULT_FOTO)));
    }

    @Test
    @Transactional
    void getLiga() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        // Get the liga
        restLigaMockMvc
            .perform(get(ENTITY_API_URL_ID, liga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(liga.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.foto").value(DEFAULT_FOTO));
    }

    @Test
    @Transactional
    void getNonExistingLiga() throws Exception {
        // Get the liga
        restLigaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLiga() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();

        // Update the liga
        Liga updatedLiga = ligaRepository.findById(liga.getId()).get();
        // Disconnect from session so that the updates on updatedLiga are not directly saved in db
        em.detach(updatedLiga);
        updatedLiga.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).foto(UPDATED_FOTO);

        restLigaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLiga.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLiga))
            )
            .andExpect(status().isOk());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLiga.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testLiga.getFoto()).isEqualTo(UPDATED_FOTO);
    }

    @Test
    @Transactional
    void putNonExistingLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, liga.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(liga))
            )
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(liga))
            )
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigaWithPatch() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();

        // Update the liga using partial update
        Liga partialUpdatedLiga = new Liga();
        partialUpdatedLiga.setId(liga.getId());

        partialUpdatedLiga.nombre(UPDATED_NOMBRE);

        restLigaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLiga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLiga))
            )
            .andExpect(status().isOk());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLiga.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testLiga.getFoto()).isEqualTo(DEFAULT_FOTO);
    }

    @Test
    @Transactional
    void fullUpdateLigaWithPatch() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();

        // Update the liga using partial update
        Liga partialUpdatedLiga = new Liga();
        partialUpdatedLiga.setId(liga.getId());

        partialUpdatedLiga.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).foto(UPDATED_FOTO);

        restLigaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLiga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLiga))
            )
            .andExpect(status().isOk());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
        Liga testLiga = ligaList.get(ligaList.size() - 1);
        assertThat(testLiga.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLiga.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testLiga.getFoto()).isEqualTo(UPDATED_FOTO);
    }

    @Test
    @Transactional
    void patchNonExistingLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, liga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(liga))
            )
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(liga))
            )
            .andExpect(status().isBadRequest());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLiga() throws Exception {
        int databaseSizeBeforeUpdate = ligaRepository.findAll().size();
        liga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(liga)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Liga in the database
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLiga() throws Exception {
        // Initialize the database
        ligaRepository.saveAndFlush(liga);

        int databaseSizeBeforeDelete = ligaRepository.findAll().size();

        // Delete the liga
        restLigaMockMvc
            .perform(delete(ENTITY_API_URL_ID, liga.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Liga> ligaList = ligaRepository.findAll();
        assertThat(ligaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
