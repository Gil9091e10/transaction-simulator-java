package com.transaction.project.simulator.app.domain;

import static com.transaction.project.simulator.app.domain.TransactionTestSamples.*;
import static com.transaction.project.simulator.app.domain.TransactionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransactionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionType.class);
        TransactionType transactionType1 = getTransactionTypeSample1();
        TransactionType transactionType2 = new TransactionType();
        assertThat(transactionType1).isNotEqualTo(transactionType2);

        transactionType2.setId(transactionType1.getId());
        assertThat(transactionType1).isEqualTo(transactionType2);

        transactionType2 = getTransactionTypeSample2();
        assertThat(transactionType1).isNotEqualTo(transactionType2);
    }

    @Test
    void transactionTest() {
        TransactionType transactionType = getTransactionTypeRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        transactionType.addTransaction(transactionBack);
        assertThat(transactionType.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getTransactionType()).isEqualTo(transactionType);

        transactionType.removeTransaction(transactionBack);
        assertThat(transactionType.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getTransactionType()).isNull();

        transactionType.transactions(new HashSet<>(Set.of(transactionBack)));
        assertThat(transactionType.getTransactions()).containsOnly(transactionBack);
        assertThat(transactionBack.getTransactionType()).isEqualTo(transactionType);

        transactionType.setTransactions(new HashSet<>());
        assertThat(transactionType.getTransactions()).doesNotContain(transactionBack);
        assertThat(transactionBack.getTransactionType()).isNull();
    }
}
