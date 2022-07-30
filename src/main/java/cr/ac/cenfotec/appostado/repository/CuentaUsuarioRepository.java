package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CuentaUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuentaUsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    Optional<CuentaUsuario> findCuentaUsuarioByUsuario_Id(Long usuario_id);
}
