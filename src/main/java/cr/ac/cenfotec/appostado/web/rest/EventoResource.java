package cr.ac.cenfotec.appostado.web.rest;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.domain.Competidor;
import cr.ac.cenfotec.appostado.domain.Evento;
import cr.ac.cenfotec.appostado.repository.CompetidorRepository;
import cr.ac.cenfotec.appostado.repository.EventoRepository;
import cr.ac.cenfotec.appostado.util.EventoDeportivoUtil;
import cr.ac.cenfotec.appostado.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cr.ac.cenfotec.appostado.domain.Evento}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EventoResource {

    private final Logger log = LoggerFactory.getLogger(EventoResource.class);

    private static final String ENTITY_NAME = "evento";

    EventoDeportivoUtil eventoDeportivoUtil;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventoRepository eventoRepository;

    private final CompetidorRepository competidorRepository;

    public EventoResource(EventoRepository eventoRepository, CompetidorRepository competidorRepository) {
        this.eventoRepository = eventoRepository;
        this.competidorRepository = competidorRepository;

        eventoDeportivoUtil = new EventoDeportivoUtil(this.eventoRepository);
    }

    /**
     * {@code POST  /eventos} : Create a new evento.
     *
     * @param evento the evento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evento, or with status {@code 400 (Bad Request)} if the evento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eventos")
    public ResponseEntity<Evento> createEvento(@Valid @RequestBody Evento evento) throws URISyntaxException {
        log.debug("REST request to save Evento : {}", evento);
        if (evento.getId() != null) {
            throw new BadRequestAlertException("A new evento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evento result = eventoRepository.save(evento);
        return ResponseEntity
            .created(new URI("/api/eventos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eventos/:id} : Updates an existing evento.
     *
     * @param id the id of the evento to save.
     * @param evento the evento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evento,
     * or with status {@code 400 (Bad Request)} if the evento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eventos/{id}")
    public ResponseEntity<Evento> updateEvento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Evento evento
    ) throws URISyntaxException {
        log.debug("REST request to update Evento : {}, {}", id, evento);
        if (evento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Evento result = eventoRepository.save(evento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evento.getId().toString()))
            .body(result);
    }

    @PutMapping("/eventos/cancelar/{id}")
    public ResponseEntity<Evento> cancelarEvento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody int evento
    ) throws URISyntaxException {
        Optional<Evento> e = eventoRepository.findById(id);
        e.get().setEstado("Cancelado");
        this.eventoRepository.save(e.get());

        /** ALGORITMO DE DEVOLVER CREDTIOS DE EVENTO CANCELADO*/

        // this.eventoDeportivoUtil.devolverCreditosEventoCancelado(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/eventos/resolver/{id}")
    public ResponseEntity<Evento> updateEventoResuelto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Evento evento
    ) throws URISyntaxException {
        if (evento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Evento eventoOriginal = this.eventoRepository.getById(id);
        eventoOriginal.setMarcador1(evento.getMarcador1());
        eventoOriginal.setMarcador2(evento.getMarcador2());
        eventoOriginal.setGanador(evento.getGanador());
        eventoOriginal.setEstado("Finalizado");

        /** ALGORITMO DE RESOLUCION DE APUESTAS*/

        Evento result = eventoRepository.save(eventoOriginal);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eventos/:id} : Partial updates given fields of an existing evento, field will ignore if it is null
     *
     * @param id the id of the evento to save.
     * @param evento the evento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evento,
     * or with status {@code 400 (Bad Request)} if the evento is not valid,
     * or with status {@code 404 (Not Found)} if the evento is not found,
     * or with status {@code 500 (Internal Server Error)} if the evento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eventos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Evento> partialUpdateEvento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Evento evento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evento partially : {}, {}", id, evento);
        if (evento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Evento> result = eventoRepository
            .findById(evento.getId())
            .map(existingEvento -> {
                if (evento.getMarcador1() != null) {
                    existingEvento.setMarcador1(evento.getMarcador1());
                }
                if (evento.getMarcador2() != null) {
                    existingEvento.setMarcador2(evento.getMarcador2());
                }
                if (evento.getEstado() != null) {
                    existingEvento.setEstado(evento.getEstado());
                }
                if (evento.getMultiplicador() != null) {
                    existingEvento.setMultiplicador(evento.getMultiplicador());
                }
                if (evento.getFecha() != null) {
                    existingEvento.setFecha(evento.getFecha());
                }
                if (evento.getHoraInicio() != null) {
                    existingEvento.setHoraInicio(evento.getHoraInicio());
                }
                if (evento.getHoraFin() != null) {
                    existingEvento.setHoraFin(evento.getHoraFin());
                }

                return existingEvento;
            })
            .map(eventoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evento.getId().toString())
        );
    }

    /**
     * {@code GET  /eventos} : get all the eventos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventos in body.
     */
    @GetMapping("/eventos")
    public List<Evento> getAllEventos() {
        this.eventoDeportivoUtil.observarEventos();
        log.debug("REST request to get all Eventos");
        return eventoRepository.findAll();
    }

    /**
     * {@code GET  /eventos/:id} : get the "id" evento.
     *
     * @param id the id of the evento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<Evento> getEvento(@PathVariable Long id) {
        log.debug("REST request to get Evento : {}", id);
        Optional<Evento> evento = eventoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(evento);
    }

    @GetMapping("/eventos/competidores/{id}")
    public List<Competidor> getCompetidoresEvento(@PathVariable Long id) {
        List<Competidor> listaCompetidores = new ArrayList<>();
        Evento evento = this.eventoRepository.getById(id);
        Competidor empate = new Competidor();
        empate.setNombre("Empate");
        empate.setId(Long.parseLong("0"));
        listaCompetidores.add(empate);
        listaCompetidores.add(evento.getCompetidor1());
        listaCompetidores.add(evento.getCompetidor2());

        return listaCompetidores;
    }

    /**
     * {@code DELETE  /eventos/:id} : delete the "id" evento.
     *
     * @param id the id of the evento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        log.debug("REST request to delete Evento : {}", id);
        eventoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET}
     * Obtiene eventos dependiendo de deporte, division y estado
     */
    @GetMapping("/eventos/{deporte}/{division}/{estado}")
    public List<Evento> getEventosByDeporteAndDivisionAndState(
        @PathVariable Long deporte,
        @PathVariable Long division,
        @PathVariable String estado
    ) {
        log.debug("REST request to get getEventosByDeporteOrDivision");
        if (deporte == -1) deporte = null;
        if (division == -1) division = null;
        if (estado.equals("empty")) estado = null;
        return eventoRepository.findEventoByDeporteAndDivisionAndEstado(deporte, division, estado);
    }
}
