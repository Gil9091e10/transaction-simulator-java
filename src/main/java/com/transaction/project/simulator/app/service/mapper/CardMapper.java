package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.domain.Card;
import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import com.transaction.project.simulator.app.service.dto.CardDto;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDto}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDto, Card> {
    @Mapping(target = "accountBank", source = "accountBank", qualifiedByName = "accountBankId")
    @Mapping(target = "cardType", source = "cardType", qualifiedByName = "cardTypeId")
    @Mapping(target = "issuer", source = "issuer", qualifiedByName = "issuerId")
    CardDto toDto(Card s);

    @Named("accountBankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountBankDto toDtoAccountBankId(AccountBank accountBank);

    @Named("cardTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardTypeDto toDtoCardTypeId(CardType cardType);

    @Named("issuerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IssuerDto toDtoIssuerId(Issuer issuer);
}
