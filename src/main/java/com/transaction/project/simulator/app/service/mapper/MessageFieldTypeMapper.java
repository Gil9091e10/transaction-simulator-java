package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageFieldType} and its DTO {@link MessageFieldTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface MessageFieldTypeMapper extends EntityMapper<MessageFieldTypeDto, MessageFieldType> {
    @Mapping(target = "fieldType", source = "fieldType", qualifiedByName = "fieldTypeId")
    MessageFieldTypeDto toDto(MessageFieldType s);

    @Named("fieldTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FieldTypeDto toDtoFieldTypeId(FieldType fieldType);
}
