package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
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
 * Integration tests for the {@link ApuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApuestaResourceIT {

    private static final Float DEFAULT_CREDITOS_APOSTADOS = 1F;
    private static final Float UPDATED_CREDITOS_APOSTADOS = 2F;

    private static final Boolean DEFAULT_HA_GANADO = false;
    private static final Boolean UPDATED_HA_GANADO = true;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApuestaRepository apuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApuestaMockMvc;

    private Apuesta apuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apuesta createEntity(EntityManager em) {
        Apuesta apuesta = new Apuesta().creditosApostados(DEFAULT_CREDITOS_APOSTADOS).haGanado(DEFAULT_HA_GANADO).estado(DEFAULT_ESTADO);
        return apuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apuesta createUpdatedEntity(EntityManager em) {
        Apuesta apuesta = new Apuesta().creditosApostados(UPDATED_CREDITOS_APOSTADOS).haGanado(UPDATED_HA_GANADO).estado(UPDATED_ESTADO);
        return apuesta;
    }

    @BeforeEach
    public void initTest() {
        apuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createApuesta() throws Exception {
        int databaseSizeBeforeCreate = apuestaRepository.findAll().size();
        // Create the Apuesta
        restApuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isCreated());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeCreate + 1);
        Apuesta testApuesta = apuestaList.get(apuestaList.size() - 1);
        assertThat(testApuesta.getCreditosApostados()).isEqualTo(DEFAULT_CREDITOS_APOSTADOS);
        assertThat(testApuesta.getHaGanado()).isEqualTo(DEFAULT_HA_GANADO);
        assertThat(testApuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createApuestaWithExistingId() throws Exception {
        // Create the Apuesta with an existing ID
        apuesta.setId(1L);

        int databaseSizeBeforeCreate = apuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isBadRequest());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreditosApostadosIsRequired() throws Exception {
        int databaseSizeBeforeTest = apuestaRepository.findAll().size();
        // set the field null
        apuesta.setCreditosApostados(null);

        // Create the Apuesta, which fails.

        restApuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isBadRequest());

        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = apuestaRepository.findAll().size();
        // set the field null
        apuesta.setEstado(null);

        // Create the Apuesta, which fails.

        restApuestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isBadRequest());

        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApuestas() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        // Get all the apuestaList
        restApuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditosApostados").value(hasItem(DEFAULT_CREDITOS_APOSTADOS.doubleValue())))
            .andExpect(jsonPath("$.[*].haGanado").value(hasItem(DEFAULT_HA_GANADO.booleanValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getApuesta() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        // Get the apuesta
        restApuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, apuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apuesta.getId().intValue()))
            .andExpect(jsonPath("$.creditosApostados").value(DEFAULT_CREDITOS_APOSTADOS.doubleValue()))
            .andExpect(jsonPath("$.haGanado").value(DEFAULT_HA_GANADO.booleanValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingApuesta() throws Exception {
        // Get the apuesta
        restApuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApuesta() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();

        // Update the apuesta
        Apuesta updatedApuesta = apuestaRepository.findById(apuesta.getId()).get();
        // Disconnect from session so that the updates on updatedApuesta are not directly saved in db
        em.detach(updatedApuesta);
        updatedApuesta.creditosApostados(UPDATED_CREDITOS_APOSTADOS).haGanado(UPDATED_HA_GANADO).estado(UPDATED_ESTADO);

        restApuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApuesta))
            )
            .andExpect(status().isOk());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
        Apuesta testApuesta = apuestaList.get(apuestaList.size() - 1);
        assertThat(testApuesta.getCreditosApostados()).isEqualTo(UPDATED_CREDITOS_APOSTADOS);
        assertThat(testApuesta.getHaGanado()).isEqualTo(UPDATED_HA_GANADO);
        assertThat(testApuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apuesta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApuestaWithPatch() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();

        // Update the apuesta using partial update
        Apuesta partialUpdatedApuesta = new Apuesta();
        partialUpdatedApuesta.setId(apuesta.getId());

        partialUpdatedApuesta.creditosApostados(UPDATED_CREDITOS_APOSTADOS).haGanado(UPDATED_HA_GANADO);

        restApuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApuesta))
            )
            .andExpect(status().isOk());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
        Apuesta testApuesta = apuestaList.get(apuestaList.size() - 1);
        assertThat(testApuesta.getCreditosApostados()).isEqualTo(UPDATED_CREDITOS_APOSTADOS);
        assertThat(testApuesta.getHaGanado()).isEqualTo(UPDATED_HA_GANADO);
        assertThat(testApuesta.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateApuestaWithPatch() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();

        // Update the apuesta using partial update
        Apuesta partialUpdatedApuesta = new Apuesta();
        partialUpdatedApuesta.setId(apuesta.getId());

        partialUpdatedApuesta.creditosApostados(UPDATED_CREDITOS_APOSTADOS).haGanado(UPDATED_HA_GANADO).estado(UPDATED_ESTADO);

        restApuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApuesta))
            )
            .andExpect(status().isOk());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
        Apuesta testApuesta = apuestaList.get(apuestaList.size() - 1);
        assertThat(testApuesta.getCreditosApostados()).isEqualTo(UPDATED_CREDITOS_APOSTADOS);
        assertThat(testApuesta.getHaGanado()).isEqualTo(UPDATED_HA_GANADO);
        assertThat(testApuesta.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apuesta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApuesta() throws Exception {
        int databaseSizeBeforeUpdate = apuestaRepository.findAll().size();
        apuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApuestaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apuesta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apuesta in the database
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApuesta() throws Exception {
        // Initialize the database
        apuestaRepository.saveAndFlush(apuesta);

        int databaseSizeBeforeDelete = apuestaRepository.findAll().size();

        // Delete the apuesta
        restApuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, apuesta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apuesta> apuestaList = apuestaRepository.findAll();
        assertThat(apuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
