package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanjeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Canje.class);
        Canje canje1 = new Canje();
        canje1.setId(1L);
        Canje canje2 = new Canje();
        canje2.setId(canje1.getId());
        assertThat(canje1).isEqualTo(canje2);
        canje2.setId(2L);
        assertThat(canje1).isNotEqualTo(canje2);
        canje1.setId(null);
        assertThat(canje1).isNotEqualTo(canje2);
    }
}
