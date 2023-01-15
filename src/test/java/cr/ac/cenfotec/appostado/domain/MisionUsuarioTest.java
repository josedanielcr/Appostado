package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisionUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MisionUsuario.class);
        MisionUsuario misionUsuario1 = new MisionUsuario();
        misionUsuario1.setId(1L);
        MisionUsuario misionUsuario2 = new MisionUsuario();
        misionUsuario2.setId(misionUsuario1.getId());
        assertThat(misionUsuario1).isEqualTo(misionUsuario2);
        misionUsuario2.setId(2L);
        assertThat(misionUsuario1).isNotEqualTo(misionUsuario2);
        misionUsuario1.setId(null);
        assertThat(misionUsuario1).isNotEqualTo(misionUsuario2);
    }
}
