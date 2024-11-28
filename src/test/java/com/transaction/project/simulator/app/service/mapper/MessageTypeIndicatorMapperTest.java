package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.MessageTypeIndicatorAsserts.*;
import static com.transaction.project.simulator.app.domain.MessageTypeIndicatorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageTypeIndicatorMapperTest {

    private MessageTypeIndicatorMapper messageTypeIndicatorMapper;

    @BeforeEach
    void setUp() {
        messageTypeIndicatorMapper = new MessageTypeIndicatorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageTypeIndicatorSample1();
        var actual = messageTypeIndicatorMapper.toEntity(messageTypeIndicatorMapper.toDto(expected));
        assertMessageTypeIndicatorAllPropertiesEquals(expected, actual);
    }
}
