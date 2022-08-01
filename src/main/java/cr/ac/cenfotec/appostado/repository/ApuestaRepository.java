package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Apuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApuestaRepository extends JpaRepository<Apuesta, Long> {
    List<Apuesta> findApuestaByEventoId(Long idEvento);
}
