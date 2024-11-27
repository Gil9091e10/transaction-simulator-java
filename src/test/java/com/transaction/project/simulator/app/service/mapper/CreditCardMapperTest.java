package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.CreditCardAsserts.*;
import static com.transaction.project.simulator.app.domain.CreditCardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreditCardMapperTest {

    private CreditCardMapper creditCardMapper;

    @BeforeEach
    void setUp() {
        creditCardMapper = new CreditCardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCreditCardSample1();
        var actual = creditCardMapper.toEntity(creditCardMapper.toDto(expected));
        assertCreditCardAllPropertiesEquals(expected, actual);
    }
}
