package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.CardType}.
 */
public interface CardTypeService {
    /**
     * Save a cardType.
     *
     * @param cardTypeDto the entity to save.
     * @return the persisted entity.
     */
    CardTypeDto save(CardTypeDto cardTypeDto);

    /**
     * Updates a cardType.
     *
     * @param cardTypeDto the entity to update.
     * @return the persisted entity.
     */
    CardTypeDto update(CardTypeDto cardTypeDto);

    /**
     * Partially updates a cardType.
     *
     * @param cardTypeDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardTypeDto> partialUpdate(CardTypeDto cardTypeDto);

    /**
     * Get the "id" cardType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardTypeDto> findOne(Long id);

    /**
     * Delete the "id" cardType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
