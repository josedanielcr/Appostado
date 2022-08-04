package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.repository.ApuestaRepository;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
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

    public ApuestaService(CuentaUsuarioRepository cuentaUsuarioRepository, ApuestaRepository apuestaRepository) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.apuestaRepository = apuestaRepository;
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
            return apuestaRes;
        } catch (Exception e) {
            log.error("createApuesta" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
