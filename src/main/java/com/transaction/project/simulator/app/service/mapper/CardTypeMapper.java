package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CardType} and its DTO {@link CardTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface CardTypeMapper extends EntityMapper<CardTypeDto, CardType> {}
