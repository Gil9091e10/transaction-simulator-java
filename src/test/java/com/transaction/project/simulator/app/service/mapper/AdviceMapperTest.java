package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.AdviceAsserts.*;
import static com.transaction.project.simulator.app.domain.AdviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdviceMapperTest {

    private AdviceMapper adviceMapper;

    @BeforeEach
    void setUp() {
        adviceMapper = new AdviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdviceSample1();
        var actual = adviceMapper.toEntity(adviceMapper.toDto(expected));
        assertAdviceAllPropertiesEquals(expected, actual);
    }
}
