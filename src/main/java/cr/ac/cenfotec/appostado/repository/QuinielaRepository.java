package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Quiniela;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quiniela entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuinielaRepository extends JpaRepository<Quiniela, Long> {}
