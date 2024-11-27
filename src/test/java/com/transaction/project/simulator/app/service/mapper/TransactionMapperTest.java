package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.TransactionAsserts.*;
import static com.transaction.project.simulator.app.domain.TransactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransactionSample1();
        var actual = transactionMapper.toEntity(transactionMapper.toDto(expected));
        assertTransactionAllPropertiesEquals(expected, actual);
    }
}
