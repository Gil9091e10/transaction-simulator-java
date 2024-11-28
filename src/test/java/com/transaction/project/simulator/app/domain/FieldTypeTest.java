package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.FieldTypeTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageFieldTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FieldTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldType.class);
        FieldType fieldType1 = getFieldTypeSample1();
        FieldType fieldType2 = new FieldType();
        assertThat(fieldType1).isNotEqualTo(fieldType2);

        fieldType2.setId(fieldType1.getId());
        assertThat(fieldType1).isEqualTo(fieldType2);

        fieldType2 = getFieldTypeSample2();
        assertThat(fieldType1).isNotEqualTo(fieldType2);
    }

    @Test
    void messageFieldTypeTest() {
        FieldType fieldType = getFieldTypeRandomSampleGenerator();
        MessageFieldType messageFieldTypeBack = getMessageFieldTypeRandomSampleGenerator();

        fieldType.addMessageFieldType(messageFieldTypeBack);
        assertThat(fieldType.getMessageFieldTypes()).containsOnly(messageFieldTypeBack);
        assertThat(messageFieldTypeBack.getFieldType()).isEqualTo(fieldType);

        fieldType.removeMessageFieldType(messageFieldTypeBack);
        assertThat(fieldType.getMessageFieldTypes()).doesNotContain(messageFieldTypeBack);
        assertThat(messageFieldTypeBack.getFieldType()).isNull();

        fieldType.messageFieldTypes(new HashSet<>(Set.of(messageFieldTypeBack)));
        assertThat(fieldType.getMessageFieldTypes()).containsOnly(messageFieldTypeBack);
        assertThat(messageFieldTypeBack.getFieldType()).isEqualTo(fieldType);

        fieldType.setMessageFieldTypes(new HashSet<>());
        assertThat(fieldType.getMessageFieldTypes()).doesNotContain(messageFieldTypeBack);
        assertThat(messageFieldTypeBack.getFieldType()).isNull();
    }
}
