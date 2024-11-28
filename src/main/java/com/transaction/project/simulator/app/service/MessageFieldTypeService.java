package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.MessageFieldType}.
 */
public interface MessageFieldTypeService {
    /**
     * Save a messageFieldType.
     *
     * @param messageFieldTypeDto the entity to save.
     * @return the persisted entity.
     */
    MessageFieldTypeDto save(MessageFieldTypeDto messageFieldTypeDto);

    /**
     * Updates a messageFieldType.
     *
     * @param messageFieldTypeDto the entity to update.
     * @return the persisted entity.
     */
    MessageFieldTypeDto update(MessageFieldTypeDto messageFieldTypeDto);

    /**
     * Partially updates a messageFieldType.
     *
     * @param messageFieldTypeDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MessageFieldTypeDto> partialUpdate(MessageFieldTypeDto messageFieldTypeDto);

    /**
     * Get the "id" messageFieldType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageFieldTypeDto> findOne(Long id);

    /**
     * Delete the "id" messageFieldType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
