package net.subsquid.quest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.subsquid.quest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Applicant.class);
        Applicant applicant1 = new Applicant();
        applicant1.setId(1L);
        Applicant applicant2 = new Applicant();
        applicant2.setId(applicant1.getId());
        assertThat(applicant1).isEqualTo(applicant2);
        applicant2.setId(2L);
        assertThat(applicant1).isNotEqualTo(applicant2);
        applicant1.setId(null);
        assertThat(applicant1).isNotEqualTo(applicant2);
    }
}
