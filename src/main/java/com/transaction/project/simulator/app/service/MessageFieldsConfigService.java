package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.MessageFieldsConfig}.
 */
public interface MessageFieldsConfigService {
    /**
     * Save a messageFieldsConfig.
     *
     * @param messageFieldsConfigDto the entity to save.
     * @return the persisted entity.
     */
    MessageFieldsConfigDto save(MessageFieldsConfigDto messageFieldsConfigDto);

    /**
     * Updates a messageFieldsConfig.
     *
     * @param messageFieldsConfigDto the entity to update.
     * @return the persisted entity.
     */
    MessageFieldsConfigDto update(MessageFieldsConfigDto messageFieldsConfigDto);

    /**
     * Partially updates a messageFieldsConfig.
     *
     * @param messageFieldsConfigDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MessageFieldsConfigDto> partialUpdate(MessageFieldsConfigDto messageFieldsConfigDto);

    /**
     * Get the "id" messageFieldsConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageFieldsConfigDto> findOne(Long id);

    /**
     * Delete the "id" messageFieldsConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
