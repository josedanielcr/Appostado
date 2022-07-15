package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.OpcionRol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OpcionRol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpcionRolRepository extends JpaRepository<OpcionRol, Long> {}
