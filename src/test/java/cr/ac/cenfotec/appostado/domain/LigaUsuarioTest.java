package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigaUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigaUsuario.class);
        LigaUsuario ligaUsuario1 = new LigaUsuario();
        ligaUsuario1.setId(1L);
        LigaUsuario ligaUsuario2 = new LigaUsuario();
        ligaUsuario2.setId(ligaUsuario1.getId());
        assertThat(ligaUsuario1).isEqualTo(ligaUsuario2);
        ligaUsuario2.setId(2L);
        assertThat(ligaUsuario1).isNotEqualTo(ligaUsuario2);
        ligaUsuario1.setId(null);
        assertThat(ligaUsuario1).isNotEqualTo(ligaUsuario2);
    }
}
