package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AcquirerTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageIsoConfigTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageTypeIndicatorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageIsoConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageIsoConfig.class);
        MessageIsoConfig messageIsoConfig1 = getMessageIsoConfigSample1();
        MessageIsoConfig messageIsoConfig2 = new MessageIsoConfig();
        assertThat(messageIsoConfig1).isNotEqualTo(messageIsoConfig2);

        messageIsoConfig2.setId(messageIsoConfig1.getId());
        assertThat(messageIsoConfig1).isEqualTo(messageIsoConfig2);

        messageIsoConfig2 = getMessageIsoConfigSample2();
        assertThat(messageIsoConfig1).isNotEqualTo(messageIsoConfig2);
    }

    @Test
    void acquirerTest() {
        MessageIsoConfig messageIsoConfig = getMessageIsoConfigRandomSampleGenerator();
        Acquirer acquirerBack = getAcquirerRandomSampleGenerator();

        messageIsoConfig.setAcquirer(acquirerBack);
        assertThat(messageIsoConfig.getAcquirer()).isEqualTo(acquirerBack);

        messageIsoConfig.acquirer(null);
        assertThat(messageIsoConfig.getAcquirer()).isNull();
    }

    @Test
    void messageTypeIndicatorTest() {
        MessageIsoConfig messageIsoConfig = getMessageIsoConfigRandomSampleGenerator();
        MessageTypeIndicator messageTypeIndicatorBack = getMessageTypeIndicatorRandomSampleGenerator();

        messageIsoConfig.setMessageTypeIndicator(messageTypeIndicatorBack);
        assertThat(messageIsoConfig.getMessageTypeIndicator()).isEqualTo(messageTypeIndicatorBack);

        messageIsoConfig.messageTypeIndicator(null);
        assertThat(messageIsoConfig.getMessageTypeIndicator()).isNull();
    }
}
