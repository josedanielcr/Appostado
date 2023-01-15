package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuinielaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quiniela.class);
        Quiniela quiniela1 = new Quiniela();
        quiniela1.setId(1L);
        Quiniela quiniela2 = new Quiniela();
        quiniela2.setId(quiniela1.getId());
        assertThat(quiniela1).isEqualTo(quiniela2);
        quiniela2.setId(2L);
        assertThat(quiniela1).isNotEqualTo(quiniela2);
        quiniela1.setId(null);
        assertThat(quiniela1).isNotEqualTo(quiniela2);
    }
}
