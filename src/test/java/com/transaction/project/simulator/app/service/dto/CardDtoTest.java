package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardDto.class);
        CardDto cardDto1 = new CardDto();
        cardDto1.setId(1L);
        CardDto cardDto2 = new CardDto();
        assertThat(cardDto1).isNotEqualTo(cardDto2);
        cardDto2.setId(cardDto1.getId());
        assertThat(cardDto1).isEqualTo(cardDto2);
        cardDto2.setId(2L);
        assertThat(cardDto1).isNotEqualTo(cardDto2);
        cardDto1.setId(null);
        assertThat(cardDto1).isNotEqualTo(cardDto2);
    }
}
