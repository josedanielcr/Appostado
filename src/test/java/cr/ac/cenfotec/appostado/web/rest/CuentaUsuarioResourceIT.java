package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
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
 * Integration tests for the {@link CuentaUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuentaUsuarioResourceIT {

    private static final Float DEFAULT_BALANCE = 1F;
    private static final Float UPDATED_BALANCE = 2F;

    private static final Integer DEFAULT_NUM_CANJES = 1;
    private static final Integer UPDATED_NUM_CANJES = 2;

    private static final Integer DEFAULT_APUESTAS_TOTALES = 1;
    private static final Integer UPDATED_APUESTAS_TOTALES = 2;

    private static final Integer DEFAULT_APUESTAS_GANADAS = 1;
    private static final Integer UPDATED_APUESTAS_GANADAS = 2;

    private static final String ENTITY_API_URL = "/api/cuenta-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CuentaUsuarioRepository cuentaUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuentaUsuarioMockMvc;

    private CuentaUsuario cuentaUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuentaUsuario createEntity(EntityManager em) {
        CuentaUsuario cuentaUsuario = new CuentaUsuario()
            .balance(DEFAULT_BALANCE)
            .numCanjes(DEFAULT_NUM_CANJES)
            .apuestasTotales(DEFAULT_APUESTAS_TOTALES)
            .apuestasGanadas(DEFAULT_APUESTAS_GANADAS);
        return cuentaUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuentaUsuario createUpdatedEntity(EntityManager em) {
        CuentaUsuario cuentaUsuario = new CuentaUsuario()
            .balance(UPDATED_BALANCE)
            .numCanjes(UPDATED_NUM_CANJES)
            .apuestasTotales(UPDATED_APUESTAS_TOTALES)
            .apuestasGanadas(UPDATED_APUESTAS_GANADAS);
        return cuentaUsuario;
    }

    @BeforeEach
    public void initTest() {
        cuentaUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createCuentaUsuario() throws Exception {
        int databaseSizeBeforeCreate = cuentaUsuarioRepository.findAll().size();
        // Create the CuentaUsuario
        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isCreated());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        CuentaUsuario testCuentaUsuario = cuentaUsuarioList.get(cuentaUsuarioList.size() - 1);
        assertThat(testCuentaUsuario.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testCuentaUsuario.getNumCanjes()).isEqualTo(DEFAULT_NUM_CANJES);
        assertThat(testCuentaUsuario.getApuestasTotales()).isEqualTo(DEFAULT_APUESTAS_TOTALES);
        assertThat(testCuentaUsuario.getApuestasGanadas()).isEqualTo(DEFAULT_APUESTAS_GANADAS);
    }

    @Test
    @Transactional
    void createCuentaUsuarioWithExistingId() throws Exception {
        // Create the CuentaUsuario with an existing ID
        cuentaUsuario.setId(1L);

        int databaseSizeBeforeCreate = cuentaUsuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaUsuarioRepository.findAll().size();
        // set the field null
        cuentaUsuario.setBalance(null);

        // Create the CuentaUsuario, which fails.

        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isBadRequest());

        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumCanjesIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaUsuarioRepository.findAll().size();
        // set the field null
        cuentaUsuario.setNumCanjes(null);

        // Create the CuentaUsuario, which fails.

        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isBadRequest());

        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApuestasTotalesIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaUsuarioRepository.findAll().size();
        // set the field null
        cuentaUsuario.setApuestasTotales(null);

        // Create the CuentaUsuario, which fails.

        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isBadRequest());

        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApuestasGanadasIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaUsuarioRepository.findAll().size();
        // set the field null
        cuentaUsuario.setApuestasGanadas(null);

        // Create the CuentaUsuario, which fails.

        restCuentaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isBadRequest());

        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCuentaUsuarios() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        // Get all the cuentaUsuarioList
        restCuentaUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuentaUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].numCanjes").value(hasItem(DEFAULT_NUM_CANJES)))
            .andExpect(jsonPath("$.[*].apuestasTotales").value(hasItem(DEFAULT_APUESTAS_TOTALES)))
            .andExpect(jsonPath("$.[*].apuestasGanadas").value(hasItem(DEFAULT_APUESTAS_GANADAS)));
    }

    @Test
    @Transactional
    void getCuentaUsuario() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        // Get the cuentaUsuario
        restCuentaUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, cuentaUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuentaUsuario.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.numCanjes").value(DEFAULT_NUM_CANJES))
            .andExpect(jsonPath("$.apuestasTotales").value(DEFAULT_APUESTAS_TOTALES))
            .andExpect(jsonPath("$.apuestasGanadas").value(DEFAULT_APUESTAS_GANADAS));
    }

    @Test
    @Transactional
    void getNonExistingCuentaUsuario() throws Exception {
        // Get the cuentaUsuario
        restCuentaUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCuentaUsuario() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();

        // Update the cuentaUsuario
        CuentaUsuario updatedCuentaUsuario = cuentaUsuarioRepository.findById(cuentaUsuario.getId()).get();
        // Disconnect from session so that the updates on updatedCuentaUsuario are not directly saved in db
        em.detach(updatedCuentaUsuario);
        updatedCuentaUsuario
            .balance(UPDATED_BALANCE)
            .numCanjes(UPDATED_NUM_CANJES)
            .apuestasTotales(UPDATED_APUESTAS_TOTALES)
            .apuestasGanadas(UPDATED_APUESTAS_GANADAS);

        restCuentaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCuentaUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCuentaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        CuentaUsuario testCuentaUsuario = cuentaUsuarioList.get(cuentaUsuarioList.size() - 1);
        assertThat(testCuentaUsuario.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCuentaUsuario.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
        assertThat(testCuentaUsuario.getApuestasTotales()).isEqualTo(UPDATED_APUESTAS_TOTALES);
        assertThat(testCuentaUsuario.getApuestasGanadas()).isEqualTo(UPDATED_APUESTAS_GANADAS);
    }

    @Test
    @Transactional
    void putNonExistingCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuentaUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuentaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuentaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuentaUsuarioWithPatch() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();

        // Update the cuentaUsuario using partial update
        CuentaUsuario partialUpdatedCuentaUsuario = new CuentaUsuario();
        partialUpdatedCuentaUsuario.setId(cuentaUsuario.getId());

        partialUpdatedCuentaUsuario
            .balance(UPDATED_BALANCE)
            .numCanjes(UPDATED_NUM_CANJES)
            .apuestasTotales(UPDATED_APUESTAS_TOTALES)
            .apuestasGanadas(UPDATED_APUESTAS_GANADAS);

        restCuentaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuentaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuentaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        CuentaUsuario testCuentaUsuario = cuentaUsuarioList.get(cuentaUsuarioList.size() - 1);
        assertThat(testCuentaUsuario.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCuentaUsuario.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
        assertThat(testCuentaUsuario.getApuestasTotales()).isEqualTo(UPDATED_APUESTAS_TOTALES);
        assertThat(testCuentaUsuario.getApuestasGanadas()).isEqualTo(UPDATED_APUESTAS_GANADAS);
    }

    @Test
    @Transactional
    void fullUpdateCuentaUsuarioWithPatch() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();

        // Update the cuentaUsuario using partial update
        CuentaUsuario partialUpdatedCuentaUsuario = new CuentaUsuario();
        partialUpdatedCuentaUsuario.setId(cuentaUsuario.getId());

        partialUpdatedCuentaUsuario
            .balance(UPDATED_BALANCE)
            .numCanjes(UPDATED_NUM_CANJES)
            .apuestasTotales(UPDATED_APUESTAS_TOTALES)
            .apuestasGanadas(UPDATED_APUESTAS_GANADAS);

        restCuentaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuentaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuentaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        CuentaUsuario testCuentaUsuario = cuentaUsuarioList.get(cuentaUsuarioList.size() - 1);
        assertThat(testCuentaUsuario.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testCuentaUsuario.getNumCanjes()).isEqualTo(UPDATED_NUM_CANJES);
        assertThat(testCuentaUsuario.getApuestasTotales()).isEqualTo(UPDATED_APUESTAS_TOTALES);
        assertThat(testCuentaUsuario.getApuestasGanadas()).isEqualTo(UPDATED_APUESTAS_GANADAS);
    }

    @Test
    @Transactional
    void patchNonExistingCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuentaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuentaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuentaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuentaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = cuentaUsuarioRepository.findAll().size();
        cuentaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cuentaUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuentaUsuario in the database
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuentaUsuario() throws Exception {
        // Initialize the database
        cuentaUsuarioRepository.saveAndFlush(cuentaUsuario);

        int databaseSizeBeforeDelete = cuentaUsuarioRepository.findAll().size();

        // Delete the cuentaUsuario
        restCuentaUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuentaUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CuentaUsuario> cuentaUsuarioList = cuentaUsuarioRepository.findAll();
        assertThat(cuentaUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
