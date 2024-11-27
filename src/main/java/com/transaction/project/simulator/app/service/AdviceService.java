package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.AdviceDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.Advice}.
 */
public interface AdviceService {
    /**
     * Save a advice.
     *
     * @param adviceDto the entity to save.
     * @return the persisted entity.
     */
    AdviceDto save(AdviceDto adviceDto);

    /**
     * Updates a advice.
     *
     * @param adviceDto the entity to update.
     * @return the persisted entity.
     */
    AdviceDto update(AdviceDto adviceDto);

    /**
     * Partially updates a advice.
     *
     * @param adviceDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdviceDto> partialUpdate(AdviceDto adviceDto);

    /**
     * Get the "id" advice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdviceDto> findOne(Long id);

    /**
     * Delete the "id" advice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
