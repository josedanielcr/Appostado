package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Transaccion;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TransaccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransaccionResourceIT {

    private static final Long DEFAULT_ID_CUENTA = 1L;
    private static final Long UPDATED_ID_CUENTA = 2L;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_MONTO = 1F;
    private static final Float UPDATED_MONTO = 2F;

    private static final String ENTITY_API_URL = "/api/transaccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransaccionMockMvc;

    private Transaccion transaccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaccion createEntity(EntityManager em) {
        Transaccion transaccion = new Transaccion()
            .idCuenta(DEFAULT_ID_CUENTA)
            .fecha(DEFAULT_FECHA)
            .tipo(DEFAULT_TIPO)
            .descripcion(DEFAULT_DESCRIPCION)
            .monto(DEFAULT_MONTO);
        return transaccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaccion createUpdatedEntity(EntityManager em) {
        Transaccion transaccion = new Transaccion()
            .idCuenta(UPDATED_ID_CUENTA)
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO);
        return transaccion;
    }

    @BeforeEach
    public void initTest() {
        transaccion = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaccion() throws Exception {
        int databaseSizeBeforeCreate = transaccionRepository.findAll().size();
        // Create the Transaccion
        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isCreated());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getIdCuenta()).isEqualTo(DEFAULT_ID_CUENTA);
        assertThat(testTransaccion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTransaccion.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTransaccion.getMonto()).isEqualTo(DEFAULT_MONTO);
    }

    @Test
    @Transactional
    void createTransaccionWithExistingId() throws Exception {
        // Create the Transaccion with an existing ID
        transaccion.setId(1L);

        int databaseSizeBeforeCreate = transaccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdCuentaIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setIdCuenta(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setFecha(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setTipo(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setDescripcion(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setMonto(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransaccions() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get all the transaccionList
        restTransaccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCuenta").value(hasItem(DEFAULT_ID_CUENTA.intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.doubleValue())));
    }

    @Test
    @Transactional
    void getTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get the transaccion
        restTransaccionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaccion.getId().intValue()))
            .andExpect(jsonPath("$.idCuenta").value(DEFAULT_ID_CUENTA.intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTransaccion() throws Exception {
        // Get the transaccion
        restTransaccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion
        Transaccion updatedTransaccion = transaccionRepository.findById(transaccion.getId()).get();
        // Disconnect from session so that the updates on updatedTransaccion are not directly saved in db
        em.detach(updatedTransaccion);
        updatedTransaccion
            .idCuenta(UPDATED_ID_CUENTA)
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO);

        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getIdCuenta()).isEqualTo(UPDATED_ID_CUENTA);
        assertThat(testTransaccion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTransaccion.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTransaccion.getMonto()).isEqualTo(UPDATED_MONTO);
    }

    @Test
    @Transactional
    void putNonExistingTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaccion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransaccionWithPatch() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion using partial update
        Transaccion partialUpdatedTransaccion = new Transaccion();
        partialUpdatedTransaccion.setId(transaccion.getId());

        partialUpdatedTransaccion.descripcion(UPDATED_DESCRIPCION).monto(UPDATED_MONTO);

        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getIdCuenta()).isEqualTo(DEFAULT_ID_CUENTA);
        assertThat(testTransaccion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTransaccion.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTransaccion.getMonto()).isEqualTo(UPDATED_MONTO);
    }

    @Test
    @Transactional
    void fullUpdateTransaccionWithPatch() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion using partial update
        Transaccion partialUpdatedTransaccion = new Transaccion();
        partialUpdatedTransaccion.setId(transaccion.getId());

        partialUpdatedTransaccion
            .idCuenta(UPDATED_ID_CUENTA)
            .fecha(UPDATED_FECHA)
            .tipo(UPDATED_TIPO)
            .descripcion(UPDATED_DESCRIPCION)
            .monto(UPDATED_MONTO);

        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getIdCuenta()).isEqualTo(UPDATED_ID_CUENTA);
        assertThat(testTransaccion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTransaccion.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTransaccion.getMonto()).isEqualTo(UPDATED_MONTO);
    }

    @Test
    @Transactional
    void patchNonExistingTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaccion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transaccion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeDelete = transaccionRepository.findAll().size();

        // Delete the transaccion
        restTransaccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
