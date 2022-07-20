package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.Evento;
import cr.ac.cenfotec.appostado.repository.EventoRepository;
import java.time.ZonedDateTime;
import java.util.List;

public class EventoDeportivoService {

    private static EventoRepository eventoRepository;

    public EventoDeportivoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public static void observarEventos() {
        List<Evento> eventosDeportivos = eventoRepository.findAll();

        System.out.println("OBSERVANDO");
        ZonedDateTime fechaActual = ZonedDateTime.now();

        for (int i = 0; i < eventosDeportivos.size(); i++) {
            if (fechaActual.isAfter(eventosDeportivos.get(i).getHoraInicio()) && eventosDeportivos.get(i).getEstado().equals("Pendiente")) {
                eventosDeportivos.get(i).setEstado("En progreso");
                System.out.println("CAMBIO");
                eventoRepository.save(eventosDeportivos.get(i));
            }
        }
    }
}
