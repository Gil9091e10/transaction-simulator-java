package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.DebitCardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DebitCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DebitCard.class);
        DebitCard debitCard1 = getDebitCardSample1();
        DebitCard debitCard2 = new DebitCard();
        assertThat(debitCard1).isNotEqualTo(debitCard2);

        debitCard2.setId(debitCard1.getId());
        assertThat(debitCard1).isEqualTo(debitCard2);

        debitCard2 = getDebitCardSample2();
        assertThat(debitCard1).isNotEqualTo(debitCard2);
    }

    @Test
    void hashCodeVerifier() {
        DebitCard debitCard = new DebitCard();
        assertThat(debitCard.hashCode()).isZero();

        DebitCard debitCard1 = getDebitCardSample1();
        debitCard.setId(debitCard1.getId());
        assertThat(debitCard).hasSameHashCodeAs(debitCard1);
    }
}
