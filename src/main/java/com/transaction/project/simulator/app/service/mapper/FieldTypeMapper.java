package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FieldType} and its DTO {@link FieldTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface FieldTypeMapper extends EntityMapper<FieldTypeDto, FieldType> {}
