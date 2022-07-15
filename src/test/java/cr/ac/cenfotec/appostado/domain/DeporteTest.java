package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeporteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deporte.class);
        Deporte deporte1 = new Deporte();
        deporte1.setId(1L);
        Deporte deporte2 = new Deporte();
        deporte2.setId(deporte1.getId());
        assertThat(deporte1).isEqualTo(deporte2);
        deporte2.setId(2L);
        assertThat(deporte1).isNotEqualTo(deporte2);
        deporte1.setId(null);
        assertThat(deporte1).isNotEqualTo(deporte2);
    }
}
