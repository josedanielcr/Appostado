package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Amigo;
import cr.ac.cenfotec.appostado.repository.AmigoRepository;
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
 * Integration tests for the {@link AmigoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AmigoResourceIT {

    private static final Long DEFAULT_ID_USUARIO = 1L;
    private static final Long UPDATED_ID_USUARIO = 2L;

    private static final Long DEFAULT_ID_AMIGO = 1L;
    private static final Long UPDATED_ID_AMIGO = 2L;

    private static final String ENTITY_API_URL = "/api/amigos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmigoRepository amigoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmigoMockMvc;

    private Amigo amigo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amigo createEntity(EntityManager em) {
        Amigo amigo = new Amigo().idUsuario(DEFAULT_ID_USUARIO).idAmigo(DEFAULT_ID_AMIGO);
        return amigo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Amigo createUpdatedEntity(EntityManager em) {
        Amigo amigo = new Amigo().idUsuario(UPDATED_ID_USUARIO).idAmigo(UPDATED_ID_AMIGO);
        return amigo;
    }

    @BeforeEach
    public void initTest() {
        amigo = createEntity(em);
    }

    @Test
    @Transactional
    void createAmigo() throws Exception {
        int databaseSizeBeforeCreate = amigoRepository.findAll().size();
        // Create the Amigo
        restAmigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isCreated());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeCreate + 1);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testAmigo.getIdAmigo()).isEqualTo(DEFAULT_ID_AMIGO);
    }

    @Test
    @Transactional
    void createAmigoWithExistingId() throws Exception {
        // Create the Amigo with an existing ID
        amigo.setId(1L);

        int databaseSizeBeforeCreate = amigoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdUsuarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = amigoRepository.findAll().size();
        // set the field null
        amigo.setIdUsuario(null);

        // Create the Amigo, which fails.

        restAmigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isBadRequest());

        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdAmigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = amigoRepository.findAll().size();
        // set the field null
        amigo.setIdAmigo(null);

        // Create the Amigo, which fails.

        restAmigoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isBadRequest());

        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAmigos() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        // Get all the amigoList
        restAmigoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(amigo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUsuario").value(hasItem(DEFAULT_ID_USUARIO.intValue())))
            .andExpect(jsonPath("$.[*].idAmigo").value(hasItem(DEFAULT_ID_AMIGO.intValue())));
    }

    @Test
    @Transactional
    void getAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        // Get the amigo
        restAmigoMockMvc
            .perform(get(ENTITY_API_URL_ID, amigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(amigo.getId().intValue()))
            .andExpect(jsonPath("$.idUsuario").value(DEFAULT_ID_USUARIO.intValue()))
            .andExpect(jsonPath("$.idAmigo").value(DEFAULT_ID_AMIGO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAmigo() throws Exception {
        // Get the amigo
        restAmigoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();

        // Update the amigo
        Amigo updatedAmigo = amigoRepository.findById(amigo.getId()).get();
        // Disconnect from session so that the updates on updatedAmigo are not directly saved in db
        em.detach(updatedAmigo);
        updatedAmigo.idUsuario(UPDATED_ID_USUARIO).idAmigo(UPDATED_ID_AMIGO);

        restAmigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAmigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAmigo))
            )
            .andExpect(status().isOk());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testAmigo.getIdAmigo()).isEqualTo(UPDATED_ID_AMIGO);
    }

    @Test
    @Transactional
    void putNonExistingAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, amigo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(amigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAmigoWithPatch() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();

        // Update the amigo using partial update
        Amigo partialUpdatedAmigo = new Amigo();
        partialUpdatedAmigo.setId(amigo.getId());

        restAmigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmigo))
            )
            .andExpect(status().isOk());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getIdUsuario()).isEqualTo(DEFAULT_ID_USUARIO);
        assertThat(testAmigo.getIdAmigo()).isEqualTo(DEFAULT_ID_AMIGO);
    }

    @Test
    @Transactional
    void fullUpdateAmigoWithPatch() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();

        // Update the amigo using partial update
        Amigo partialUpdatedAmigo = new Amigo();
        partialUpdatedAmigo.setId(amigo.getId());

        partialUpdatedAmigo.idUsuario(UPDATED_ID_USUARIO).idAmigo(UPDATED_ID_AMIGO);

        restAmigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAmigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAmigo))
            )
            .andExpect(status().isOk());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
        Amigo testAmigo = amigoList.get(amigoList.size() - 1);
        assertThat(testAmigo.getIdUsuario()).isEqualTo(UPDATED_ID_USUARIO);
        assertThat(testAmigo.getIdAmigo()).isEqualTo(UPDATED_ID_AMIGO);
    }

    @Test
    @Transactional
    void patchNonExistingAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, amigo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(amigo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAmigo() throws Exception {
        int databaseSizeBeforeUpdate = amigoRepository.findAll().size();
        amigo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmigoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(amigo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Amigo in the database
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAmigo() throws Exception {
        // Initialize the database
        amigoRepository.saveAndFlush(amigo);

        int databaseSizeBeforeDelete = amigoRepository.findAll().size();

        // Delete the amigo
        restAmigoMockMvc
            .perform(delete(ENTITY_API_URL_ID, amigo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Amigo> amigoList = amigoRepository.findAll();
        assertThat(amigoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
