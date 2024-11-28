package com.transaction.project.simulator.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.transaction.project.simulator.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBankDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBankDto.class);
        AccountBankDto accountBankDto1 = new AccountBankDto();
        accountBankDto1.setId(1L);
        AccountBankDto accountBankDto2 = new AccountBankDto();
        assertThat(accountBankDto1).isNotEqualTo(accountBankDto2);
        accountBankDto2.setId(accountBankDto1.getId());
        assertThat(accountBankDto1).isEqualTo(accountBankDto2);
        accountBankDto2.setId(2L);
        assertThat(accountBankDto1).isNotEqualTo(accountBankDto2);
        accountBankDto1.setId(null);
        assertThat(accountBankDto1).isNotEqualTo(accountBankDto2);
    }
}
