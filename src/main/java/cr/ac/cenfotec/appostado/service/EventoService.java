package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.NotificacionRepository;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import cr.ac.cenfotec.appostado.web.rest.vm.EventCalculatedData;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing resolution of Events.
 */
@Service
@Transactional
public class EventoService {

    private final Logger log = LoggerFactory.getLogger(ApuestaService.class);

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    private final ApuestaRepository apuestaRepository;

    private final TransaccionRepository transaccionRepository;

    private final ApuestaService apuestaService;

    private final NotificacionRepository notificacionRepository;

    public EventoService(
        CuentaUsuarioRepository cuentaUsuarioRepository,
        ApuestaRepository apuestaRepository,
        TransaccionRepository transaccionRepository,
        NotificacionRepository notificacionRepository,
        ApuestaService apuestaService
    ) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.apuestaRepository = apuestaRepository;
        this.transaccionRepository = transaccionRepository;
        this.notificacionRepository = notificacionRepository;
        this.apuestaService = apuestaService;
    }

    public Evento resolveEventBets(Evento event, Evento eventUpdated) {
        // Se obtienen todas las apuestas realizadas en el evento
        List<Apuesta> apuestas = apuestaRepository.findAllByEvento(event);

        //Se actualiza la info del evento
        event.setMarcador1(eventUpdated.getMarcador1());
        event.setMarcador2(eventUpdated.getMarcador2());
        event.setGanador(eventUpdated.getGanador());
        event.setEstado("Finalizado");

        /** ALGORITMO DE RESOLUCIÓN DE APUESTAS **/

        CuentaUsuario cuenta;

        for (Apuesta a : apuestas) {
            EventCalculatedData data = apuestaService.generateEventData(event, a);

            // No hay un ganador en el evento = empate
            if (event.getGanador() == null) {
                if (a.getApostado() == null) {
                    // Se actualiza la información de la cuenta
                    cuenta = cuentaUsuarioRepository.findByUsuarioId(a.getUsuario().getId()).get();
                    cuenta.setBalance((float) (cuenta.getBalance() + data.getGanaciaEstimada()));
                    cuenta.setApuestasGanadas(cuenta.getApuestasGanadas() + 1);
                    cuentaUsuarioRepository.save(cuenta);

                    // Se crea una transacción de crédito
                    Transaccion transaccion = new Transaccion();
                    transaccion.setCuenta(cuenta);
                    transaccion.setMonto((float) data.getGanaciaEstimada());
                    transaccion.setTipo("Crédito");
                    transaccion.setFecha(LocalDate.now());
                    transaccion.setDescripcion(
                        "Apuesta Ganada. Evento de " +
                        event.getDeporte().getNombre() +
                        ": " +
                        event.getDivision().getNombre() +
                        " | " +
                        event.getCompetidor1().getNombre() +
                        " vs " +
                        event.getCompetidor2().getNombre()
                    );
                    transaccionRepository.save(transaccion);

                    // Se envía una notificación de ganador

                    Notificacion notificacion = new Notificacion();
                    notificacion.setGanancia((float) data.getGanaciaEstimada());
                    notificacion.setFecha(LocalDate.now());
                    notificacion.setTipo("Apuesta");
                    notificacion.setFueLeida(false);
                    notificacion.setUsuario(a.getUsuario());
                    notificacion.setHaGanado(true);
                    notificacion.setDescripcion("Ganaste Apuesta: " + event.getCompetidor1().getNombre() + " vs " + event.getCompetidor2());
                    notificacionRepository.save(notificacion);

                    // Se actualiza la apuesta
                    a.setEstado("Finalizado");
                    a.setHaGanado(true);
                } else {
                    // Enviar notificación de pérdida

                    Notificacion notificacion = new Notificacion();
                    notificacion.setGanancia((float) 0);
                    notificacion.setFecha(LocalDate.now());
                    notificacion.setTipo("Apuesta");
                    notificacion.setFueLeida(false);
                    notificacion.setUsuario(a.getUsuario());
                    notificacion.setHaGanado(false);
                    notificacion.setDescripcion(
                        "Perdiste Apuesta: " + event.getCompetidor1().getNombre() + " vs " + event.getCompetidor2()
                    );
                    notificacionRepository.save(notificacion);

                    // Se actualiza la apuesta
                    a.setEstado("Finalizado");
                    a.setHaGanado(false);
                }
            }
            // Se ingresa un ganador para el evento
            else {
                if (a.getApostado().getNombre() == event.getGanador().getNombre()) {
                    // Se actualiza la información de la cuenta
                    cuenta = cuentaUsuarioRepository.findByUsuarioId(a.getUsuario().getId()).get();
                    cuenta.setBalance((float) (cuenta.getBalance() + data.getGanaciaEstimada()));
                    cuenta.setApuestasGanadas(cuenta.getApuestasGanadas() + 1);
                    cuentaUsuarioRepository.save(cuenta);

                    // Se crea una transacción de crédito
                    Transaccion transaccion = new Transaccion();
                    transaccion.setCuenta(cuenta);
                    transaccion.setMonto((float) data.getGanaciaEstimada());
                    transaccion.setTipo("Crédito");
                    transaccion.setFecha(LocalDate.now());
                    transaccion.setDescripcion(
                        "Apuesta Ganada. Evento de " +
                        event.getDeporte().getNombre() +
                        ": " +
                        event.getDivision().getNombre() +
                        " | " +
                        event.getCompetidor1().getNombre() +
                        " vs " +
                        event.getCompetidor2().getNombre()
                    );
                    transaccionRepository.save(transaccion);

                    // Se envía una notificación de ganador

                    Notificacion notificacion = new Notificacion();
                    notificacion.setGanancia((float) data.getGanaciaEstimada());
                    notificacion.setFecha(LocalDate.now());
                    notificacion.setTipo("Apuesta");
                    notificacion.setFueLeida(false);
                    notificacion.setUsuario(a.getUsuario());
                    notificacion.setHaGanado(true);
                    notificacion.setDescripcion("Ganaste Apuesta: " + event.getCompetidor1().getNombre() + " vs " + event.getCompetidor2());
                    notificacionRepository.save(notificacion);

                    // Se actualiza la apuesta
                    a.setEstado("Finalizado");
                    a.setHaGanado(true);
                } else {
                    // Enviar notificación de pérdida

                    Notificacion notificacion = new Notificacion();
                    notificacion.setGanancia((float) 0);
                    notificacion.setFecha(LocalDate.now());
                    notificacion.setTipo("Apuesta");
                    notificacion.setFueLeida(false);
                    notificacion.setUsuario(a.getUsuario());
                    notificacion.setHaGanado(false);
                    notificacion.setDescripcion(
                        "Perdiste Apuesta: " + event.getCompetidor1().getNombre() + " vs " + event.getCompetidor2()
                    );
                    notificacionRepository.save(notificacion);

                    // Se actualiza la apuesta
                    a.setEstado("Finalizado");
                    a.setHaGanado(false);
                }
            }
            apuestaRepository.save(a);
        }

        return event;
    }
}
