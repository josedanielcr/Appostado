package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Competidor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Competidor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetidorRepository extends JpaRepository<Competidor, Long> {
    Competidor findByNombre(String nombre);
}
