package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mision.class);
        Mision mision1 = new Mision();
        mision1.setId(1L);
        Mision mision2 = new Mision();
        mision2.setId(mision1.getId());
        assertThat(mision1).isEqualTo(mision2);
        mision2.setId(2L);
        assertThat(mision1).isNotEqualTo(mision2);
        mision1.setId(null);
        assertThat(mision1).isNotEqualTo(mision2);
    }
}
