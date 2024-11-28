package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AcquirerTestSamples.*;
import static com.transaction.project.simulator.app.domain.AdviceTestSamples.*;
import static com.transaction.project.simulator.app.domain.MessageIsoConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AcquirerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acquirer.class);
        Acquirer acquirer1 = getAcquirerSample1();
        Acquirer acquirer2 = new Acquirer();
        assertThat(acquirer1).isNotEqualTo(acquirer2);

        acquirer2.setId(acquirer1.getId());
        assertThat(acquirer1).isEqualTo(acquirer2);

        acquirer2 = getAcquirerSample2();
        assertThat(acquirer1).isNotEqualTo(acquirer2);
    }

    @Test
    void adviceTest() {
        Acquirer acquirer = getAcquirerRandomSampleGenerator();
        Advice adviceBack = getAdviceRandomSampleGenerator();

        acquirer.addAdvice(adviceBack);
        assertThat(acquirer.getAdvice()).containsOnly(adviceBack);
        assertThat(adviceBack.getAcquirer()).isEqualTo(acquirer);

        acquirer.removeAdvice(adviceBack);
        assertThat(acquirer.getAdvice()).doesNotContain(adviceBack);
        assertThat(adviceBack.getAcquirer()).isNull();

        acquirer.advice(new HashSet<>(Set.of(adviceBack)));
        assertThat(acquirer.getAdvice()).containsOnly(adviceBack);
        assertThat(adviceBack.getAcquirer()).isEqualTo(acquirer);

        acquirer.setAdvice(new HashSet<>());
        assertThat(acquirer.getAdvice()).doesNotContain(adviceBack);
        assertThat(adviceBack.getAcquirer()).isNull();
    }

    @Test
    void messageIsoConfigTest() {
        Acquirer acquirer = getAcquirerRandomSampleGenerator();
        MessageIsoConfig messageIsoConfigBack = getMessageIsoConfigRandomSampleGenerator();

        acquirer.addMessageIsoConfig(messageIsoConfigBack);
        assertThat(acquirer.getMessageIsoConfigs()).containsOnly(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getAcquirer()).isEqualTo(acquirer);

        acquirer.removeMessageIsoConfig(messageIsoConfigBack);
        assertThat(acquirer.getMessageIsoConfigs()).doesNotContain(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getAcquirer()).isNull();

        acquirer.messageIsoConfigs(new HashSet<>(Set.of(messageIsoConfigBack)));
        assertThat(acquirer.getMessageIsoConfigs()).containsOnly(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getAcquirer()).isEqualTo(acquirer);

        acquirer.setMessageIsoConfigs(new HashSet<>());
        assertThat(acquirer.getMessageIsoConfigs()).doesNotContain(messageIsoConfigBack);
        assertThat(messageIsoConfigBack.getAcquirer()).isNull();
    }
}
