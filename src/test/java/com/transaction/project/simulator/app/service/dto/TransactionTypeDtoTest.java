package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTypeDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionTypeDto.class);
        TransactionTypeDto transactionTypeDto1 = new TransactionTypeDto();
        transactionTypeDto1.setId(1L);
        TransactionTypeDto transactionTypeDto2 = new TransactionTypeDto();
        assertThat(transactionTypeDto1).isNotEqualTo(transactionTypeDto2);
        transactionTypeDto2.setId(transactionTypeDto1.getId());
        assertThat(transactionTypeDto1).isEqualTo(transactionTypeDto2);
        transactionTypeDto2.setId(2L);
        assertThat(transactionTypeDto1).isNotEqualTo(transactionTypeDto2);
        transactionTypeDto1.setId(null);
        assertThat(transactionTypeDto1).isNotEqualTo(transactionTypeDto2);
    }
}
