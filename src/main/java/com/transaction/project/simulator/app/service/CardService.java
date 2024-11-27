package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.CardDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Card}.
 */
public interface CardService {
    /**
     * Save a card.
     *
     * @param cardDto the entity to save.
     * @return the persisted entity.
     */
    CardDto save(CardDto cardDto);

    /**
     * Updates a card.
     *
     * @param cardDto the entity to update.
     * @return the persisted entity.
     */
    CardDto update(CardDto cardDto);

    /**
     * Partially updates a card.
     *
     * @param cardDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardDto> partialUpdate(CardDto cardDto);

    /**
     * Get the "id" card.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardDto> findOne(Long id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
