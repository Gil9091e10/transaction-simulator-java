package com.transaction.project.simulator.app.service.mapper;

import static com.transaction.project.simulator.app.domain.AccountBankAsserts.*;
import static com.transaction.project.simulator.app.domain.AccountBankTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountBankMapperTest {

    private AccountBankMapper accountBankMapper;

    @BeforeEach
    void setUp() {
        accountBankMapper = new AccountBankMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccountBankSample1();
        var actual = accountBankMapper.toEntity(accountBankMapper.toDto(expected));
        assertAccountBankAllPropertiesEquals(expected, actual);
    }
}
