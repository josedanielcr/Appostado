package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmigoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Amigo.class);
        Amigo amigo1 = new Amigo();
        amigo1.setId(1L);
        Amigo amigo2 = new Amigo();
        amigo2.setId(amigo1.getId());
        assertThat(amigo1).isEqualTo(amigo2);
        amigo2.setId(2L);
        assertThat(amigo1).isNotEqualTo(amigo2);
        amigo1.setId(null);
        assertThat(amigo1).isNotEqualTo(amigo2);
    }
}
