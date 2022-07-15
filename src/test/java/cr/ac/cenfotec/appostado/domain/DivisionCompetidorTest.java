package cr.ac.cenfotec.appostado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cr.ac.cenfotec.appostado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DivisionCompetidorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DivisionCompetidor.class);
        DivisionCompetidor divisionCompetidor1 = new DivisionCompetidor();
        divisionCompetidor1.setId(1L);
        DivisionCompetidor divisionCompetidor2 = new DivisionCompetidor();
        divisionCompetidor2.setId(divisionCompetidor1.getId());
        assertThat(divisionCompetidor1).isEqualTo(divisionCompetidor2);
        divisionCompetidor2.setId(2L);
        assertThat(divisionCompetidor1).isNotEqualTo(divisionCompetidor2);
        divisionCompetidor1.setId(null);
        assertThat(divisionCompetidor1).isNotEqualTo(divisionCompetidor2);
    }
}
