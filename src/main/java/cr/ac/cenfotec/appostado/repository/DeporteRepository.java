package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Deporte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deporte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long> {}
