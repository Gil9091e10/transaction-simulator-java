package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Acquirer}.
 */
public interface AcquirerService {
    /**
     * Save a acquirer.
     *
     * @param acquirerDto the entity to save.
     * @return the persisted entity.
     */
    AcquirerDto save(AcquirerDto acquirerDto);

    /**
     * Updates a acquirer.
     *
     * @param acquirerDto the entity to update.
     * @return the persisted entity.
     */
    AcquirerDto update(AcquirerDto acquirerDto);

    /**
     * Partially updates a acquirer.
     *
     * @param acquirerDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AcquirerDto> partialUpdate(AcquirerDto acquirerDto);

    /**
     * Get the "id" acquirer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AcquirerDto> findOne(Long id);

    /**
     * Delete the "id" acquirer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
