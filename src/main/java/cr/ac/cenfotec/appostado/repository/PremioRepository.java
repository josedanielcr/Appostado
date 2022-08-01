package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Premio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Premio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
    List<Premio> findByEstado(String estado);

    @Query("select c from Premio c where c.estado = 'Activo'  ORDER BY c.costo")
    List<Premio> findByCostoA();

    @Query("select c from Premio c where c.estado = 'Activo'  ORDER BY c.costo DESC")
    List<Premio> findByCostoD();

    @Query("select c from Premio c where c.estado = 'Activo'   ORDER BY c.numCanjes DESC")
    List<Premio> findByPopularidadA();

    @Query("select c from Premio c where c.estado = 'Activo'  ORDER BY c.numCanjes")
    List<Premio> findByPopularidadD();
}
