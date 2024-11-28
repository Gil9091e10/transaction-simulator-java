package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.MessageIsoConfigTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageTypeIndicatorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MessageTypeIndicatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageTypeIndicator.class);
        MessageTypeIndicator messageTypeIndicator1 = getMessageTypeIndicatorSample1();
        MessageTypeIndicator messageTypeIndicator2 = new MessageTypeIndicator();
        assertThat(messageTypeIndicator1).isNotEqualTo(messageTypeIndicator2);

        messageTypeIndicator2.setId(messageTypeIndicator1.getId());
        assertThat(messageTypeIndicator1).isEqualTo(messageTypeIndicator2);

        messageTypeIndicator2 = getMessageTypeIndicatorSample2();
        assertThat(messageTypeIndicator1).isNotEqualTo(messageTypeIndicator2);
    }

    @Test
    void messageIsoConfigTest() {
        MessageTypeIndicator messageTypeIndicator = getMessageTypeIndicatorRandomSampleGenerator();
        MessageIsoConfig messageIsoConfigBack = getMessageIsoConfigRandomSampleGenerator();

        messageTypeIndicator.addMessageIsoConfig(messageIsoConfigBack);
        assertThat(messageTypeIndicator.getMessageIsoConfigs()).containsOnly(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getMessageTypeIndicator()).isEqualTo(messageTypeIndicator);

        messageTypeIndicator.removeMessageIsoConfig(messageIsoConfigBack);
        assertThat(messageTypeIndicator.getMessageIsoConfigs()).doesNotContain(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getMessageTypeIndicator()).isNull();

        messageTypeIndicator.messageIsoConfigs(new HashSet<>(Set.of(messageIsoConfigBack)));
        assertThat(messageTypeIndicator.getMessageIsoConfigs()).containsOnly(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getMessageTypeIndicator()).isEqualTo(messageTypeIndicator);

        messageTypeIndicator.setMessageIsoConfigs(new HashSet<>());
        assertThat(messageTypeIndicator.getMessageIsoConfigs()).doesNotContain(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getMessageTypeIndicator()).isNull();
    }
}
