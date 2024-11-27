package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.TransactionType}.
 */
public interface TransactionTypeService {
    /**
     * Save a transactionType.
     *
     * @param transactionTypeDto the entity to save.
     * @return the persisted entity.
     */
    TransactionTypeDto save(TransactionTypeDto transactionTypeDto);

    /**
     * Updates a transactionType.
     *
     * @param transactionTypeDto the entity to update.
     * @return the persisted entity.
     */
    TransactionTypeDto update(TransactionTypeDto transactionTypeDto);

    /**
     * Partially updates a transactionType.
     *
     * @param transactionTypeDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionTypeDto> partialUpdate(TransactionTypeDto transactionTypeDto);

    /**
     * Get the "id" transactionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionTypeDto> findOne(Long id);

    /**
     * Delete the "id" transactionType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
