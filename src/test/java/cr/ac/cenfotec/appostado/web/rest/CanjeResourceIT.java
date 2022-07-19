package cr.ac.cenfotec.appostado.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Canje;
import cr.ac.cenfotec.appostado.repository.CanjeRepository;
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
 * Integration tests for the {@link CanjeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanjeResourceIT {

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_DETALLE = "AAAAAAAAAA";
    private static final String UPDATED_DETALLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/canjes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CanjeRepository canjeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanjeMockMvc;

    private Canje canje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Canje createEntity(EntityManager em) {
        Canje canje = new Canje().estado(DEFAULT_ESTADO).detalle(DEFAULT_DETALLE);
        return canje;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Canje createUpdatedEntity(EntityManager em) {
        Canje canje = new Canje().estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);
        return canje;
    }

    @BeforeEach
    public void initTest() {
        canje = createEntity(em);
    }

    @Test
    @Transactional
    void createCanje() throws Exception {
        int databaseSizeBeforeCreate = canjeRepository.findAll().size();
        // Create the Canje
        restCanjeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(canje)))
            .andExpect(status().isCreated());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeCreate + 1);
        Canje testCanje = canjeList.get(canjeList.size() - 1);
        assertThat(testCanje.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCanje.getDetalle()).isEqualTo(DEFAULT_DETALLE);
    }

    @Test
    @Transactional
    void createCanjeWithExistingId() throws Exception {
        // Create the Canje with an existing ID
        canje.setId(1L);

        int databaseSizeBeforeCreate = canjeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanjeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(canje)))
            .andExpect(status().isBadRequest());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCanjes() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        // Get all the canjeList
        restCanjeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canje.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE)));
    }

    @Test
    @Transactional
    void getCanje() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        // Get the canje
        restCanjeMockMvc
            .perform(get(ENTITY_API_URL_ID, canje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canje.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE));
    }

    @Test
    @Transactional
    void getNonExistingCanje() throws Exception {
        // Get the canje
        restCanjeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCanje() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();

        // Update the canje
        Canje updatedCanje = canjeRepository.findById(canje.getId()).get();
        // Disconnect from session so that the updates on updatedCanje are not directly saved in db
        em.detach(updatedCanje);
        updatedCanje.estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);

        restCanjeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCanje.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCanje))
            )
            .andExpect(status().isOk());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
        Canje testCanje = canjeList.get(canjeList.size() - 1);
        assertThat(testCanje.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCanje.getDetalle()).isEqualTo(UPDATED_DETALLE);
    }

    @Test
    @Transactional
    void putNonExistingCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canje.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(canje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(canje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(canje)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanjeWithPatch() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();

        // Update the canje using partial update
        Canje partialUpdatedCanje = new Canje();
        partialUpdatedCanje.setId(canje.getId());

        restCanjeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCanje))
            )
            .andExpect(status().isOk());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
        Canje testCanje = canjeList.get(canjeList.size() - 1);
        assertThat(testCanje.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCanje.getDetalle()).isEqualTo(DEFAULT_DETALLE);
    }

    @Test
    @Transactional
    void fullUpdateCanjeWithPatch() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();

        // Update the canje using partial update
        Canje partialUpdatedCanje = new Canje();
        partialUpdatedCanje.setId(canje.getId());

        partialUpdatedCanje.estado(UPDATED_ESTADO).detalle(UPDATED_DETALLE);

        restCanjeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCanje))
            )
            .andExpect(status().isOk());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
        Canje testCanje = canjeList.get(canjeList.size() - 1);
        assertThat(testCanje.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCanje.getDetalle()).isEqualTo(UPDATED_DETALLE);
    }

    @Test
    @Transactional
    void patchNonExistingCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(canje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(canje))
            )
            .andExpect(status().isBadRequest());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanje() throws Exception {
        int databaseSizeBeforeUpdate = canjeRepository.findAll().size();
        canje.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanjeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(canje)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Canje in the database
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanje() throws Exception {
        // Initialize the database
        canjeRepository.saveAndFlush(canje);

        int databaseSizeBeforeDelete = canjeRepository.findAll().size();

        // Delete the canje
        restCanjeMockMvc
            .perform(delete(ENTITY_API_URL_ID, canje.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Canje> canjeList = canjeRepository.findAll();
        assertThat(canjeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
