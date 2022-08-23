package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.*;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.CompetidorRepository;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import cr.ac.cenfotec.appostado.web.rest.vm.EventCalculatedData;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApuestaService {

    private final Logger log = LoggerFactory.getLogger(ApuestaService.class);

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    private final ApuestaRepository apuestaRepository;

    private final TransaccionRepository transaccionRepository;

    private final CompetidorRepository competidorRepository;

    private final double BET_BASE = 1.0;

    public ApuestaService(
        CuentaUsuarioRepository cuentaUsuarioRepository,
        ApuestaRepository apuestaRepository,
        TransaccionRepository transaccionRepository,
        CompetidorRepository competidorRepository
    ) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.apuestaRepository = apuestaRepository;
        this.transaccionRepository = transaccionRepository;
        this.competidorRepository = competidorRepository;
    }

    public Apuesta createApuesta(Apuesta apuesta) throws Exception {
        try {
            CuentaUsuario cuentaUsuario = this.cuentaUsuarioRepository.findByUsuarioId(apuesta.getUsuario().getId()).get();
            if (cuentaUsuario.getBalance() < apuesta.getCreditosApostados()) {
                throw new Exception("Usuario no posee créditos suficientes para realizar la apuesta");
            }

            //verifica si es empate para poner el competidor de empate
            if (apuesta.getApostado() == null) {
                Optional<Competidor> competidorEmp = this.competidorRepository.findById(1L);
                if (competidorEmp.isEmpty()) {
                    throw new Exception("Error al crear evento de empate");
                }
                apuesta.setApostado(competidorEmp.get());
            }
            Apuesta apuestaRes = this.apuestaRepository.save(apuesta);

            //rebajo de creditos al usuario
            cuentaUsuario.setBalance(cuentaUsuario.getBalance() - apuesta.getCreditosApostados());
            cuentaUsuarioRepository.save(cuentaUsuario);

            //genera transaccion de tipo debido
            Transaccion trans = new Transaccion();
            trans.setCuenta(cuentaUsuario);
            trans.setDescripcion("Rebajo por apuesta " + apuesta.getId());
            trans.setFecha(LocalDate.now());
            trans.setTipo("Débito");
            trans.setMonto(apuesta.getCreditosApostados());
            transaccionRepository.save(trans);

            //aumenta contador de apuestas hechas
            updateApuestasCont(cuentaUsuario);

            return apuestaRes;
        } catch (Exception e) {
            log.error("createApuesta" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public EventCalculatedData generateEventData(Evento event, Apuesta bet) {
        // Bet algorithm
        Integer eventMultiplier = event.getMultiplicador();
        double multiplier1;
        double multiplier2;
        double multiplierTie;
        EventCalculatedData data = new EventCalculatedData();

        long numBets1 = apuestaRepository.countByApostadoAndEvento(event.getCompetidor1(), event);
        long numBets2 = apuestaRepository.countByApostadoAndEvento(event.getCompetidor2(), event);

        float numCredits1 = apuestaRepository.getSumCredits(event.getCompetidor1().getId(), event.getId());
        float numCredits2 = apuestaRepository.getSumCredits(event.getCompetidor2().getId(), event.getId());
        if (eventMultiplier != 1) {
            eventMultiplier = eventMultiplier / 10 + 1;
        }

        if (event.getDeporte().getNombre().equals("Fútbol")) {
            long numBetsTie = apuestaRepository.countByApostadoAndEvento(event.getCompetidor1(), event);
            float numCreditsTie = apuestaRepository.getSumCreditsTie(event.getId());
            long totalBets = numBets1 + numBets2 + numBetsTie;
            float totalCredits = numCredits1 + numCredits2 + numCreditsTie;

            multiplier1 = BET_BASE + (eventMultiplier * ((1 - (numBets1 / totalBets)) + (1 - (numCredits1 / totalCredits))));
            multiplier2 = BET_BASE + (eventMultiplier * ((1 - (numBets2 / totalBets)) + (1 - (numCredits2 / totalCredits))));
            multiplierTie = BET_BASE + (eventMultiplier * ((1 - (numBetsTie / totalBets)) + (1 - (numCreditsTie / totalCredits))));
        } else {
            multiplierTie = 0;
            long totalBets = numBets1 + numBets2;
            float totalCredits = numCredits1 + numCredits2;

            multiplier1 = BET_BASE + (eventMultiplier * ((1 - (numBets1 / totalBets)) + (1 - (numCredits1 / totalCredits))));
            multiplier2 = BET_BASE + (eventMultiplier * ((1 - (numBets2 / totalBets)) + (1 - (numCredits2 / totalCredits))));
        }

        data.setMultiplicadorCompetidor1(Math.round(multiplier1 * 100.0) / 100.0);
        data.setMultiplicadorCompetidor2(Math.round(multiplier2 * 100.0) / 100.0);
        data.setMultiplicadorEmpate(Math.round(multiplierTie * 100.0) / 100.0);
        if (bet.getCreditosApostados() != 0) {
            if (Objects.equals(bet.getApostado().getId(), event.getCompetidor1().getId())) {
                data.setGanaciaEstimada(Math.round(bet.getCreditosApostados() * multiplier1));
            } else if (Objects.equals(bet.getApostado().getId(), event.getCompetidor2().getId())) {
                data.setGanaciaEstimada(Math.round(bet.getCreditosApostados() * multiplier2));
            } else {
                data.setGanaciaEstimada(Math.round(bet.getCreditosApostados() * multiplierTie));
            }
        } else {
            data.setGanaciaEstimada(0);
        }

        log.debug("Generated Event Bet Data: {}", data);

        return data;
    }

    private void updateApuestasCont(CuentaUsuario cuentaUsuario) {
        cuentaUsuario.setApuestasTotales(cuentaUsuario.getApuestasTotales() + 1);
        cuentaUsuarioRepository.save(cuentaUsuario);
    }
}
