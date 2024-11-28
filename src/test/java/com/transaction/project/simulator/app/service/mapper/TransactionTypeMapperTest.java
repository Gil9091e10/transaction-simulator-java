package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.TransactionTypeAsserts.*;
import static com.transaction.project.simulator.app.domain.TransactionTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionTypeMapperTest {

    private TransactionTypeMapper transactionTypeMapper;

    @BeforeEach
    void setUp() {
        transactionTypeMapper = new TransactionTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransactionTypeSample1();
        var actual = transactionTypeMapper.toEntity(transactionTypeMapper.toDto(expected));
        assertTransactionTypeAllPropertiesEquals(expected, actual);
    }
}
