package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.MisionTransaccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MisionTransaccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisionTransaccionRepository extends JpaRepository<MisionTransaccion, Long> {}
