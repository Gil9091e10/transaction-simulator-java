package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.TransactionType;
import com.transaction.project.simulator.app.repository.TransactionTypeRepository;
import com.transaction.project.simulator.app.service.TransactionTypeService;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import com.transaction.project.simulator.app.service.mapper.TransactionTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeServiceImpl.class);

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeMapper transactionTypeMapper;

    public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    @Override
    public TransactionTypeDto save(TransactionTypeDto transactionTypeDto) {
        LOG.debug("Request to save TransactionType : {}", transactionTypeDto);
        TransactionType transactionType = transactionTypeMapper.toEntity(transactionTypeDto);
        transactionType = transactionTypeRepository.save(transactionType);
        return transactionTypeMapper.toDto(transactionType);
    }

    @Override
    public TransactionTypeDto update(TransactionTypeDto transactionTypeDto) {
        LOG.debug("Request to update TransactionType : {}", transactionTypeDto);
        TransactionType transactionType = transactionTypeMapper.toEntity(transactionTypeDto);
        transactionType = transactionTypeRepository.save(transactionType);
        return transactionTypeMapper.toDto(transactionType);
    }

    @Override
    public Optional<TransactionTypeDto> partialUpdate(TransactionTypeDto transactionTypeDto) {
        LOG.debug("Request to partially update TransactionType : {}", transactionTypeDto);

        return transactionTypeRepository
            .findById(transactionTypeDto.getId())
            .map(existingTransactionType -> {
                transactionTypeMapper.partialUpdate(existingTransactionType, transactionTypeDto);

                return existingTransactionType;
            })
            .map(transactionTypeRepository::save)
            .map(transactionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionTypeDto> findOne(Long id) {
        LOG.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id).map(transactionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TransactionType : {}", id);
        transactionTypeRepository.deleteById(id);
    }
}
