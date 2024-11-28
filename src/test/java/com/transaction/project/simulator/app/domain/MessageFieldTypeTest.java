package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.FieldTypeTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageFieldTypeTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageFieldsConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MessageFieldTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageFieldType.class);
        MessageFieldType messageFieldType1 = getMessageFieldTypeSample1();
        MessageFieldType messageFieldType2 = new MessageFieldType();
        assertThat(messageFieldType1).isNotEqualTo(messageFieldType2);

        messageFieldType2.setId(messageFieldType1.getId());
        assertThat(messageFieldType1).isEqualTo(messageFieldType2);

        messageFieldType2 = getMessageFieldTypeSample2();
        assertThat(messageFieldType1).isNotEqualTo(messageFieldType2);
    }

    @Test
    void messageFieldsConfigTest() {
        MessageFieldType messageFieldType = getMessageFieldTypeRandomSampleGenerator();
        MessageFieldsConfig messageFieldsConfigBack = getMessageFieldsConfigRandomSampleGenerator();

        messageFieldType.addMessageFieldsConfig(messageFieldsConfigBack);
        assertThat(messageFieldType.getMessageFieldsConfigs()).containsOnly(messageFieldsConfigBack);
        assertThat(messageFieldsConfigBack.getMessageFieldType()).isEqualTo(messageFieldType);

        messageFieldType.removeMessageFieldsConfig(messageFieldsConfigBack);
        assertThat(messageFieldType.getMessageFieldsConfigs()).doesNotContain(messageFieldsConfigBack);
        assertThat(messageFieldsConfigBack.getMessageFieldType()).isNull();

        messageFieldType.messageFieldsConfigs(new HashSet<>(Set.of(messageFieldsConfigBack)));
        assertThat(messageFieldType.getMessageFieldsConfigs()).containsOnly(messageFieldsConfigBack);
        assertThat(messageFieldsConfigBack.getMessageFieldType()).isEqualTo(messageFieldType);

        messageFieldType.setMessageFieldsConfigs(new HashSet<>());
        assertThat(messageFieldType.getMessageFieldsConfigs()).doesNotContain(messageFieldsConfigBack);
        assertThat(messageFieldsConfigBack.getMessageFieldType()).isNull();
    }

    @Test
    void fieldTypeTest() {
        MessageFieldType messageFieldType = getMessageFieldTypeRandomSampleGenerator();
        FieldType fieldTypeBack = getFieldTypeRandomSampleGenerator();

        messageFieldType.setFieldType(fieldTypeBack);
        assertThat(messageFieldType.getFieldType()).isEqualTo(fieldTypeBack);

        messageFieldType.fieldType(null);
        assertThat(messageFieldType.getFieldType()).isNull();
    }
}
