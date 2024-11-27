package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AdviceTestSamples.*;
import static com.transaction.project.simulator.app.domain.MerchantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MerchantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Merchant.class);
        Merchant merchant1 = getMerchantSample1();
        Merchant merchant2 = new Merchant();
        assertThat(merchant1).isNotEqualTo(merchant2);

        merchant2.setId(merchant1.getId());
        assertThat(merchant1).isEqualTo(merchant2);

        merchant2 = getMerchantSample2();
        assertThat(merchant1).isNotEqualTo(merchant2);
    }

    @Test
    void adviceTest() {
        Merchant merchant = getMerchantRandomSampleGenerator();
        Advice adviceBack = getAdviceRandomSampleGenerator();

        merchant.addAdvice(adviceBack);
        assertThat(merchant.getAdvice()).containsOnly(adviceBack);
        assertThat(adviceBack.getMerchant()).isEqualTo(merchant);

        merchant.removeAdvice(adviceBack);
        assertThat(merchant.getAdvice()).doesNotContain(adviceBack);
        assertThat(adviceBack.getMerchant()).isNull();

        merchant.advice(new HashSet<>(Set.of(adviceBack)));
        assertThat(merchant.getAdvice()).containsOnly(adviceBack);
        assertThat(adviceBack.getMerchant()).isEqualTo(merchant);

        merchant.setAdvice(new HashSet<>());
        assertThat(merchant.getAdvice()).doesNotContain(adviceBack);
        assertThat(adviceBack.getMerchant()).isNull();
    }
}
