package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.MessageFieldTypeAsserts.*;
import static com.transaction.project.simulator.app.domain.MessageFieldTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageFieldTypeMapperTest {

    private MessageFieldTypeMapper messageFieldTypeMapper;

    @BeforeEach
    void setUp() {
        messageFieldTypeMapper = new MessageFieldTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageFieldTypeSample1();
        var actual = messageFieldTypeMapper.toEntity(messageFieldTypeMapper.toDto(expected));
        assertMessageFieldTypeAllPropertiesEquals(expected, actual);
    }
}
