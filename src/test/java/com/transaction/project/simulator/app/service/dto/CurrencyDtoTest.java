package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrencyDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyDto.class);
        CurrencyDto currencyDto1 = new CurrencyDto();
        currencyDto1.setId(1L);
        CurrencyDto currencyDto2 = new CurrencyDto();
        assertThat(currencyDto1).isNotEqualTo(currencyDto2);
        currencyDto2.setId(currencyDto1.getId());
        assertThat(currencyDto1).isEqualTo(currencyDto2);
        currencyDto2.setId(2L);
        assertThat(currencyDto1).isNotEqualTo(currencyDto2);
        currencyDto1.setId(null);
        assertThat(currencyDto1).isNotEqualTo(currencyDto2);
    }
}
