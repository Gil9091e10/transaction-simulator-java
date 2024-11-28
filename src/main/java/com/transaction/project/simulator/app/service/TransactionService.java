package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.TransactionDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Transaction}.
 */
public interface TransactionService {
    /**
     * Save a transaction.
     *
     * @param transactionDto the entity to save.
     * @return the persisted entity.
     */
    TransactionDto save(TransactionDto transactionDto);

    /**
     * Updates a transaction.
     *
     * @param transactionDto the entity to update.
     * @return the persisted entity.
     */
    TransactionDto update(TransactionDto transactionDto);

    /**
     * Partially updates a transaction.
     *
     * @param transactionDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionDto> partialUpdate(TransactionDto transactionDto);

    /**
     * Get the "id" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionDto> findOne(Long id);

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
