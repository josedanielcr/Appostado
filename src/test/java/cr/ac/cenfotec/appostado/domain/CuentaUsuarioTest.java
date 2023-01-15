package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CuentaUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuentaUsuario.class);
        CuentaUsuario cuentaUsuario1 = new CuentaUsuario();
        cuentaUsuario1.setId(1L);
        CuentaUsuario cuentaUsuario2 = new CuentaUsuario();
        cuentaUsuario2.setId(cuentaUsuario1.getId());
        assertThat(cuentaUsuario1).isEqualTo(cuentaUsuario2);
        cuentaUsuario2.setId(2L);
        assertThat(cuentaUsuario1).isNotEqualTo(cuentaUsuario2);
        cuentaUsuario1.setId(null);
        assertThat(cuentaUsuario1).isNotEqualTo(cuentaUsuario2);
    }
}
