package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AccountBankTestSamples.*;
import static com.transaction.project.simulator.app.domain.CurrencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CurrencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Currency.class);
        Currency currency1 = getCurrencySample1();
        Currency currency2 = new Currency();
        assertThat(currency1).isNotEqualTo(currency2);

        currency2.setId(currency1.getId());
        assertThat(currency1).isEqualTo(currency2);

        currency2 = getCurrencySample2();
        assertThat(currency1).isNotEqualTo(currency2);
    }

    @Test
    void accountBankTest() {
        Currency currency = getCurrencyRandomSampleGenerator();
        AccountBank accountBankBack = getAccountBankRandomSampleGenerator();

        currency.addAccountBank(accountBankBack);
        assertThat(currency.getAccountBanks()).containsOnly(accountBankBack);
        assertThat(accountBankBack.getCurrency()).isEqualTo(currency);

        currency.removeAccountBank(accountBankBack);
        assertThat(currency.getAccountBanks()).doesNotContain(accountBankBack);
        assertThat(accountBankBack.getCurrency()).isNull();

        currency.accountBanks(new HashSet<>(Set.of(accountBankBack)));
        assertThat(currency.getAccountBanks()).containsOnly(accountBankBack);
        assertThat(accountBankBack.getCurrency()).isEqualTo(currency);

        currency.setAccountBanks(new HashSet<>());
        assertThat(currency.getAccountBanks()).doesNotContain(accountBankBack);
        assertThat(accountBankBack.getCurrency()).isNull();
    }
}
