package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Liga;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Liga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigaRepository extends JpaRepository<Liga, Long> {}
