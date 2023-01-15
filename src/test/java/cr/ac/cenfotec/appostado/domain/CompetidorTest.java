package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetidorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competidor.class);
        Competidor competidor1 = new Competidor();
        competidor1.setId(1L);
        Competidor competidor2 = new Competidor();
        competidor2.setId(competidor1.getId());
        assertThat(competidor1).isEqualTo(competidor2);
        competidor2.setId(2L);
        assertThat(competidor1).isNotEqualTo(competidor2);
        competidor1.setId(null);
        assertThat(competidor1).isNotEqualTo(competidor2);
    }
}
