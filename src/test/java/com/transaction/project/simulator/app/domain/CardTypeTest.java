package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.CardTestSamples.*;
import static com.transaction.project.simulator.app.domain.CardTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CardTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardType.class);
        CardType cardType1 = getCardTypeSample1();
        CardType cardType2 = new CardType();
        assertThat(cardType1).isNotEqualTo(cardType2);

        cardType2.setId(cardType1.getId());
        assertThat(cardType1).isEqualTo(cardType2);

        cardType2 = getCardTypeSample2();
        assertThat(cardType1).isNotEqualTo(cardType2);
    }

    @Test
    void cardTest() {
        CardType cardType = getCardTypeRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        cardType.addCard(cardBack);
        assertThat(cardType.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getCardType()).isEqualTo(cardType);

        cardType.removeCard(cardBack);
        assertThat(cardType.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getCardType()).isNull();

        cardType.cards(new HashSet<>(Set.of(cardBack)));
        assertThat(cardType.getCards()).containsOnly(cardBack);
        assertThat(cardBack.getCardType()).isEqualTo(cardType);

        cardType.setCards(new HashSet<>());
        assertThat(cardType.getCards()).doesNotContain(cardBack);
        assertThat(cardBack.getCardType()).isNull();
    }
}
