package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DebitCardDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DebitCardDto.class);
        DebitCardDto debitCardDto1 = new DebitCardDto();
        debitCardDto1.setId(1L);
        DebitCardDto debitCardDto2 = new DebitCardDto();
        assertThat(debitCardDto1).isNotEqualTo(debitCardDto2);
        debitCardDto2.setId(debitCardDto1.getId());
        assertThat(debitCardDto1).isEqualTo(debitCardDto2);
        debitCardDto2.setId(2L);
        assertThat(debitCardDto1).isNotEqualTo(debitCardDto2);
        debitCardDto1.setId(null);
        assertThat(debitCardDto1).isNotEqualTo(debitCardDto2);
    }
}
