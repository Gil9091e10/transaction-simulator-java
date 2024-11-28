package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FieldTypeDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTypeDto.class);
        FieldTypeDto fieldTypeDto1 = new FieldTypeDto();
        fieldTypeDto1.setId(1L);
        FieldTypeDto fieldTypeDto2 = new FieldTypeDto();
        assertThat(fieldTypeDto1).isNotEqualTo(fieldTypeDto2);
        fieldTypeDto2.setId(fieldTypeDto1.getId());
        assertThat(fieldTypeDto1).isEqualTo(fieldTypeDto2);
        fieldTypeDto2.setId(2L);
        assertThat(fieldTypeDto1).isNotEqualTo(fieldTypeDto2);
        fieldTypeDto1.setId(null);
        assertThat(fieldTypeDto1).isNotEqualTo(fieldTypeDto2);
    }
}
