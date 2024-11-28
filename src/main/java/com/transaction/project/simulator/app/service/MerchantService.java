package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.MerchantDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Merchant}.
 */
public interface MerchantService {
    /**
     * Save a merchant.
     *
     * @param merchantDto the entity to save.
     * @return the persisted entity.
     */
    MerchantDto save(MerchantDto merchantDto);

    /**
     * Updates a merchant.
     *
     * @param merchantDto the entity to update.
     * @return the persisted entity.
     */
    MerchantDto update(MerchantDto merchantDto);

    /**
     * Partially updates a merchant.
     *
     * @param merchantDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MerchantDto> partialUpdate(MerchantDto merchantDto);

    /**
     * Get the "id" merchant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MerchantDto> findOne(Long id);

    /**
     * Delete the "id" merchant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
