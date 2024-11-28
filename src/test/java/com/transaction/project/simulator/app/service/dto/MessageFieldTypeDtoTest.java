package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageFieldTypeDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageFieldTypeDto.class);
        MessageFieldTypeDto messageFieldTypeDto1 = new MessageFieldTypeDto();
        messageFieldTypeDto1.setId(1L);
        MessageFieldTypeDto messageFieldTypeDto2 = new MessageFieldTypeDto();
        assertThat(messageFieldTypeDto1).isNotEqualTo(messageFieldTypeDto2);
        messageFieldTypeDto2.setId(messageFieldTypeDto1.getId());
        assertThat(messageFieldTypeDto1).isEqualTo(messageFieldTypeDto2);
        messageFieldTypeDto2.setId(2L);
        assertThat(messageFieldTypeDto1).isNotEqualTo(messageFieldTypeDto2);
        messageFieldTypeDto1.setId(null);
        assertThat(messageFieldTypeDto1).isNotEqualTo(messageFieldTypeDto2);
    }
}
