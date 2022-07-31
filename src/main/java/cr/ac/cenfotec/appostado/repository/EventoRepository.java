package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Evento;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    @Query(
        "SELECT E FROM Evento E " +
        "WHERE (:P_DEPORTE IS NULL OR E.deporte.id = :P_DEPORTE) " +
        "AND (:P_DIVISION IS NULL OR E.division.id = :P_DIVISION) " +
        "AND (:P_ESTADO IS NULL OR E.estado = :P_ESTADO)"
    )
    List<Evento> findEventoByDeporteAndDivisionAndEstado(
        @Param("P_DEPORTE") Long deporte,
        @Param("P_DIVISION") Long division,
        @Param("P_ESTADO") String estado
    );
}
