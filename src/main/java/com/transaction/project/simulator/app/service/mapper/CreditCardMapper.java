package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.CreditCard;
import com.transaction.project.simulator.app.service.dto.CreditCardDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CreditCard} and its DTO {@link CreditCardDto}.
 */
@Mapper(componentModel = "spring")
public interface CreditCardMapper extends EntityMapper<CreditCardDto, CreditCard> {}
