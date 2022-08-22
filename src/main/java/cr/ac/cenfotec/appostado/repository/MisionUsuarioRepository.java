package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Mision;
import cr.ac.cenfotec.appostado.domain.MisionUsuario;
import cr.ac.cenfotec.appostado.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MisionUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisionUsuarioRepository extends JpaRepository<MisionUsuario, Long> {
    @Query("select c from MisionUsuario c where c.usuario = ?1 AND c.completado=0")
    List<MisionUsuario> findByUsuarioTrivia(Optional<Usuario> usuario);
}
