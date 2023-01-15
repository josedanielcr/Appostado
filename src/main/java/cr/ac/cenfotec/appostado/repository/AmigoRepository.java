package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Amigo;
import cr.ac.cenfotec.appostado.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Amigo entity.
 */

@Repository
public interface AmigoRepository extends JpaRepository<Amigo, Long> {
    List<Amigo> findAllByUsuario(Usuario usuario);
    Boolean existsByUsuarioAndAmigo(Usuario usuario, Usuario amigo);
    void deleteByUsuarioAndAmigo(Usuario usuario, Usuario amigo);
}
