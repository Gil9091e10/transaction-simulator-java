package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.domain.MessageFieldsConfig;
import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageFieldsConfig} and its DTO {@link MessageFieldsConfigDto}.
 */
@Mapper(componentModel = "spring")
public interface MessageFieldsConfigMapper extends EntityMapper<MessageFieldsConfigDto, MessageFieldsConfig> {
    @Mapping(target = "messageIsoConfig", source = "messageIsoConfig", qualifiedByName = "messageIsoConfigId")
    @Mapping(target = "messageFieldType", source = "messageFieldType", qualifiedByName = "messageFieldTypeId")
    MessageFieldsConfigDto toDto(MessageFieldsConfig s);

    @Named("messageIsoConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MessageIsoConfigDto toDtoMessageIsoConfigId(MessageIsoConfig messageIsoConfig);

    @Named("messageFieldTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MessageFieldTypeDto toDtoMessageFieldTypeId(MessageFieldType messageFieldType);
}
