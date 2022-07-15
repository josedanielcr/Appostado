package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.ApuestaTransaccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApuestaTransaccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApuestaTransaccionRepository extends JpaRepository<ApuestaTransaccion, Long> {}
