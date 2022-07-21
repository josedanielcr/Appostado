package cr.ac.cenfotec.appostado.util;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.domain.Evento;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.EventoRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDeportivoUtil {

    private EventoRepository eventoRepository;

    /*
    private ApuestaRepository apuestaRepository;

    private CuentaUsuarioRepository cuentaUsuarioRepository;*/

    public EventoDeportivoUtil(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public void observarEventos() {
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
    /*
    public void devolverCreditosEventoCancelado(long id) {
        Evento eventoCancelado = this.eventoRepository.getById(id);
        List<Apuesta> todasLasApuestas = this.apuestaRepository.findAll();
        List<Apuesta> apuestasDelEventoDeportivo = new ArrayList<>();

        for (int i = 0; i < todasLasApuestas.size(); i++) {
            if (todasLasApuestas.get(i).getEvento().getId() == eventoCancelado.getId()) {
                apuestasDelEventoDeportivo.add(todasLasApuestas.get(i));
            }
        }

        for(int i = 0;i< apuestasDelEventoDeportivo.size();i++){
            CuentaUsuario user = cuentaUsuarioRepository.getById();
            float creditoDevolver = apuestasDelEventoDeportivo.get(i).getCreditosApostados();
            user.setBalance(user.getBalance()+creditoDevolver);
        }
    }*/
}
