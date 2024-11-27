package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.MessageTypeIndicator}.
 */
public interface MessageTypeIndicatorService {
    /**
     * Save a messageTypeIndicator.
     *
     * @param messageTypeIndicatorDto the entity to save.
     * @return the persisted entity.
     */
    MessageTypeIndicatorDto save(MessageTypeIndicatorDto messageTypeIndicatorDto);

    /**
     * Updates a messageTypeIndicator.
     *
     * @param messageTypeIndicatorDto the entity to update.
     * @return the persisted entity.
     */
    MessageTypeIndicatorDto update(MessageTypeIndicatorDto messageTypeIndicatorDto);

    /**
     * Partially updates a messageTypeIndicator.
     *
     * @param messageTypeIndicatorDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MessageTypeIndicatorDto> partialUpdate(MessageTypeIndicatorDto messageTypeIndicatorDto);

    /**
     * Get the "id" messageTypeIndicator.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageTypeIndicatorDto> findOne(Long id);

    /**
     * Delete the "id" messageTypeIndicator.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
