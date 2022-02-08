package net.subsquid.quest.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicantMapperTest {

    private ApplicantMapper applicantMapper;

    @BeforeEach
    public void setUp() {
        applicantMapper = new ApplicantMapperImpl();
    }
}
