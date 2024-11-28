package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageIsoConfigDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageIsoConfigDto.class);
        MessageIsoConfigDto messageIsoConfigDto1 = new MessageIsoConfigDto();
        messageIsoConfigDto1.setId(1L);
        MessageIsoConfigDto messageIsoConfigDto2 = new MessageIsoConfigDto();
        assertThat(messageIsoConfigDto1).isNotEqualTo(messageIsoConfigDto2);
        messageIsoConfigDto2.setId(messageIsoConfigDto1.getId());
        assertThat(messageIsoConfigDto1).isEqualTo(messageIsoConfigDto2);
        messageIsoConfigDto2.setId(2L);
        assertThat(messageIsoConfigDto1).isNotEqualTo(messageIsoConfigDto2);
        messageIsoConfigDto1.setId(null);
        assertThat(messageIsoConfigDto1).isNotEqualTo(messageIsoConfigDto2);
    }
}
