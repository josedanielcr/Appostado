package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Transaccion;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import java.time.LocalDate;
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

    public ApuestaService(
        CuentaUsuarioRepository cuentaUsuarioRepository,
        ApuestaRepository apuestaRepository,
        TransaccionRepository transaccionRepository
    ) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.apuestaRepository = apuestaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public Apuesta createApuesta(Apuesta apuesta) throws Exception {
        try {
            CuentaUsuario cuentaUsuario = this.cuentaUsuarioRepository.findByUsuarioId(apuesta.getUsuario().getId()).get();
            if (cuentaUsuario.getBalance() < apuesta.getCreditosApostados()) {
                throw new Exception("Usuario no posee créditos suficientes para realizar la apuesta");
            }
            Apuesta apuestaRes = this.apuestaRepository.save(apuesta);
            this.cuentaUsuarioRepository.setUserBalance(
                    cuentaUsuario.getId(),
                    (cuentaUsuario.getBalance() - apuesta.getCreditosApostados())
                );

            //genera transaccion de tipo debido
            Transaccion trans = new Transaccion();
            trans.setCuenta(cuentaUsuario);
            trans.setDescripcion("Rebajo por apuesta " + apuesta.getId() + ", apuesta a competidor: " + apuesta.getApostado().getNombre());
            trans.setFecha(LocalDate.now());
            trans.setTipo("Débito");
            trans.setMonto(apuesta.getCreditosApostados());
            transaccionRepository.save(trans);

            return apuestaRes;
        } catch (Exception e) {
            log.error("createApuesta" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
