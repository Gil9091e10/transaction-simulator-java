package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.FieldType}.
 */
public interface FieldTypeService {
    /**
     * Save a fieldType.
     *
     * @param fieldTypeDto the entity to save.
     * @return the persisted entity.
     */
    FieldTypeDto save(FieldTypeDto fieldTypeDto);

    /**
     * Updates a fieldType.
     *
     * @param fieldTypeDto the entity to update.
     * @return the persisted entity.
     */
    FieldTypeDto update(FieldTypeDto fieldTypeDto);

    /**
     * Partially updates a fieldType.
     *
     * @param fieldTypeDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FieldTypeDto> partialUpdate(FieldTypeDto fieldTypeDto);

    /**
     * Get the "id" fieldType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FieldTypeDto> findOne(Long id);

    /**
     * Delete the "id" fieldType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
