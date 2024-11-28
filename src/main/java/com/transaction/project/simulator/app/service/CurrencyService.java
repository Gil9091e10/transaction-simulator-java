package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Currency}.
 */
public interface CurrencyService {
    /**
     * Save a currency.
     *
     * @param currencyDto the entity to save.
     * @return the persisted entity.
     */
    CurrencyDto save(CurrencyDto currencyDto);

    /**
     * Updates a currency.
     *
     * @param currencyDto the entity to update.
     * @return the persisted entity.
     */
    CurrencyDto update(CurrencyDto currencyDto);

    /**
     * Partially updates a currency.
     *
     * @param currencyDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CurrencyDto> partialUpdate(CurrencyDto currencyDto);

    /**
     * Get the "id" currency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CurrencyDto> findOne(Long id);

    /**
     * Delete the "id" currency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
