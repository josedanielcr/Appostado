package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductoUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoUsuario.class);
        ProductoUsuario productoUsuario1 = new ProductoUsuario();
        productoUsuario1.setId(1L);
        ProductoUsuario productoUsuario2 = new ProductoUsuario();
        productoUsuario2.setId(productoUsuario1.getId());
        assertThat(productoUsuario1).isEqualTo(productoUsuario2);
        productoUsuario2.setId(2L);
        assertThat(productoUsuario1).isNotEqualTo(productoUsuario2);
        productoUsuario1.setId(null);
        assertThat(productoUsuario1).isNotEqualTo(productoUsuario2);
    }
}
