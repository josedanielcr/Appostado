package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Mision;
import cr.ac.cenfotec.appostado.repository.MisionRepository;
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
 * Integration tests for the {@link MisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MisionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_BONO_CREDITOS = 1F;
    private static final Float UPDATED_BONO_CREDITOS = 2F;

    private static final String DEFAULT_DIA = "AAAAAAAAAA";
    private static final String UPDATED_DIA = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_OPCION_1 = "AAAAAAAAAA";
    private static final String UPDATED_OPCION_1 = "BBBBBBBBBB";

    private static final String DEFAULT_OPCION_2 = "AAAAAAAAAA";
    private static final String UPDATED_OPCION_2 = "BBBBBBBBBB";

    private static final String DEFAULT_OPCION_3 = "AAAAAAAAAA";
    private static final String UPDATED_OPCION_3 = "BBBBBBBBBB";

    private static final String DEFAULT_OPCION_4 = "AAAAAAAAAA";
    private static final String UPDATED_OPCION_4 = "BBBBBBBBBB";

    private static final String DEFAULT_ENLACE = "AAAAAAAAAA";
    private static final String UPDATED_ENLACE = "BBBBBBBBBB";

    private static final Integer DEFAULT_OPCION_CORRECTA = 1;
    private static final Integer UPDATED_OPCION_CORRECTA = 2;

    private static final String ENTITY_API_URL = "/api/misions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMisionMockMvc;

    private Mision mision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mision createEntity(EntityManager em) {
        Mision mision = new Mision()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .bonoCreditos(DEFAULT_BONO_CREDITOS)
            .dia(DEFAULT_DIA)
            .tipo(DEFAULT_TIPO)
            .opcion1(DEFAULT_OPCION_1)
            .opcion2(DEFAULT_OPCION_2)
            .opcion3(DEFAULT_OPCION_3)
            .opcion4(DEFAULT_OPCION_4)
            .enlace(DEFAULT_ENLACE)
            .opcionCorrecta(DEFAULT_OPCION_CORRECTA);
        return mision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mision createUpdatedEntity(EntityManager em) {
        Mision mision = new Mision()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .bonoCreditos(UPDATED_BONO_CREDITOS)
            .dia(UPDATED_DIA)
            .tipo(UPDATED_TIPO)
            .opcion1(UPDATED_OPCION_1)
            .opcion2(UPDATED_OPCION_2)
            .opcion3(UPDATED_OPCION_3)
            .opcion4(UPDATED_OPCION_4)
            .enlace(UPDATED_ENLACE)
            .opcionCorrecta(UPDATED_OPCION_CORRECTA);
        return mision;
    }

    @BeforeEach
    public void initTest() {
        mision = createEntity(em);
    }

    @Test
    @Transactional
    void createMision() throws Exception {
        int databaseSizeBeforeCreate = misionRepository.findAll().size();
        // Create the Mision
        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isCreated());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeCreate + 1);
        Mision testMision = misionList.get(misionList.size() - 1);
        assertThat(testMision.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMision.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMision.getBonoCreditos()).isEqualTo(DEFAULT_BONO_CREDITOS);
        assertThat(testMision.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testMision.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testMision.getOpcion1()).isEqualTo(DEFAULT_OPCION_1);
        assertThat(testMision.getOpcion2()).isEqualTo(DEFAULT_OPCION_2);
        assertThat(testMision.getOpcion3()).isEqualTo(DEFAULT_OPCION_3);
        assertThat(testMision.getOpcion4()).isEqualTo(DEFAULT_OPCION_4);
        assertThat(testMision.getEnlace()).isEqualTo(DEFAULT_ENLACE);
        assertThat(testMision.getOpcionCorrecta()).isEqualTo(DEFAULT_OPCION_CORRECTA);
    }

    @Test
    @Transactional
    void createMisionWithExistingId() throws Exception {
        // Create the Mision with an existing ID
        mision.setId(1L);

        int databaseSizeBeforeCreate = misionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionRepository.findAll().size();
        // set the field null
        mision.setNombre(null);

        // Create the Mision, which fails.

        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionRepository.findAll().size();
        // set the field null
        mision.setDescripcion(null);

        // Create the Mision, which fails.

        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBonoCreditosIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionRepository.findAll().size();
        // set the field null
        mision.setBonoCreditos(null);

        // Create the Mision, which fails.

        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionRepository.findAll().size();
        // set the field null
        mision.setDia(null);

        // Create the Mision, which fails.

        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionRepository.findAll().size();
        // set the field null
        mision.setTipo(null);

        // Create the Mision, which fails.

        restMisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isBadRequest());

        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMisions() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        // Get all the misionList
        restMisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].bonoCreditos").value(hasItem(DEFAULT_BONO_CREDITOS.doubleValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].opcion1").value(hasItem(DEFAULT_OPCION_1)))
            .andExpect(jsonPath("$.[*].opcion2").value(hasItem(DEFAULT_OPCION_2)))
            .andExpect(jsonPath("$.[*].opcion3").value(hasItem(DEFAULT_OPCION_3)))
            .andExpect(jsonPath("$.[*].opcion4").value(hasItem(DEFAULT_OPCION_4)))
            .andExpect(jsonPath("$.[*].enlace").value(hasItem(DEFAULT_ENLACE)))
            .andExpect(jsonPath("$.[*].opcionCorrecta").value(hasItem(DEFAULT_OPCION_CORRECTA)));
    }

    @Test
    @Transactional
    void getMision() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        // Get the mision
        restMisionMockMvc
            .perform(get(ENTITY_API_URL_ID, mision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mision.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.bonoCreditos").value(DEFAULT_BONO_CREDITOS.doubleValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.opcion1").value(DEFAULT_OPCION_1))
            .andExpect(jsonPath("$.opcion2").value(DEFAULT_OPCION_2))
            .andExpect(jsonPath("$.opcion3").value(DEFAULT_OPCION_3))
            .andExpect(jsonPath("$.opcion4").value(DEFAULT_OPCION_4))
            .andExpect(jsonPath("$.enlace").value(DEFAULT_ENLACE))
            .andExpect(jsonPath("$.opcionCorrecta").value(DEFAULT_OPCION_CORRECTA));
    }

    @Test
    @Transactional
    void getNonExistingMision() throws Exception {
        // Get the mision
        restMisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMision() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        int databaseSizeBeforeUpdate = misionRepository.findAll().size();

        // Update the mision
        Mision updatedMision = misionRepository.findById(mision.getId()).get();
        // Disconnect from session so that the updates on updatedMision are not directly saved in db
        em.detach(updatedMision);
        updatedMision
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .bonoCreditos(UPDATED_BONO_CREDITOS)
            .dia(UPDATED_DIA)
            .tipo(UPDATED_TIPO)
            .opcion1(UPDATED_OPCION_1)
            .opcion2(UPDATED_OPCION_2)
            .opcion3(UPDATED_OPCION_3)
            .opcion4(UPDATED_OPCION_4)
            .enlace(UPDATED_ENLACE)
            .opcionCorrecta(UPDATED_OPCION_CORRECTA);

        restMisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMision))
            )
            .andExpect(status().isOk());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
        Mision testMision = misionList.get(misionList.size() - 1);
        assertThat(testMision.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMision.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMision.getBonoCreditos()).isEqualTo(UPDATED_BONO_CREDITOS);
        assertThat(testMision.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testMision.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testMision.getOpcion1()).isEqualTo(UPDATED_OPCION_1);
        assertThat(testMision.getOpcion2()).isEqualTo(UPDATED_OPCION_2);
        assertThat(testMision.getOpcion3()).isEqualTo(UPDATED_OPCION_3);
        assertThat(testMision.getOpcion4()).isEqualTo(UPDATED_OPCION_4);
        assertThat(testMision.getEnlace()).isEqualTo(UPDATED_ENLACE);
        assertThat(testMision.getOpcionCorrecta()).isEqualTo(UPDATED_OPCION_CORRECTA);
    }

    @Test
    @Transactional
    void putNonExistingMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMisionWithPatch() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        int databaseSizeBeforeUpdate = misionRepository.findAll().size();

        // Update the mision using partial update
        Mision partialUpdatedMision = new Mision();
        partialUpdatedMision.setId(mision.getId());

        partialUpdatedMision
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .dia(UPDATED_DIA)
            .tipo(UPDATED_TIPO)
            .opcion3(UPDATED_OPCION_3)
            .opcion4(UPDATED_OPCION_4)
            .opcionCorrecta(UPDATED_OPCION_CORRECTA);

        restMisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMision))
            )
            .andExpect(status().isOk());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
        Mision testMision = misionList.get(misionList.size() - 1);
        assertThat(testMision.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMision.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMision.getBonoCreditos()).isEqualTo(DEFAULT_BONO_CREDITOS);
        assertThat(testMision.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testMision.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testMision.getOpcion1()).isEqualTo(DEFAULT_OPCION_1);
        assertThat(testMision.getOpcion2()).isEqualTo(DEFAULT_OPCION_2);
        assertThat(testMision.getOpcion3()).isEqualTo(UPDATED_OPCION_3);
        assertThat(testMision.getOpcion4()).isEqualTo(UPDATED_OPCION_4);
        assertThat(testMision.getEnlace()).isEqualTo(DEFAULT_ENLACE);
        assertThat(testMision.getOpcionCorrecta()).isEqualTo(UPDATED_OPCION_CORRECTA);
    }

    @Test
    @Transactional
    void fullUpdateMisionWithPatch() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        int databaseSizeBeforeUpdate = misionRepository.findAll().size();

        // Update the mision using partial update
        Mision partialUpdatedMision = new Mision();
        partialUpdatedMision.setId(mision.getId());

        partialUpdatedMision
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .bonoCreditos(UPDATED_BONO_CREDITOS)
            .dia(UPDATED_DIA)
            .tipo(UPDATED_TIPO)
            .opcion1(UPDATED_OPCION_1)
            .opcion2(UPDATED_OPCION_2)
            .opcion3(UPDATED_OPCION_3)
            .opcion4(UPDATED_OPCION_4)
            .enlace(UPDATED_ENLACE)
            .opcionCorrecta(UPDATED_OPCION_CORRECTA);

        restMisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMision))
            )
            .andExpect(status().isOk());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
        Mision testMision = misionList.get(misionList.size() - 1);
        assertThat(testMision.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMision.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMision.getBonoCreditos()).isEqualTo(UPDATED_BONO_CREDITOS);
        assertThat(testMision.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testMision.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testMision.getOpcion1()).isEqualTo(UPDATED_OPCION_1);
        assertThat(testMision.getOpcion2()).isEqualTo(UPDATED_OPCION_2);
        assertThat(testMision.getOpcion3()).isEqualTo(UPDATED_OPCION_3);
        assertThat(testMision.getOpcion4()).isEqualTo(UPDATED_OPCION_4);
        assertThat(testMision.getEnlace()).isEqualTo(UPDATED_ENLACE);
        assertThat(testMision.getOpcionCorrecta()).isEqualTo(UPDATED_OPCION_CORRECTA);
    }

    @Test
    @Transactional
    void patchNonExistingMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMision() throws Exception {
        int databaseSizeBeforeUpdate = misionRepository.findAll().size();
        mision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mision in the database
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMision() throws Exception {
        // Initialize the database
        misionRepository.saveAndFlush(mision);

        int databaseSizeBeforeDelete = misionRepository.findAll().size();

        // Delete the mision
        restMisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, mision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mision> misionList = misionRepository.findAll();
        assertThat(misionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
