package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.repository.FieldTypeRepository;
import com.transaction.project.simulator.app.service.FieldTypeService;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.FieldTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.FieldType}.
 */
@Service
@Transactional
public class FieldTypeServiceImpl implements FieldTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTypeServiceImpl.class);

    private final FieldTypeRepository fieldTypeRepository;

    private final FieldTypeMapper fieldTypeMapper;

    public FieldTypeServiceImpl(FieldTypeRepository fieldTypeRepository, FieldTypeMapper fieldTypeMapper) {
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldTypeMapper = fieldTypeMapper;
    }

    @Override
    public FieldTypeDto save(FieldTypeDto fieldTypeDto) {
        LOG.debug("Request to save FieldType : {}", fieldTypeDto);
        FieldType fieldType = fieldTypeMapper.toEntity(fieldTypeDto);
        fieldType = fieldTypeRepository.save(fieldType);
        return fieldTypeMapper.toDto(fieldType);
    }

    @Override
    public FieldTypeDto update(FieldTypeDto fieldTypeDto) {
        LOG.debug("Request to update FieldType : {}", fieldTypeDto);
        FieldType fieldType = fieldTypeMapper.toEntity(fieldTypeDto);
        fieldType = fieldTypeRepository.save(fieldType);
        return fieldTypeMapper.toDto(fieldType);
    }

    @Override
    public Optional<FieldTypeDto> partialUpdate(FieldTypeDto fieldTypeDto) {
        LOG.debug("Request to partially update FieldType : {}", fieldTypeDto);

        return fieldTypeRepository
            .findById(fieldTypeDto.getId())
            .map(existingFieldType -> {
                fieldTypeMapper.partialUpdate(existingFieldType, fieldTypeDto);

                return existingFieldType;
            })
            .map(fieldTypeRepository::save)
            .map(fieldTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldTypeDto> findOne(Long id) {
        LOG.debug("Request to get FieldType : {}", id);
        return fieldTypeRepository.findById(id).map(fieldTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FieldType : {}", id);
        fieldTypeRepository.deleteById(id);
    }
}
