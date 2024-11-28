package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageFieldsConfigDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageFieldsConfigDto.class);
        MessageFieldsConfigDto messageFieldsConfigDto1 = new MessageFieldsConfigDto();
        messageFieldsConfigDto1.setId(1L);
        MessageFieldsConfigDto messageFieldsConfigDto2 = new MessageFieldsConfigDto();
        assertThat(messageFieldsConfigDto1).isNotEqualTo(messageFieldsConfigDto2);
        messageFieldsConfigDto2.setId(messageFieldsConfigDto1.getId());
        assertThat(messageFieldsConfigDto1).isEqualTo(messageFieldsConfigDto2);
        messageFieldsConfigDto2.setId(2L);
        assertThat(messageFieldsConfigDto1).isNotEqualTo(messageFieldsConfigDto2);
        messageFieldsConfigDto1.setId(null);
        assertThat(messageFieldsConfigDto1).isNotEqualTo(messageFieldsConfigDto2);
    }
}
