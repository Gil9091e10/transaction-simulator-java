package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AccountBankTestSamples.*;
import static com.transaction.project.simulator.app.domain.CardTestSamples.*;
import static com.transaction.project.simulator.app.domain.CurrencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBank.class);
        AccountBank accountBank1 = getAccountBankSample1();
        AccountBank accountBank2 = new AccountBank();
        assertThat(accountBank1).isNotEqualTo(accountBank2);

        accountBank2.setId(accountBank1.getId());
        assertThat(accountBank1).isEqualTo(accountBank2);

        accountBank2 = getAccountBankSample2();
        assertThat(accountBank1).isNotEqualTo(accountBank2);
    }

    @Test
    void cardTest() {
        AccountBank accountBank = getAccountBankRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        accountBank.setCard(cardBack);
        assertThat(accountBank.getCard()).isEqualTo(cardBack);
        assertThat(cardBack.getAccountBank()).isEqualTo(accountBank);

        accountBank.card(null);
        assertThat(accountBank.getCard()).isNull();
        assertThat(cardBack.getAccountBank()).isNull();
    }

    @Test
    void currencyTest() {
        AccountBank accountBank = getAccountBankRandomSampleGenerator();
        Currency currencyBack = getCurrencyRandomSampleGenerator();

        accountBank.setCurrency(currencyBack);
        assertThat(accountBank.getCurrency()).isEqualTo(currencyBack);

        accountBank.currency(null);
        assertThat(accountBank.getCurrency()).isNull();
    }
}
