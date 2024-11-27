package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcquirerDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcquirerDto.class);
        AcquirerDto acquirerDto1 = new AcquirerDto();
        acquirerDto1.setId(1L);
        AcquirerDto acquirerDto2 = new AcquirerDto();
        assertThat(acquirerDto1).isNotEqualTo(acquirerDto2);
        acquirerDto2.setId(acquirerDto1.getId());
        assertThat(acquirerDto1).isEqualTo(acquirerDto2);
        acquirerDto2.setId(2L);
        assertThat(acquirerDto1).isNotEqualTo(acquirerDto2);
        acquirerDto1.setId(null);
        assertThat(acquirerDto1).isNotEqualTo(acquirerDto2);
    }
}
