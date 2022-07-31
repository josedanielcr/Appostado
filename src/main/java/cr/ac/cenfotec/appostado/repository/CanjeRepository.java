package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Canje;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Canje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CanjeRepository extends JpaRepository<Canje, Long> {
    List<Canje> findByEstado(String incompleto);
}
