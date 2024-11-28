package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.CreditCardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreditCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditCard.class);
        CreditCard creditCard1 = getCreditCardSample1();
        CreditCard creditCard2 = new CreditCard();
        assertThat(creditCard1).isNotEqualTo(creditCard2);

        creditCard2.setId(creditCard1.getId());
        assertThat(creditCard1).isEqualTo(creditCard2);

        creditCard2 = getCreditCardSample2();
        assertThat(creditCard1).isNotEqualTo(creditCard2);
    }
}
