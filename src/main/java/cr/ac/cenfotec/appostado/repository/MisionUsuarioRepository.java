package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.MisionUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MisionUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisionUsuarioRepository extends JpaRepository<MisionUsuario, Long> {}
