package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.MessageIsoConfigAsserts.*;
import static com.transaction.project.simulator.app.domain.MessageIsoConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageIsoConfigMapperTest {

    private MessageIsoConfigMapper messageIsoConfigMapper;

    @BeforeEach
    void setUp() {
        messageIsoConfigMapper = new MessageIsoConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageIsoConfigSample1();
        var actual = messageIsoConfigMapper.toEntity(messageIsoConfigMapper.toDto(expected));
        assertMessageIsoConfigAllPropertiesEquals(expected, actual);
    }
}
