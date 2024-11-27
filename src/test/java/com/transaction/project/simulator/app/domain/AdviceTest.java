package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AcquirerTestSamples.*;
import static com.transaction.project.simulator.app.domain.AdviceTestSamples.*;
import static com.transaction.project.simulator.app.domain.MerchantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Advice.class);
        Advice advice1 = getAdviceSample1();
        Advice advice2 = new Advice();
        assertThat(advice1).isNotEqualTo(advice2);

        advice2.setId(advice1.getId());
        assertThat(advice1).isEqualTo(advice2);

        advice2 = getAdviceSample2();
        assertThat(advice1).isNotEqualTo(advice2);
    }

    @Test
    void merchantTest() {
        Advice advice = getAdviceRandomSampleGenerator();
        Merchant merchantBack = getMerchantRandomSampleGenerator();

        advice.setMerchant(merchantBack);
        assertThat(advice.getMerchant()).isEqualTo(merchantBack);

        advice.merchant(null);
        assertThat(advice.getMerchant()).isNull();
    }

    @Test
    void acquirerTest() {
        Advice advice = getAdviceRandomSampleGenerator();
        Acquirer acquirerBack = getAcquirerRandomSampleGenerator();

        advice.setAcquirer(acquirerBack);
        assertThat(advice.getAcquirer()).isEqualTo(acquirerBack);

        advice.acquirer(null);
        assertThat(advice.getAcquirer()).isNull();
    }
}
