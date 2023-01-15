package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.DivisionCompetidor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DivisionCompetidor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DivisionCompetidorRepository extends JpaRepository<DivisionCompetidor, Long> {}
