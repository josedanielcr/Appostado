package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Apuesta;
import cr.ac.cenfotec.appostado.domain.Competidor;
import cr.ac.cenfotec.appostado.domain.Evento;
import cr.ac.cenfotec.appostado.domain.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Apuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApuestaRepository extends JpaRepository<Apuesta, Long> {
    List<Apuesta> findApuestaByEventoId(Long idEvento);
    long countByApostadoAndEvento(Competidor apostado, Evento evento);
    List<Apuesta> findAllByUsuarioAndEstado(Usuario usuario, String estado);
    List<Apuesta> findAllByUsuarioAndEstadoOrderByIdDesc(Usuario usuario, String estado);
    List<Apuesta> findAllByEvento(Evento evento);

    @Query("SELECT COALESCE(sum(a.creditosApostados),0) FROM Apuesta a WHERE a.apostado.id = :idCompetidor AND a.evento.id = :idEvento")
    float getSumCredits(@Param("idCompetidor") Long idCompetidor, @Param("idEvento") Long idEvento);

    @Query("SELECT COALESCE(sum(a.creditosApostados),0) FROM Apuesta a WHERE a.apostado is NULL AND a.evento.id = :idEvento")
    float getSumCreditsTie(@Param("idEvento") Long idEvento);
}
