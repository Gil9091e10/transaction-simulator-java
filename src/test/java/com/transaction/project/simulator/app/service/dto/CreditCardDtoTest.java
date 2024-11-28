package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CreditCardDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditCardDto.class);
        CreditCardDto creditCardDto1 = new CreditCardDto();
        creditCardDto1.setId(1L);
        CreditCardDto creditCardDto2 = new CreditCardDto();
        assertThat(creditCardDto1).isNotEqualTo(creditCardDto2);
        creditCardDto2.setId(creditCardDto1.getId());
        assertThat(creditCardDto1).isEqualTo(creditCardDto2);
        creditCardDto2.setId(2L);
        assertThat(creditCardDto1).isNotEqualTo(creditCardDto2);
        creditCardDto1.setId(null);
        assertThat(creditCardDto1).isNotEqualTo(creditCardDto2);
    }
}
