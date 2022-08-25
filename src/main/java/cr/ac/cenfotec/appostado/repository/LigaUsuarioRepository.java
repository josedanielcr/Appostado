package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Liga;
import cr.ac.cenfotec.appostado.domain.LigaUsuario;
import cr.ac.cenfotec.appostado.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigaUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigaUsuarioRepository extends JpaRepository<LigaUsuario, Long> {
    List<LigaUsuario> findAllByUsuario(Usuario usuario);
    List<LigaUsuario> findAllByLiga(Liga liga);
    Optional<LigaUsuario> findByUsuarioAndLiga(Usuario usuario, Liga liga);
}
