package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.AcquirerAsserts.*;
import static com.transaction.project.simulator.app.domain.AcquirerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AcquirerMapperTest {

    private AcquirerMapper acquirerMapper;

    @BeforeEach
    void setUp() {
        acquirerMapper = new AcquirerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAcquirerSample1();
        var actual = acquirerMapper.toEntity(acquirerMapper.toDto(expected));
        assertAcquirerAllPropertiesEquals(expected, actual);
    }
}
