package cr.ac.cenfotec.appostado.service;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Transaccion;
import cr.ac.cenfotec.appostado.domain.Usuario;
import cr.ac.cenfotec.appostado.repository.CuentaUsuarioRepository;
import cr.ac.cenfotec.appostado.repository.TransaccionRepository;
import cr.ac.cenfotec.appostado.repository.UsuarioRepository;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing monetary accounts.
 */
@Service
@Transactional
public class CuentaUsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final float BALANCE_INICIAL = 1000;

    private final CuentaUsuarioRepository cuentaUsuarioRepository;

    private final TransaccionRepository transaccionRepository;

    public CuentaUsuarioService(CuentaUsuarioRepository cuentaUsuarioRepository, TransaccionRepository transaccionRepository) {
        this.cuentaUsuarioRepository = cuentaUsuarioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    /**
     *
     * @return The new monetary account
     */
    public CuentaUsuario createCuenta(Usuario usuario) {
        CuentaUsuario moneyAccount = new CuentaUsuario();
        moneyAccount.setApuestasGanadas(0);
        moneyAccount.setApuestasTotales(0);
        moneyAccount.setNumCanjes(0);
        moneyAccount.setBalance(BALANCE_INICIAL);
        moneyAccount.setUsuario(usuario);
        moneyAccount = cuentaUsuarioRepository.save(moneyAccount);
        log.debug("Created Information for Cuenta: {}", moneyAccount);

        Transaccion trans = new Transaccion();
        trans.setCuenta(moneyAccount);
        trans.setDescripcion("Bono inicial de jugador nuevo");
        trans.setFecha(LocalDate.now());
        trans.setTipo("Bono");
        trans.setMonto(BALANCE_INICIAL);

        trans = transaccionRepository.save(trans);

        log.debug("Registering initial transaccion: {}", trans);

        return moneyAccount;
    }
}
