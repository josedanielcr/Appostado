package cr.ac.cenfotec.appostado.repository;

import cr.ac.cenfotec.appostado.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("select c from Producto c where c.codigoFijo != '' ")
    List<Producto> findAllCodigo();

    @Query("select c from Producto c where c.codigoFijo = '' ")
    List<Producto> findAllSinCodigo();

    @Query("select c from Producto c where c.codigoFijo != ''  ORDER BY c.costo")
    List<Producto> findAllCodigoDescA();

    @Query("select c from Producto c where c.codigoFijo != ''  ORDER BY c.costo DESC")
    List<Producto> findAllCodigoDescD();

    @Query("select c from Producto c where c.codigoFijo != ''  ORDER BY c.numCompras DESC")
    List<Producto> findAllCodigoPopuA();

    @Query("select c from Producto c where c.codigoFijo != ''  ORDER BY c.numCompras")
    List<Producto> findAllCodigoPopuD();

    @Query("select c from Producto c where c.codigoFijo = ''  ORDER BY c.costo")
    List<Producto> findAllSinCodigoDescA();

    @Query("select c from Producto c where c.codigoFijo = ''  ORDER BY c.costo DESC")
    List<Producto> findAllSinCodigoDescD();

    @Query("select c from Producto c where c.codigoFijo = ''  ORDER BY c.numCompras DESC")
    List<Producto> findAllSinCodigoPopuA();

    @Query("select c from Producto c where c.codigoFijo = ''  ORDER BY c.numCompras")
    List<Producto> findAllSinCodigoPopuD();
}
