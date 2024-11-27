package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.IssuerDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Issuer}.
 */
public interface IssuerService {
    /**
     * Save a issuer.
     *
     * @param issuerDto the entity to save.
     * @return the persisted entity.
     */
    IssuerDto save(IssuerDto issuerDto);

    /**
     * Updates a issuer.
     *
     * @param issuerDto the entity to update.
     * @return the persisted entity.
     */
    IssuerDto update(IssuerDto issuerDto);

    /**
     * Partially updates a issuer.
     *
     * @param issuerDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IssuerDto> partialUpdate(IssuerDto issuerDto);

    /**
     * Get the "id" issuer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IssuerDto> findOne(Long id);

    /**
     * Delete the "id" issuer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
