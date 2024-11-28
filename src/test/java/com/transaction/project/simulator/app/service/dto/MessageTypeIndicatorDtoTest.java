package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTypeIndicatorDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageTypeIndicatorDto.class);
        MessageTypeIndicatorDto messageTypeIndicatorDto1 = new MessageTypeIndicatorDto();
        messageTypeIndicatorDto1.setId(1L);
        MessageTypeIndicatorDto messageTypeIndicatorDto2 = new MessageTypeIndicatorDto();
        assertThat(messageTypeIndicatorDto1).isNotEqualTo(messageTypeIndicatorDto2);
        messageTypeIndicatorDto2.setId(messageTypeIndicatorDto1.getId());
        assertThat(messageTypeIndicatorDto1).isEqualTo(messageTypeIndicatorDto2);
        messageTypeIndicatorDto2.setId(2L);
        assertThat(messageTypeIndicatorDto1).isNotEqualTo(messageTypeIndicatorDto2);
        messageTypeIndicatorDto1.setId(null);
        assertThat(messageTypeIndicatorDto1).isNotEqualTo(messageTypeIndicatorDto2);
    }
}
