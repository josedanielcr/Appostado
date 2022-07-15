package cr.ac.cenfotec.appostado.web.rest;

import static cr.ac.cenfotec.appostado.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cenfotec.appostado.IntegrationTest;
import cr.ac.cenfotec.appostado.domain.Evento;
import cr.ac.cenfotec.appostado.repository.EventoRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link EventoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventoResourceIT {

    private static final Long DEFAULT_ID_DEPORTE = 1L;
    private static final Long UPDATED_ID_DEPORTE = 2L;

    private static final Long DEFAULT_ID_DIVISION = 1L;
    private static final Long UPDATED_ID_DIVISION = 2L;

    private static final Long DEFAULT_ID_COMPETIDOR_1 = 1L;
    private static final Long UPDATED_ID_COMPETIDOR_1 = 2L;

    private static final Long DEFAULT_ID_COMPETIDOR_2 = 1L;
    private static final Long UPDATED_ID_COMPETIDOR_2 = 2L;

    private static final Long DEFAULT_ID_QUINIELA = 1L;
    private static final Long UPDATED_ID_QUINIELA = 2L;

    private static final Long DEFAULT_ID_GANADOR = 1L;
    private static final Long UPDATED_ID_GANADOR = 2L;

    private static final Integer DEFAULT_MARCADOR_1 = 1;
    private static final Integer UPDATED_MARCADOR_1 = 2;

    private static final Integer DEFAULT_MARCADOR_2 = 1;
    private static final Integer UPDATED_MARCADOR_2 = 2;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_MULTIPLICADOR = 1;
    private static final Integer UPDATED_MULTIPLICADOR = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_HORA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_HORA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HORA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/eventos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventoMockMvc;

    private Evento evento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createEntity(EntityManager em) {
        Evento evento = new Evento()
            .idDeporte(DEFAULT_ID_DEPORTE)
            .idDivision(DEFAULT_ID_DIVISION)
            .idCompetidor1(DEFAULT_ID_COMPETIDOR_1)
            .idCompetidor2(DEFAULT_ID_COMPETIDOR_2)
            .idQuiniela(DEFAULT_ID_QUINIELA)
            .idGanador(DEFAULT_ID_GANADOR)
            .marcador1(DEFAULT_MARCADOR_1)
            .marcador2(DEFAULT_MARCADOR_2)
            .estado(DEFAULT_ESTADO)
            .multiplicador(DEFAULT_MULTIPLICADOR)
            .fecha(DEFAULT_FECHA)
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFin(DEFAULT_HORA_FIN);
        return evento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evento createUpdatedEntity(EntityManager em) {
        Evento evento = new Evento()
            .idDeporte(UPDATED_ID_DEPORTE)
            .idDivision(UPDATED_ID_DIVISION)
            .idCompetidor1(UPDATED_ID_COMPETIDOR_1)
            .idCompetidor2(UPDATED_ID_COMPETIDOR_2)
            .idQuiniela(UPDATED_ID_QUINIELA)
            .idGanador(UPDATED_ID_GANADOR)
            .marcador1(UPDATED_MARCADOR_1)
            .marcador2(UPDATED_MARCADOR_2)
            .estado(UPDATED_ESTADO)
            .multiplicador(UPDATED_MULTIPLICADOR)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN);
        return evento;
    }

    @BeforeEach
    public void initTest() {
        evento = createEntity(em);
    }

    @Test
    @Transactional
    void createEvento() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();
        // Create the Evento
        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isCreated());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate + 1);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getIdDeporte()).isEqualTo(DEFAULT_ID_DEPORTE);
        assertThat(testEvento.getIdDivision()).isEqualTo(DEFAULT_ID_DIVISION);
        assertThat(testEvento.getIdCompetidor1()).isEqualTo(DEFAULT_ID_COMPETIDOR_1);
        assertThat(testEvento.getIdCompetidor2()).isEqualTo(DEFAULT_ID_COMPETIDOR_2);
        assertThat(testEvento.getIdQuiniela()).isEqualTo(DEFAULT_ID_QUINIELA);
        assertThat(testEvento.getIdGanador()).isEqualTo(DEFAULT_ID_GANADOR);
        assertThat(testEvento.getMarcador1()).isEqualTo(DEFAULT_MARCADOR_1);
        assertThat(testEvento.getMarcador2()).isEqualTo(DEFAULT_MARCADOR_2);
        assertThat(testEvento.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testEvento.getMultiplicador()).isEqualTo(DEFAULT_MULTIPLICADOR);
        assertThat(testEvento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEvento.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testEvento.getHoraFin()).isEqualTo(DEFAULT_HORA_FIN);
    }

    @Test
    @Transactional
    void createEventoWithExistingId() throws Exception {
        // Create the Evento with an existing ID
        evento.setId(1L);

        int databaseSizeBeforeCreate = eventoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdDeporteIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setIdDeporte(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdDivisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setIdDivision(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdCompetidor1IsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setIdCompetidor1(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdCompetidor2IsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setIdCompetidor2(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdGanadorIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setIdGanador(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarcador1IsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setMarcador1(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarcador2IsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setMarcador2(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setEstado(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMultiplicadorIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setMultiplicador(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setFecha(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setHoraInicio(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventoRepository.findAll().size();
        // set the field null
        evento.setHoraFin(null);

        // Create the Evento, which fails.

        restEventoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isBadRequest());

        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventos() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventoList
        restEventoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDeporte").value(hasItem(DEFAULT_ID_DEPORTE.intValue())))
            .andExpect(jsonPath("$.[*].idDivision").value(hasItem(DEFAULT_ID_DIVISION.intValue())))
            .andExpect(jsonPath("$.[*].idCompetidor1").value(hasItem(DEFAULT_ID_COMPETIDOR_1.intValue())))
            .andExpect(jsonPath("$.[*].idCompetidor2").value(hasItem(DEFAULT_ID_COMPETIDOR_2.intValue())))
            .andExpect(jsonPath("$.[*].idQuiniela").value(hasItem(DEFAULT_ID_QUINIELA.intValue())))
            .andExpect(jsonPath("$.[*].idGanador").value(hasItem(DEFAULT_ID_GANADOR.intValue())))
            .andExpect(jsonPath("$.[*].marcador1").value(hasItem(DEFAULT_MARCADOR_1)))
            .andExpect(jsonPath("$.[*].marcador2").value(hasItem(DEFAULT_MARCADOR_2)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].multiplicador").value(hasItem(DEFAULT_MULTIPLICADOR)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(sameInstant(DEFAULT_HORA_INICIO))))
            .andExpect(jsonPath("$.[*].horaFin").value(hasItem(sameInstant(DEFAULT_HORA_FIN))));
    }

    @Test
    @Transactional
    void getEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get the evento
        restEventoMockMvc
            .perform(get(ENTITY_API_URL_ID, evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evento.getId().intValue()))
            .andExpect(jsonPath("$.idDeporte").value(DEFAULT_ID_DEPORTE.intValue()))
            .andExpect(jsonPath("$.idDivision").value(DEFAULT_ID_DIVISION.intValue()))
            .andExpect(jsonPath("$.idCompetidor1").value(DEFAULT_ID_COMPETIDOR_1.intValue()))
            .andExpect(jsonPath("$.idCompetidor2").value(DEFAULT_ID_COMPETIDOR_2.intValue()))
            .andExpect(jsonPath("$.idQuiniela").value(DEFAULT_ID_QUINIELA.intValue()))
            .andExpect(jsonPath("$.idGanador").value(DEFAULT_ID_GANADOR.intValue()))
            .andExpect(jsonPath("$.marcador1").value(DEFAULT_MARCADOR_1))
            .andExpect(jsonPath("$.marcador2").value(DEFAULT_MARCADOR_2))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.multiplicador").value(DEFAULT_MULTIPLICADOR))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.horaInicio").value(sameInstant(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.horaFin").value(sameInstant(DEFAULT_HORA_FIN)));
    }

    @Test
    @Transactional
    void getNonExistingEvento() throws Exception {
        // Get the evento
        restEventoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento
        Evento updatedEvento = eventoRepository.findById(evento.getId()).get();
        // Disconnect from session so that the updates on updatedEvento are not directly saved in db
        em.detach(updatedEvento);
        updatedEvento
            .idDeporte(UPDATED_ID_DEPORTE)
            .idDivision(UPDATED_ID_DIVISION)
            .idCompetidor1(UPDATED_ID_COMPETIDOR_1)
            .idCompetidor2(UPDATED_ID_COMPETIDOR_2)
            .idQuiniela(UPDATED_ID_QUINIELA)
            .idGanador(UPDATED_ID_GANADOR)
            .marcador1(UPDATED_MARCADOR_1)
            .marcador2(UPDATED_MARCADOR_2)
            .estado(UPDATED_ESTADO)
            .multiplicador(UPDATED_MULTIPLICADOR)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN);

        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvento))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getIdDeporte()).isEqualTo(UPDATED_ID_DEPORTE);
        assertThat(testEvento.getIdDivision()).isEqualTo(UPDATED_ID_DIVISION);
        assertThat(testEvento.getIdCompetidor1()).isEqualTo(UPDATED_ID_COMPETIDOR_1);
        assertThat(testEvento.getIdCompetidor2()).isEqualTo(UPDATED_ID_COMPETIDOR_2);
        assertThat(testEvento.getIdQuiniela()).isEqualTo(UPDATED_ID_QUINIELA);
        assertThat(testEvento.getIdGanador()).isEqualTo(UPDATED_ID_GANADOR);
        assertThat(testEvento.getMarcador1()).isEqualTo(UPDATED_MARCADOR_1);
        assertThat(testEvento.getMarcador2()).isEqualTo(UPDATED_MARCADOR_2);
        assertThat(testEvento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEvento.getMultiplicador()).isEqualTo(UPDATED_MULTIPLICADOR);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testEvento.getHoraFin()).isEqualTo(UPDATED_HORA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento
            .idDivision(UPDATED_ID_DIVISION)
            .idCompetidor1(UPDATED_ID_COMPETIDOR_1)
            .idGanador(UPDATED_ID_GANADOR)
            .marcador2(UPDATED_MARCADOR_2)
            .estado(UPDATED_ESTADO)
            .multiplicador(UPDATED_MULTIPLICADOR)
            .fecha(UPDATED_FECHA);

        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvento))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getIdDeporte()).isEqualTo(DEFAULT_ID_DEPORTE);
        assertThat(testEvento.getIdDivision()).isEqualTo(UPDATED_ID_DIVISION);
        assertThat(testEvento.getIdCompetidor1()).isEqualTo(UPDATED_ID_COMPETIDOR_1);
        assertThat(testEvento.getIdCompetidor2()).isEqualTo(DEFAULT_ID_COMPETIDOR_2);
        assertThat(testEvento.getIdQuiniela()).isEqualTo(DEFAULT_ID_QUINIELA);
        assertThat(testEvento.getIdGanador()).isEqualTo(UPDATED_ID_GANADOR);
        assertThat(testEvento.getMarcador1()).isEqualTo(DEFAULT_MARCADOR_1);
        assertThat(testEvento.getMarcador2()).isEqualTo(UPDATED_MARCADOR_2);
        assertThat(testEvento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEvento.getMultiplicador()).isEqualTo(UPDATED_MULTIPLICADOR);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHoraInicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testEvento.getHoraFin()).isEqualTo(DEFAULT_HORA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateEventoWithPatch() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento using partial update
        Evento partialUpdatedEvento = new Evento();
        partialUpdatedEvento.setId(evento.getId());

        partialUpdatedEvento
            .idDeporte(UPDATED_ID_DEPORTE)
            .idDivision(UPDATED_ID_DIVISION)
            .idCompetidor1(UPDATED_ID_COMPETIDOR_1)
            .idCompetidor2(UPDATED_ID_COMPETIDOR_2)
            .idQuiniela(UPDATED_ID_QUINIELA)
            .idGanador(UPDATED_ID_GANADOR)
            .marcador1(UPDATED_MARCADOR_1)
            .marcador2(UPDATED_MARCADOR_2)
            .estado(UPDATED_ESTADO)
            .multiplicador(UPDATED_MULTIPLICADOR)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFin(UPDATED_HORA_FIN);

        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvento))
            )
            .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventoList.get(eventoList.size() - 1);
        assertThat(testEvento.getIdDeporte()).isEqualTo(UPDATED_ID_DEPORTE);
        assertThat(testEvento.getIdDivision()).isEqualTo(UPDATED_ID_DIVISION);
        assertThat(testEvento.getIdCompetidor1()).isEqualTo(UPDATED_ID_COMPETIDOR_1);
        assertThat(testEvento.getIdCompetidor2()).isEqualTo(UPDATED_ID_COMPETIDOR_2);
        assertThat(testEvento.getIdQuiniela()).isEqualTo(UPDATED_ID_QUINIELA);
        assertThat(testEvento.getIdGanador()).isEqualTo(UPDATED_ID_GANADOR);
        assertThat(testEvento.getMarcador1()).isEqualTo(UPDATED_MARCADOR_1);
        assertThat(testEvento.getMarcador2()).isEqualTo(UPDATED_MARCADOR_2);
        assertThat(testEvento.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEvento.getMultiplicador()).isEqualTo(UPDATED_MULTIPLICADOR);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getHoraInicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testEvento.getHoraFin()).isEqualTo(UPDATED_HORA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvento() throws Exception {
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();
        evento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evento in the database
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        int databaseSizeBeforeDelete = eventoRepository.findAll().size();

        // Delete the evento
        restEventoMockMvc
            .perform(delete(ENTITY_API_URL_ID, evento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evento> eventoList = eventoRepository.findAll();
        assertThat(eventoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
