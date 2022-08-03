package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.CuentaUsuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CuentaUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuentaUsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    Optional<CuentaUsuario> findByUsuarioId(Long id);

    @Modifying
    @Query("UPDATE CuentaUsuario ca SET ca.balance = :P_NUEVO_BALANCE WHERE ca.id = :P_CUENTA_USUARIO")
    void setUserBalance(@Param("P_CUENTA_USUARIO") Long idCuentaUsuario, @Param("P_NUEVO_BALANCE") float balance);

    Optional<CuentaUsuario> findCuentaUsuarioByUsuario_Id(Long usuario_id);
}
