package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssuerDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssuerDto.class);
        IssuerDto issuerDto1 = new IssuerDto();
        issuerDto1.setId(1L);
        IssuerDto issuerDto2 = new IssuerDto();
        assertThat(issuerDto1).isNotEqualTo(issuerDto2);
        issuerDto2.setId(issuerDto1.getId());
        assertThat(issuerDto1).isEqualTo(issuerDto2);
        issuerDto2.setId(2L);
        assertThat(issuerDto1).isNotEqualTo(issuerDto2);
        issuerDto1.setId(null);
        assertThat(issuerDto1).isNotEqualTo(issuerDto2);
    }
}
