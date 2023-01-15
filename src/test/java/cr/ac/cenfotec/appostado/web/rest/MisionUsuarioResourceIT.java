package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.MisionUsuario;
import cr.ac.cenfotec.appostado.repository.MisionUsuarioRepository;
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
 * Integration tests for the {@link MisionUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MisionUsuarioResourceIT {

    private static final Boolean DEFAULT_COMPLETADO = false;
    private static final Boolean UPDATED_COMPLETADO = true;

    private static final String ENTITY_API_URL = "/api/mision-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MisionUsuarioRepository misionUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMisionUsuarioMockMvc;

    private MisionUsuario misionUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisionUsuario createEntity(EntityManager em) {
        MisionUsuario misionUsuario = new MisionUsuario().completado(DEFAULT_COMPLETADO);
        return misionUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MisionUsuario createUpdatedEntity(EntityManager em) {
        MisionUsuario misionUsuario = new MisionUsuario().completado(UPDATED_COMPLETADO);
        return misionUsuario;
    }

    @BeforeEach
    public void initTest() {
        misionUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createMisionUsuario() throws Exception {
        int databaseSizeBeforeCreate = misionUsuarioRepository.findAll().size();
        // Create the MisionUsuario
        restMisionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionUsuario)))
            .andExpect(status().isCreated());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        MisionUsuario testMisionUsuario = misionUsuarioList.get(misionUsuarioList.size() - 1);
        assertThat(testMisionUsuario.getCompletado()).isEqualTo(DEFAULT_COMPLETADO);
    }

    @Test
    @Transactional
    void createMisionUsuarioWithExistingId() throws Exception {
        // Create the MisionUsuario with an existing ID
        misionUsuario.setId(1L);

        int databaseSizeBeforeCreate = misionUsuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMisionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompletadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = misionUsuarioRepository.findAll().size();
        // set the field null
        misionUsuario.setCompletado(null);

        // Create the MisionUsuario, which fails.

        restMisionUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionUsuario)))
            .andExpect(status().isBadRequest());

        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMisionUsuarios() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        // Get all the misionUsuarioList
        restMisionUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(misionUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].completado").value(hasItem(DEFAULT_COMPLETADO.booleanValue())));
    }

    @Test
    @Transactional
    void getMisionUsuario() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        // Get the misionUsuario
        restMisionUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, misionUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(misionUsuario.getId().intValue()))
            .andExpect(jsonPath("$.completado").value(DEFAULT_COMPLETADO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMisionUsuario() throws Exception {
        // Get the misionUsuario
        restMisionUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMisionUsuario() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();

        // Update the misionUsuario
        MisionUsuario updatedMisionUsuario = misionUsuarioRepository.findById(misionUsuario.getId()).get();
        // Disconnect from session so that the updates on updatedMisionUsuario are not directly saved in db
        em.detach(updatedMisionUsuario);
        updatedMisionUsuario.completado(UPDATED_COMPLETADO);

        restMisionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMisionUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMisionUsuario))
            )
            .andExpect(status().isOk());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
        MisionUsuario testMisionUsuario = misionUsuarioList.get(misionUsuarioList.size() - 1);
        assertThat(testMisionUsuario.getCompletado()).isEqualTo(UPDATED_COMPLETADO);
    }

    @Test
    @Transactional
    void putNonExistingMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, misionUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misionUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(misionUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(misionUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMisionUsuarioWithPatch() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();

        // Update the misionUsuario using partial update
        MisionUsuario partialUpdatedMisionUsuario = new MisionUsuario();
        partialUpdatedMisionUsuario.setId(misionUsuario.getId());

        partialUpdatedMisionUsuario.completado(UPDATED_COMPLETADO);

        restMisionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisionUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisionUsuario))
            )
            .andExpect(status().isOk());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
        MisionUsuario testMisionUsuario = misionUsuarioList.get(misionUsuarioList.size() - 1);
        assertThat(testMisionUsuario.getCompletado()).isEqualTo(UPDATED_COMPLETADO);
    }

    @Test
    @Transactional
    void fullUpdateMisionUsuarioWithPatch() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();

        // Update the misionUsuario using partial update
        MisionUsuario partialUpdatedMisionUsuario = new MisionUsuario();
        partialUpdatedMisionUsuario.setId(misionUsuario.getId());

        partialUpdatedMisionUsuario.completado(UPDATED_COMPLETADO);

        restMisionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMisionUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMisionUsuario))
            )
            .andExpect(status().isOk());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
        MisionUsuario testMisionUsuario = misionUsuarioList.get(misionUsuarioList.size() - 1);
        assertThat(testMisionUsuario.getCompletado()).isEqualTo(UPDATED_COMPLETADO);
    }

    @Test
    @Transactional
    void patchNonExistingMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, misionUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misionUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(misionUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMisionUsuario() throws Exception {
        int databaseSizeBeforeUpdate = misionUsuarioRepository.findAll().size();
        misionUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMisionUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(misionUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MisionUsuario in the database
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMisionUsuario() throws Exception {
        // Initialize the database
        misionUsuarioRepository.saveAndFlush(misionUsuario);

        int databaseSizeBeforeDelete = misionUsuarioRepository.findAll().size();

        // Delete the misionUsuario
        restMisionUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, misionUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MisionUsuario> misionUsuarioList = misionUsuarioRepository.findAll();
        assertThat(misionUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
