package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.DebitCard}.
 */
public interface DebitCardService {
    /**
     * Save a debitCard.
     *
     * @param debitCardDto the entity to save.
     * @return the persisted entity.
     */
    DebitCardDto save(DebitCardDto debitCardDto);

    /**
     * Updates a debitCard.
     *
     * @param debitCardDto the entity to update.
     * @return the persisted entity.
     */
    DebitCardDto update(DebitCardDto debitCardDto);

    /**
     * Partially updates a debitCard.
     *
     * @param debitCardDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DebitCardDto> partialUpdate(DebitCardDto debitCardDto);

    /**
     * Get the "id" debitCard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DebitCardDto> findOne(Long id);

    /**
     * Delete the "id" debitCard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
