package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.OpcionRol;
import cr.ac.cenfotec.appostado.repository.OpcionRolRepository;
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
 * Integration tests for the {@link OpcionRolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpcionRolResourceIT {

    private static final String DEFAULT_OPCION = "AAAAAAAAAA";
    private static final String UPDATED_OPCION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ICONO = "AAAAAAAAAA";
    private static final String UPDATED_ICONO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/opcion-rols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpcionRolRepository opcionRolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpcionRolMockMvc;

    private OpcionRol opcionRol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OpcionRol createEntity(EntityManager em) {
        OpcionRol opcionRol = new OpcionRol().opcion(DEFAULT_OPCION).path(DEFAULT_PATH).icono(DEFAULT_ICONO).nombre(DEFAULT_NOMBRE);
        return opcionRol;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OpcionRol createUpdatedEntity(EntityManager em) {
        OpcionRol opcionRol = new OpcionRol().opcion(UPDATED_OPCION).path(UPDATED_PATH).icono(UPDATED_ICONO).nombre(UPDATED_NOMBRE);
        return opcionRol;
    }

    @BeforeEach
    public void initTest() {
        opcionRol = createEntity(em);
    }

    @Test
    @Transactional
    void createOpcionRol() throws Exception {
        int databaseSizeBeforeCreate = opcionRolRepository.findAll().size();
        // Create the OpcionRol
        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isCreated());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeCreate + 1);
        OpcionRol testOpcionRol = opcionRolList.get(opcionRolList.size() - 1);
        assertThat(testOpcionRol.getOpcion()).isEqualTo(DEFAULT_OPCION);
        assertThat(testOpcionRol.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testOpcionRol.getIcono()).isEqualTo(DEFAULT_ICONO);
        assertThat(testOpcionRol.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createOpcionRolWithExistingId() throws Exception {
        // Create the OpcionRol with an existing ID
        opcionRol.setId(1L);

        int databaseSizeBeforeCreate = opcionRolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isBadRequest());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOpcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRolRepository.findAll().size();
        // set the field null
        opcionRol.setOpcion(null);

        // Create the OpcionRol, which fails.

        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isBadRequest());

        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRolRepository.findAll().size();
        // set the field null
        opcionRol.setPath(null);

        // Create the OpcionRol, which fails.

        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isBadRequest());

        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIconoIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRolRepository.findAll().size();
        // set the field null
        opcionRol.setIcono(null);

        // Create the OpcionRol, which fails.

        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isBadRequest());

        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = opcionRolRepository.findAll().size();
        // set the field null
        opcionRol.setNombre(null);

        // Create the OpcionRol, which fails.

        restOpcionRolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isBadRequest());

        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpcionRols() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        // Get all the opcionRolList
        restOpcionRolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opcionRol.getId().intValue())))
            .andExpect(jsonPath("$.[*].opcion").value(hasItem(DEFAULT_OPCION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].icono").value(hasItem(DEFAULT_ICONO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getOpcionRol() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        // Get the opcionRol
        restOpcionRolMockMvc
            .perform(get(ENTITY_API_URL_ID, opcionRol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opcionRol.getId().intValue()))
            .andExpect(jsonPath("$.opcion").value(DEFAULT_OPCION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.icono").value(DEFAULT_ICONO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingOpcionRol() throws Exception {
        // Get the opcionRol
        restOpcionRolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOpcionRol() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();

        // Update the opcionRol
        OpcionRol updatedOpcionRol = opcionRolRepository.findById(opcionRol.getId()).get();
        // Disconnect from session so that the updates on updatedOpcionRol are not directly saved in db
        em.detach(updatedOpcionRol);
        updatedOpcionRol.opcion(UPDATED_OPCION).path(UPDATED_PATH).icono(UPDATED_ICONO).nombre(UPDATED_NOMBRE);

        restOpcionRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOpcionRol.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOpcionRol))
            )
            .andExpect(status().isOk());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
        OpcionRol testOpcionRol = opcionRolList.get(opcionRolList.size() - 1);
        assertThat(testOpcionRol.getOpcion()).isEqualTo(UPDATED_OPCION);
        assertThat(testOpcionRol.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testOpcionRol.getIcono()).isEqualTo(UPDATED_ICONO);
        assertThat(testOpcionRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opcionRol.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionRol))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opcionRol))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(opcionRol)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpcionRolWithPatch() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();

        // Update the opcionRol using partial update
        OpcionRol partialUpdatedOpcionRol = new OpcionRol();
        partialUpdatedOpcionRol.setId(opcionRol.getId());

        partialUpdatedOpcionRol.path(UPDATED_PATH).icono(UPDATED_ICONO).nombre(UPDATED_NOMBRE);

        restOpcionRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcionRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcionRol))
            )
            .andExpect(status().isOk());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
        OpcionRol testOpcionRol = opcionRolList.get(opcionRolList.size() - 1);
        assertThat(testOpcionRol.getOpcion()).isEqualTo(DEFAULT_OPCION);
        assertThat(testOpcionRol.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testOpcionRol.getIcono()).isEqualTo(UPDATED_ICONO);
        assertThat(testOpcionRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateOpcionRolWithPatch() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();

        // Update the opcionRol using partial update
        OpcionRol partialUpdatedOpcionRol = new OpcionRol();
        partialUpdatedOpcionRol.setId(opcionRol.getId());

        partialUpdatedOpcionRol.opcion(UPDATED_OPCION).path(UPDATED_PATH).icono(UPDATED_ICONO).nombre(UPDATED_NOMBRE);

        restOpcionRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpcionRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpcionRol))
            )
            .andExpect(status().isOk());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
        OpcionRol testOpcionRol = opcionRolList.get(opcionRolList.size() - 1);
        assertThat(testOpcionRol.getOpcion()).isEqualTo(UPDATED_OPCION);
        assertThat(testOpcionRol.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testOpcionRol.getIcono()).isEqualTo(UPDATED_ICONO);
        assertThat(testOpcionRol.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opcionRol.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionRol))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opcionRol))
            )
            .andExpect(status().isBadRequest());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpcionRol() throws Exception {
        int databaseSizeBeforeUpdate = opcionRolRepository.findAll().size();
        opcionRol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpcionRolMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(opcionRol))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OpcionRol in the database
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpcionRol() throws Exception {
        // Initialize the database
        opcionRolRepository.saveAndFlush(opcionRol);

        int databaseSizeBeforeDelete = opcionRolRepository.findAll().size();

        // Delete the opcionRol
        restOpcionRolMockMvc
            .perform(delete(ENTITY_API_URL_ID, opcionRol.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OpcionRol> opcionRolList = opcionRolRepository.findAll();
        assertThat(opcionRolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
