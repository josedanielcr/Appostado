package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.LigaUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigaUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigaUsuarioRepository extends JpaRepository<LigaUsuario, Long> {}
