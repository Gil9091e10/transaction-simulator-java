package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.CreditCardDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.CreditCard}.
 */
public interface CreditCardService {
    /**
     * Save a creditCard.
     *
     * @param creditCardDto the entity to save.
     * @return the persisted entity.
     */
    CreditCardDto save(CreditCardDto creditCardDto);

    /**
     * Updates a creditCard.
     *
     * @param creditCardDto the entity to update.
     * @return the persisted entity.
     */
    CreditCardDto update(CreditCardDto creditCardDto);

    /**
     * Partially updates a creditCard.
     *
     * @param creditCardDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CreditCardDto> partialUpdate(CreditCardDto creditCardDto);

    /**
     * Get the "id" creditCard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CreditCardDto> findOne(Long id);

    /**
     * Delete the "id" creditCard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
