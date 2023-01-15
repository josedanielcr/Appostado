package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpcionRolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpcionRol.class);
        OpcionRol opcionRol1 = new OpcionRol();
        opcionRol1.setId(1L);
        OpcionRol opcionRol2 = new OpcionRol();
        opcionRol2.setId(opcionRol1.getId());
        assertThat(opcionRol1).isEqualTo(opcionRol2);
        opcionRol2.setId(2L);
        assertThat(opcionRol1).isNotEqualTo(opcionRol2);
        opcionRol1.setId(null);
        assertThat(opcionRol1).isNotEqualTo(opcionRol2);
    }
}
