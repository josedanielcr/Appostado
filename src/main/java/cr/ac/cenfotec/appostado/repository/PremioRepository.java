package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Premio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Premio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {}
