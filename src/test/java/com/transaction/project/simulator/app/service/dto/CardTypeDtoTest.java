package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardTypeDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardTypeDto.class);
        CardTypeDto cardTypeDto1 = new CardTypeDto();
        cardTypeDto1.setId(1L);
        CardTypeDto cardTypeDto2 = new CardTypeDto();
        assertThat(cardTypeDto1).isNotEqualTo(cardTypeDto2);
        cardTypeDto2.setId(cardTypeDto1.getId());
        assertThat(cardTypeDto1).isEqualTo(cardTypeDto2);
        cardTypeDto2.setId(2L);
        assertThat(cardTypeDto1).isNotEqualTo(cardTypeDto2);
        cardTypeDto1.setId(null);
        assertThat(cardTypeDto1).isNotEqualTo(cardTypeDto2);
    }
}
