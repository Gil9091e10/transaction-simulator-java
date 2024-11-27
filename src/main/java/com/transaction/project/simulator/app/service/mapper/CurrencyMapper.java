package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDto}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDto, Currency> {}
