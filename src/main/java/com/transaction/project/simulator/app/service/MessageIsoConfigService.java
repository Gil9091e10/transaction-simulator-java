package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.MessageIsoConfig}.
 */
public interface MessageIsoConfigService {
    /**
     * Save a messageIsoConfig.
     *
     * @param messageIsoConfigDto the entity to save.
     * @return the persisted entity.
     */
    MessageIsoConfigDto save(MessageIsoConfigDto messageIsoConfigDto);

    /**
     * Updates a messageIsoConfig.
     *
     * @param messageIsoConfigDto the entity to update.
     * @return the persisted entity.
     */
    MessageIsoConfigDto update(MessageIsoConfigDto messageIsoConfigDto);

    /**
     * Partially updates a messageIsoConfig.
     *
     * @param messageIsoConfigDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MessageIsoConfigDto> partialUpdate(MessageIsoConfigDto messageIsoConfigDto);

    /**
     * Get the "id" messageIsoConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageIsoConfigDto> findOne(Long id);

    /**
     * Delete the "id" messageIsoConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
