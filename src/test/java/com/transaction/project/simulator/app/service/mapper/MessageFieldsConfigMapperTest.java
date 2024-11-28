package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.MessageFieldsConfigAsserts.*;
import static com.transaction.project.simulator.app.domain.MessageFieldsConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageFieldsConfigMapperTest {

    private MessageFieldsConfigMapper messageFieldsConfigMapper;

    @BeforeEach
    void setUp() {
        messageFieldsConfigMapper = new MessageFieldsConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageFieldsConfigSample1();
        var actual = messageFieldsConfigMapper.toEntity(messageFieldsConfigMapper.toDto(expected));
        assertMessageFieldsConfigAllPropertiesEquals(expected, actual);
    }
}
