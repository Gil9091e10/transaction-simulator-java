package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.AccountBankTestSamples.*;
import static com.transaction.project.simulator.app.domain.CardTestSamples.*;
import static com.transaction.project.simulator.app.domain.CardTypeTestSamples.*;
import static com.transaction.project.simulator.app.domain.IssuerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = getCardSample1();
        Card card2 = new Card();
        assertThat(card1).isNotEqualTo(card2);

        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);

        card2 = getCardSample2();
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void accountBankTest() {
        Card card = getCardRandomSampleGenerator();
        AccountBank accountBankBack = getAccountBankRandomSampleGenerator();

        card.setAccountBank(accountBankBack);
        assertThat(card.getAccountBank()).isEqualTo(accountBankBack);

        card.accountBank(null);
        assertThat(card.getAccountBank()).isNull();
    }

    @Test
    void cardTypeTest() {
        Card card = getCardRandomSampleGenerator();
        CardType cardTypeBack = getCardTypeRandomSampleGenerator();

        card.setCardType(cardTypeBack);
        assertThat(card.getCardType()).isEqualTo(cardTypeBack);

        card.cardType(null);
        assertThat(card.getCardType()).isNull();
    }

    @Test
    void issuerTest() {
        Card card = getCardRandomSampleGenerator();
        Issuer issuerBack = getIssuerRandomSampleGenerator();

        card.setIssuer(issuerBack);
        assertThat(card.getIssuer()).isEqualTo(issuerBack);

        card.issuer(null);
        assertThat(card.getIssuer()).isNull();
    }
}
