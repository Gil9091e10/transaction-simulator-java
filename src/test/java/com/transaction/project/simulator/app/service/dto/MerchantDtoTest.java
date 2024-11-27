package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MerchantDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MerchantDto.class);
        MerchantDto merchantDto1 = new MerchantDto();
        merchantDto1.setId(1L);
        MerchantDto merchantDto2 = new MerchantDto();
        assertThat(merchantDto1).isNotEqualTo(merchantDto2);
        merchantDto2.setId(merchantDto1.getId());
        assertThat(merchantDto1).isEqualTo(merchantDto2);
        merchantDto2.setId(2L);
        assertThat(merchantDto1).isNotEqualTo(merchantDto2);
        merchantDto1.setId(null);
        assertThat(merchantDto1).isNotEqualTo(merchantDto2);
    }
}
