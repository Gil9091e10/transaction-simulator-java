package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.DebitCard;
import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DebitCard} and its DTO {@link DebitCardDto}.
 */
@Mapper(componentModel = "spring")
public interface DebitCardMapper extends EntityMapper<DebitCardDto, DebitCard> {}
