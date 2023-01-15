package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Quiniela;
import cr.ac.cenfotec.appostado.repository.QuinielaRepository;
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
 * Integration tests for the {@link QuinielaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuinielaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_COSTO_PARTICIPACION = 1F;
    private static final Float UPDATED_COSTO_PARTICIPACION = 2F;

    private static final Float DEFAULT_PRIMER_PREMIO = 1F;
    private static final Float UPDATED_PRIMER_PREMIO = 2F;

    private static final Float DEFAULT_SEGUNDO_PREMIO = 1F;
    private static final Float UPDATED_SEGUNDO_PREMIO = 2F;

    private static final Float DEFAULT_TERCER_PREMIO = 1F;
    private static final Float UPDATED_TERCER_PREMIO = 2F;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quinielas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuinielaRepository quinielaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuinielaMockMvc;

    private Quiniela quiniela;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiniela createEntity(EntityManager em) {
        Quiniela quiniela = new Quiniela()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .costoParticipacion(DEFAULT_COSTO_PARTICIPACION)
            .primerPremio(DEFAULT_PRIMER_PREMIO)
            .segundoPremio(DEFAULT_SEGUNDO_PREMIO)
            .tercerPremio(DEFAULT_TERCER_PREMIO)
            .estado(DEFAULT_ESTADO);
        return quiniela;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiniela createUpdatedEntity(EntityManager em) {
        Quiniela quiniela = new Quiniela()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .costoParticipacion(UPDATED_COSTO_PARTICIPACION)
            .primerPremio(UPDATED_PRIMER_PREMIO)
            .segundoPremio(UPDATED_SEGUNDO_PREMIO)
            .tercerPremio(UPDATED_TERCER_PREMIO)
            .estado(UPDATED_ESTADO);
        return quiniela;
    }

    @BeforeEach
    public void initTest() {
        quiniela = createEntity(em);
    }

    @Test
    @Transactional
    void createQuiniela() throws Exception {
        int databaseSizeBeforeCreate = quinielaRepository.findAll().size();
        // Create the Quiniela
        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isCreated());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeCreate + 1);
        Quiniela testQuiniela = quinielaList.get(quinielaList.size() - 1);
        assertThat(testQuiniela.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testQuiniela.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testQuiniela.getCostoParticipacion()).isEqualTo(DEFAULT_COSTO_PARTICIPACION);
        assertThat(testQuiniela.getPrimerPremio()).isEqualTo(DEFAULT_PRIMER_PREMIO);
        assertThat(testQuiniela.getSegundoPremio()).isEqualTo(DEFAULT_SEGUNDO_PREMIO);
        assertThat(testQuiniela.getTercerPremio()).isEqualTo(DEFAULT_TERCER_PREMIO);
        assertThat(testQuiniela.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createQuinielaWithExistingId() throws Exception {
        // Create the Quiniela with an existing ID
        quiniela.setId(1L);

        int databaseSizeBeforeCreate = quinielaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setNombre(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostoParticipacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setCostoParticipacion(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrimerPremioIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setPrimerPremio(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSegundoPremioIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setSegundoPremio(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTercerPremioIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setTercerPremio(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = quinielaRepository.findAll().size();
        // set the field null
        quiniela.setEstado(null);

        // Create the Quiniela, which fails.

        restQuinielaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isBadRequest());

        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuinielas() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        // Get all the quinielaList
        restQuinielaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quiniela.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].costoParticipacion").value(hasItem(DEFAULT_COSTO_PARTICIPACION.doubleValue())))
            .andExpect(jsonPath("$.[*].primerPremio").value(hasItem(DEFAULT_PRIMER_PREMIO.doubleValue())))
            .andExpect(jsonPath("$.[*].segundoPremio").value(hasItem(DEFAULT_SEGUNDO_PREMIO.doubleValue())))
            .andExpect(jsonPath("$.[*].tercerPremio").value(hasItem(DEFAULT_TERCER_PREMIO.doubleValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getQuiniela() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        // Get the quiniela
        restQuinielaMockMvc
            .perform(get(ENTITY_API_URL_ID, quiniela.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quiniela.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.costoParticipacion").value(DEFAULT_COSTO_PARTICIPACION.doubleValue()))
            .andExpect(jsonPath("$.primerPremio").value(DEFAULT_PRIMER_PREMIO.doubleValue()))
            .andExpect(jsonPath("$.segundoPremio").value(DEFAULT_SEGUNDO_PREMIO.doubleValue()))
            .andExpect(jsonPath("$.tercerPremio").value(DEFAULT_TERCER_PREMIO.doubleValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingQuiniela() throws Exception {
        // Get the quiniela
        restQuinielaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuiniela() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();

        // Update the quiniela
        Quiniela updatedQuiniela = quinielaRepository.findById(quiniela.getId()).get();
        // Disconnect from session so that the updates on updatedQuiniela are not directly saved in db
        em.detach(updatedQuiniela);
        updatedQuiniela
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .costoParticipacion(UPDATED_COSTO_PARTICIPACION)
            .primerPremio(UPDATED_PRIMER_PREMIO)
            .segundoPremio(UPDATED_SEGUNDO_PREMIO)
            .tercerPremio(UPDATED_TERCER_PREMIO)
            .estado(UPDATED_ESTADO);

        restQuinielaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuiniela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuiniela))
            )
            .andExpect(status().isOk());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
        Quiniela testQuiniela = quinielaList.get(quinielaList.size() - 1);
        assertThat(testQuiniela.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testQuiniela.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testQuiniela.getCostoParticipacion()).isEqualTo(UPDATED_COSTO_PARTICIPACION);
        assertThat(testQuiniela.getPrimerPremio()).isEqualTo(UPDATED_PRIMER_PREMIO);
        assertThat(testQuiniela.getSegundoPremio()).isEqualTo(UPDATED_SEGUNDO_PREMIO);
        assertThat(testQuiniela.getTercerPremio()).isEqualTo(UPDATED_TERCER_PREMIO);
        assertThat(testQuiniela.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quiniela.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quiniela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quiniela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuinielaWithPatch() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();

        // Update the quiniela using partial update
        Quiniela partialUpdatedQuiniela = new Quiniela();
        partialUpdatedQuiniela.setId(quiniela.getId());

        partialUpdatedQuiniela
            .descripcion(UPDATED_DESCRIPCION)
            .costoParticipacion(UPDATED_COSTO_PARTICIPACION)
            .segundoPremio(UPDATED_SEGUNDO_PREMIO)
            .estado(UPDATED_ESTADO);

        restQuinielaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiniela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuiniela))
            )
            .andExpect(status().isOk());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
        Quiniela testQuiniela = quinielaList.get(quinielaList.size() - 1);
        assertThat(testQuiniela.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testQuiniela.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testQuiniela.getCostoParticipacion()).isEqualTo(UPDATED_COSTO_PARTICIPACION);
        assertThat(testQuiniela.getPrimerPremio()).isEqualTo(DEFAULT_PRIMER_PREMIO);
        assertThat(testQuiniela.getSegundoPremio()).isEqualTo(UPDATED_SEGUNDO_PREMIO);
        assertThat(testQuiniela.getTercerPremio()).isEqualTo(DEFAULT_TERCER_PREMIO);
        assertThat(testQuiniela.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateQuinielaWithPatch() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();

        // Update the quiniela using partial update
        Quiniela partialUpdatedQuiniela = new Quiniela();
        partialUpdatedQuiniela.setId(quiniela.getId());

        partialUpdatedQuiniela
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .costoParticipacion(UPDATED_COSTO_PARTICIPACION)
            .primerPremio(UPDATED_PRIMER_PREMIO)
            .segundoPremio(UPDATED_SEGUNDO_PREMIO)
            .tercerPremio(UPDATED_TERCER_PREMIO)
            .estado(UPDATED_ESTADO);

        restQuinielaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiniela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuiniela))
            )
            .andExpect(status().isOk());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
        Quiniela testQuiniela = quinielaList.get(quinielaList.size() - 1);
        assertThat(testQuiniela.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testQuiniela.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testQuiniela.getCostoParticipacion()).isEqualTo(UPDATED_COSTO_PARTICIPACION);
        assertThat(testQuiniela.getPrimerPremio()).isEqualTo(UPDATED_PRIMER_PREMIO);
        assertThat(testQuiniela.getSegundoPremio()).isEqualTo(UPDATED_SEGUNDO_PREMIO);
        assertThat(testQuiniela.getTercerPremio()).isEqualTo(UPDATED_TERCER_PREMIO);
        assertThat(testQuiniela.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quiniela.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quiniela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quiniela))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuiniela() throws Exception {
        int databaseSizeBeforeUpdate = quinielaRepository.findAll().size();
        quiniela.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuinielaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quiniela)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiniela in the database
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuiniela() throws Exception {
        // Initialize the database
        quinielaRepository.saveAndFlush(quiniela);

        int databaseSizeBeforeDelete = quinielaRepository.findAll().size();

        // Delete the quiniela
        restQuinielaMockMvc
            .perform(delete(ENTITY_API_URL_ID, quiniela.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quiniela> quinielaList = quinielaRepository.findAll();
        assertThat(quinielaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
