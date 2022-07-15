package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Amigo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Amigo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmigoRepository extends JpaRepository<Amigo, Long> {}
