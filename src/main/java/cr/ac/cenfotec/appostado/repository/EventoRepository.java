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
        "AND E.estado = :P_ESTADO"
    )
    List<Evento> findEventoByDeporteAndDivisionAndEstado(
        @Param("P_DEPORTE") Long deporte,
        @Param("P_DIVISION") Long division,
        @Param("P_ESTADO") String estado
    );

    @Query(
        "SELECT EV " +
        "FROM Apuesta AP INNER JOIN Evento EV" +
        " ON AP.evento.id = EV.id " +
        "WHERE AP.usuario.id = :P_USER_ID " +
        "AND EV.estado = :P_ESTADO " +
        "ORDER BY EV.id DESC"
    )
    List<Evento> findEventoByApuestaID(@Param("P_USER_ID") Long usuario_id, @Param("P_ESTADO") String estado);
}
