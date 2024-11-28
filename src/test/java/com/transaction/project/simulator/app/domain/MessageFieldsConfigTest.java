package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.MessageFieldTypeTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageFieldsConfigTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageIsoConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageFieldsConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageFieldsConfig.class);
        MessageFieldsConfig messageFieldsConfig1 = getMessageFieldsConfigSample1();
        MessageFieldsConfig messageFieldsConfig2 = new MessageFieldsConfig();
        assertThat(messageFieldsConfig1).isNotEqualTo(messageFieldsConfig2);

        messageFieldsConfig2.setId(messageFieldsConfig1.getId());
        assertThat(messageFieldsConfig1).isEqualTo(messageFieldsConfig2);

        messageFieldsConfig2 = getMessageFieldsConfigSample2();
        assertThat(messageFieldsConfig1).isNotEqualTo(messageFieldsConfig2);
    }

    @Test
    void messageIsoConfigTest() {
        MessageFieldsConfig messageFieldsConfig = getMessageFieldsConfigRandomSampleGenerator();
        MessageIsoConfig messageIsoConfigBack = getMessageIsoConfigRandomSampleGenerator();

        messageFieldsConfig.setMessageIsoConfig(messageIsoConfigBack);
        assertThat(messageFieldsConfig.getMessageIsoConfig()).isEqualTo(messageIsoConfigBack);

        messageFieldsConfig.messageIsoConfig(null);
        assertThat(messageFieldsConfig.getMessageIsoConfig()).isNull();
    }

    @Test
    void messageFieldTypeTest() {
        MessageFieldsConfig messageFieldsConfig = getMessageFieldsConfigRandomSampleGenerator();
        MessageFieldType messageFieldTypeBack = getMessageFieldTypeRandomSampleGenerator();

        messageFieldsConfig.setMessageFieldType(messageFieldTypeBack);
        assertThat(messageFieldsConfig.getMessageFieldType()).isEqualTo(messageFieldTypeBack);

        messageFieldsConfig.messageFieldType(null);
        assertThat(messageFieldsConfig.getMessageFieldType()).isNull();
    }
}
