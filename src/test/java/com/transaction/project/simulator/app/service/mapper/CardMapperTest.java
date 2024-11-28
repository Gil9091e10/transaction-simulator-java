package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.CardAsserts.*;
import static com.transaction.project.simulator.app.domain.CardTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCardSample1();
        var actual = cardMapper.toEntity(cardMapper.toDto(expected));
        assertCardAllPropertiesEquals(expected, actual);
    }
}
