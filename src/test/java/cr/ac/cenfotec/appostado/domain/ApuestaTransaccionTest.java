package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApuestaTransaccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApuestaTransaccion.class);
        ApuestaTransaccion apuestaTransaccion1 = new ApuestaTransaccion();
        apuestaTransaccion1.setId(1L);
        ApuestaTransaccion apuestaTransaccion2 = new ApuestaTransaccion();
        apuestaTransaccion2.setId(apuestaTransaccion1.getId());
        assertThat(apuestaTransaccion1).isEqualTo(apuestaTransaccion2);
        apuestaTransaccion2.setId(2L);
        assertThat(apuestaTransaccion1).isNotEqualTo(apuestaTransaccion2);
        apuestaTransaccion1.setId(null);
        assertThat(apuestaTransaccion1).isNotEqualTo(apuestaTransaccion2);
    }
}
