package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.ProductoUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductoUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoUsuarioRepository extends JpaRepository<ProductoUsuario, Long> {
    ProductoUsuario findByCodigo(String valueOf);
}
