package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.TransactionType;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionType} and its DTO {@link TransactionTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface TransactionTypeMapper extends EntityMapper<TransactionTypeDto, TransactionType> {}
