package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Mision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisionRepository extends JpaRepository<Mision, Long> {}
