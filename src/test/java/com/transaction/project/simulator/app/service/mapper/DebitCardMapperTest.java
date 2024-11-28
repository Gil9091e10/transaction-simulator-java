package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.DebitCardAsserts.*;
import static com.transaction.project.simulator.app.domain.DebitCardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DebitCardMapperTest {

    private DebitCardMapper debitCardMapper;

    @BeforeEach
    void setUp() {
        debitCardMapper = new DebitCardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDebitCardSample1();
        var actual = debitCardMapper.toEntity(debitCardMapper.toDto(expected));
        assertDebitCardAllPropertiesEquals(expected, actual);
    }
}
