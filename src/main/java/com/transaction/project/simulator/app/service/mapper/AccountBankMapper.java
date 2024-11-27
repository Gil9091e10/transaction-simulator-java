package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountBank} and its DTO {@link AccountBankDto}.
 */
@Mapper(componentModel = "spring")
public interface AccountBankMapper extends EntityMapper<AccountBankDto, AccountBank> {
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    AccountBankDto toDto(AccountBank s);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDto toDtoCurrencyId(Currency currency);
}
