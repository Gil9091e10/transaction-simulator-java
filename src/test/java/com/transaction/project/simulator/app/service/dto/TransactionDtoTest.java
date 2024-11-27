package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDto.class);
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setId(1L);
        TransactionDto transactionDto2 = new TransactionDto();
        assertThat(transactionDto1).isNotEqualTo(transactionDto2);
        transactionDto2.setId(transactionDto1.getId());
        assertThat(transactionDto1).isEqualTo(transactionDto2);
        transactionDto2.setId(2L);
        assertThat(transactionDto1).isNotEqualTo(transactionDto2);
        transactionDto1.setId(null);
        assertThat(transactionDto1).isNotEqualTo(transactionDto2);
    }
}
