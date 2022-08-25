package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import cr.ac.cenfotec.appostado.domain.Transaccion;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findAllByCuenta(CuentaUsuario cuenta);
    List<Transaccion> findAllByCuentaOrderByIdDesc(CuentaUsuario cuenta);
    List<Transaccion> findAllByCuentaAndTipo(CuentaUsuario cuenta, String tipo);
}
