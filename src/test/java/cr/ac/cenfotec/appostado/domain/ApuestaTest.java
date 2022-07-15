package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApuestaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apuesta.class);
        Apuesta apuesta1 = new Apuesta();
        apuesta1.setId(1L);
        Apuesta apuesta2 = new Apuesta();
        apuesta2.setId(apuesta1.getId());
        assertThat(apuesta1).isEqualTo(apuesta2);
        apuesta2.setId(2L);
        assertThat(apuesta1).isNotEqualTo(apuesta2);
        apuesta1.setId(null);
        assertThat(apuesta1).isNotEqualTo(apuesta2);
    }
}
