package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.LigaUsuario;
import cr.ac.cenfotec.appostado.repository.LigaUsuarioRepository;
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
 * Integration tests for the {@link LigaUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigaUsuarioResourceIT {

    private static final Long DEFAULT_ID_USUARIO = 1L;
    private static final Long UPDATED_ID_USUARIO = 2L;

    private static final Long DEFAULT_ID_LIGA = 1L;
    private static final Long UPDATED_ID_LIGA = 2L;

    private static final String ENTITY_API_URL = "/api/liga-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigaUsuarioRepository ligaUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigaUsuarioMockMvc;

    private LigaUsuario ligaUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigaUsuario createEntity(EntityManager em) {
        LigaUsuario ligaUsuario = new LigaUsuario().idUsuario(DEFAULT_ID_USUARIO).idLiga(DEFAULT_ID_LIGA);
        return ligaUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigaUsuario createUpdatedEntity(EntityManager em) {
        LigaUsuario ligaUsuario = new LigaUsuario().idUsuario(UPDATED_ID_USUARIO).idLiga(UPDATED_ID_LIGA);
        return ligaUsuario;
    }

    @BeforeEach
    public void initTest() {
        ligaUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createLigaUsuario() throws Exception {
        int databaseSizeBeforeCreate = ligaUsuarioRepository.findAll().size();
        // Create the LigaUsuario
        restLigaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligaUsuario)))
            .andExpect(status().isCreated());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeCreate + 1);
        LigaUsuario testLigaUsuario = ligaUsuarioList.get(ligaUsuarioList.size() - 1);
        assertThat(testLigaUsuario.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testLigaUsuario.getIdLiga()).isEqualTo(DEFAULT_ID_LIGA);
    }

    @Test
    @Transactional
    void createLigaUsuarioWithExistingId() throws Exception {
        // Create the LigaUsuario with an existing ID
        ligaUsuario.setId(1L);

        int databaseSizeBeforeCreate = ligaUsuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligaUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligaUsuarioRepository.findAll().size();
        // set the field null
        ligaUsuario.setIdUsuario(null);

        // Create the LigaUsuario, which fails.

        restLigaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligaUsuario)))
            .andExpect(status().isBadRequest());

        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdLigaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligaUsuarioRepository.findAll().size();
        // set the field null
        ligaUsuario.setIdLiga(null);

        // Create the LigaUsuario, which fails.

        restLigaUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligaUsuario)))
            .andExpect(status().isBadRequest());

        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLigaUsuarios() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        // Get all the ligaUsuarioList
        restLigaUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligaUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUsuario").value(hasItem(DEFAULT_ID_USUARIO.intValue())))
            .andExpect(jsonPath("$.[*].idLiga").value(hasItem(DEFAULT_ID_LIGA.intValue())));
    }

    @Test
    @Transactional
    void getLigaUsuario() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        // Get the ligaUsuario
        restLigaUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, ligaUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligaUsuario.getId().intValue()))
            .andExpect(jsonPath("$.idUsuario").value(DEFAULT_ID_USUARIO.intValue()))
            .andExpect(jsonPath("$.idLiga").value(DEFAULT_ID_LIGA.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLigaUsuario() throws Exception {
        // Get the ligaUsuario
        restLigaUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLigaUsuario() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();

        // Update the ligaUsuario
        LigaUsuario updatedLigaUsuario = ligaUsuarioRepository.findById(ligaUsuario.getId()).get();
        // Disconnect from session so that the updates on updatedLigaUsuario are not directly saved in db
        em.detach(updatedLigaUsuario);
        updatedLigaUsuario.idUsuario(UPDATED_ID_USUARIO).idLiga(UPDATED_ID_LIGA);

        restLigaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLigaUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLigaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        LigaUsuario testLigaUsuario = ligaUsuarioList.get(ligaUsuarioList.size() - 1);
        assertThat(testLigaUsuario.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLigaUsuario.getIdLiga()).isEqualTo(UPDATED_ID_LIGA);
    }

    @Test
    @Transactional
    void putNonExistingLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligaUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligaUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigaUsuarioWithPatch() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();

        // Update the ligaUsuario using partial update
        LigaUsuario partialUpdatedLigaUsuario = new LigaUsuario();
        partialUpdatedLigaUsuario.setId(ligaUsuario.getId());

        partialUpdatedLigaUsuario.idUsuario(UPDATED_ID_USUARIO).idLiga(UPDATED_ID_LIGA);

        restLigaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        LigaUsuario testLigaUsuario = ligaUsuarioList.get(ligaUsuarioList.size() - 1);
        assertThat(testLigaUsuario.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLigaUsuario.getIdLiga()).isEqualTo(UPDATED_ID_LIGA);
    }

    @Test
    @Transactional
    void fullUpdateLigaUsuarioWithPatch() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();

        // Update the ligaUsuario using partial update
        LigaUsuario partialUpdatedLigaUsuario = new LigaUsuario();
        partialUpdatedLigaUsuario.setId(ligaUsuario.getId());

        partialUpdatedLigaUsuario.idUsuario(UPDATED_ID_USUARIO).idLiga(UPDATED_ID_LIGA);

        restLigaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigaUsuario))
            )
            .andExpect(status().isOk());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
        LigaUsuario testLigaUsuario = ligaUsuarioList.get(ligaUsuarioList.size() - 1);
        assertThat(testLigaUsuario.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testLigaUsuario.getIdLiga()).isEqualTo(UPDATED_ID_LIGA);
    }

    @Test
    @Transactional
    void patchNonExistingLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligaUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligaUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigaUsuario() throws Exception {
        int databaseSizeBeforeUpdate = ligaUsuarioRepository.findAll().size();
        ligaUsuario.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigaUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ligaUsuario))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigaUsuario in the database
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigaUsuario() throws Exception {
        // Initialize the database
        ligaUsuarioRepository.saveAndFlush(ligaUsuario);

        int databaseSizeBeforeDelete = ligaUsuarioRepository.findAll().size();

        // Delete the ligaUsuario
        restLigaUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligaUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigaUsuario> ligaUsuarioList = ligaUsuarioRepository.findAll();
        assertThat(ligaUsuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
