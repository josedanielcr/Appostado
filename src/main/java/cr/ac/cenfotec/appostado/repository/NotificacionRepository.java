package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Notificacion;
import cr.ac.cenfotec.appostado.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Notificacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findAllByUsuarioAndTipoAndHaGanado(Usuario usuario, String tipo, Boolean isResolved);
    List<Notificacion> findAllByDescripcionAndTipoAndHaGanado(String descripcion, String tipo, Boolean isResolved);
    Boolean existsByUsuarioAndDescripcionAndHaGanado(Usuario usuario, String descripcion, Boolean haResuelto);
    Optional<Notificacion> findByUsuarioAndDescripcion(Usuario usuario, String descripcion);
    Optional<Notificacion> findByUsuarioAndDescripcionAndHaGanado(Usuario usuario, String descripcion, Boolean haResuelto);
}
