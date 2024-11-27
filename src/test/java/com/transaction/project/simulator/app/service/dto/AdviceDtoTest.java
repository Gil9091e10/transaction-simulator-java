package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdviceDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdviceDto.class);
        AdviceDto adviceDto1 = new AdviceDto();
        adviceDto1.setId(1L);
        AdviceDto adviceDto2 = new AdviceDto();
        assertThat(adviceDto1).isNotEqualTo(adviceDto2);
        adviceDto2.setId(adviceDto1.getId());
        assertThat(adviceDto1).isEqualTo(adviceDto2);
        adviceDto2.setId(2L);
        assertThat(adviceDto1).isNotEqualTo(adviceDto2);
        adviceDto1.setId(null);
        assertThat(adviceDto1).isNotEqualTo(adviceDto2);
    }
}
