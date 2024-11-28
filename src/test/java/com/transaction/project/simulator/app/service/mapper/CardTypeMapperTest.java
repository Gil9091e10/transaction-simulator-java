package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.CardTypeAsserts.*;
import static com.transaction.project.simulator.app.domain.CardTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardTypeMapperTest {

    private CardTypeMapper cardTypeMapper;

    @BeforeEach
    void setUp() {
        cardTypeMapper = new CardTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCardTypeSample1();
        var actual = cardTypeMapper.toEntity(cardTypeMapper.toDto(expected));
        assertCardTypeAllPropertiesEquals(expected, actual);
    }
}
