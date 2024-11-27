package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.TransactionTestSamples.*;
import static com.transaction.project.simulator.app.domain.TransactionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void transactionTypeTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        TransactionType transactionTypeBack = getTransactionTypeRandomSampleGenerator();

        transaction.setTransactionType(transactionTypeBack);
        assertThat(transaction.getTransactionType()).isEqualTo(transactionTypeBack);

        transaction.transactionType(null);
        assertThat(transaction.getTransactionType()).isNull();
    }
}