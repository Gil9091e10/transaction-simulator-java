package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.CardTestSamples.*;
import static com.transaction.project.simulator.app.domain.IssuerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IssuerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Issuer.class);
        Issuer issuer1 = getIssuerSample1();
        Issuer issuer2 = new Issuer();
        assertThat(issuer1).isNotEqualTo(issuer2);

        issuer2.setId(issuer1.getId());
        assertThat(issuer1).isEqualTo(issuer2);

        issuer2 = getIssuerSample2();
        assertThat(issuer1).isNotEqualTo(issuer2);
    }

    @Test
    void cardTest() {
        Issuer issuer = getIssuerRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        issuer.addCard(cardBack);
        assertThat(issuer.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getIssuer()).isEqualTo(issuer);

        issuer.removeCard(cardBack);
        assertThat(issuer.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getIssuer()).isNull();

        issuer.cards(new HashSet<>(Set.of(cardBack)));
        assertThat(issuer.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getIssuer()).isEqualTo(issuer);

        issuer.setCards(new HashSet<>());
        assertThat(issuer.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getIssuer()).isNull();
    }
}
