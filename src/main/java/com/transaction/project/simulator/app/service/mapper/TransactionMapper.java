package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Transaction;
import com.transaction.project.simulator.app.domain.TransactionType;
import com.transaction.project.simulator.app.service.dto.TransactionDto;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDto}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDto, Transaction> {
    @Mapping(target = "transactionType", source = "transactionType", qualifiedByName = "transactionTypeId")
    TransactionDto toDto(Transaction s);

    @Named("transactionTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransactionTypeDto toDtoTransactionTypeId(TransactionType transactionType);
}
