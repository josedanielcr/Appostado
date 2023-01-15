package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisionTransaccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MisionTransaccion.class);
        MisionTransaccion misionTransaccion1 = new MisionTransaccion();
        misionTransaccion1.setId(1L);
        MisionTransaccion misionTransaccion2 = new MisionTransaccion();
        misionTransaccion2.setId(misionTransaccion1.getId());
        assertThat(misionTransaccion1).isEqualTo(misionTransaccion2);
        misionTransaccion2.setId(2L);
        assertThat(misionTransaccion1).isNotEqualTo(misionTransaccion2);
        misionTransaccion1.setId(null);
        assertThat(misionTransaccion1).isNotEqualTo(misionTransaccion2);
    }
}
