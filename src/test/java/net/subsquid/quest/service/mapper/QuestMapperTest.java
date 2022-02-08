package net.subsquid.quest.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestMapperTest {

    private QuestMapper questMapper;

    @BeforeEach
    public void setUp() {
        questMapper = new QuestMapperImpl();
    }
}
