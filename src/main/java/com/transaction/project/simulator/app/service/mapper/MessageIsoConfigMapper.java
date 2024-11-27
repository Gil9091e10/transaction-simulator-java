package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageIsoConfig} and its DTO {@link MessageIsoConfigDto}.
 */
@Mapper(componentModel = "spring")
public interface MessageIsoConfigMapper extends EntityMapper<MessageIsoConfigDto, MessageIsoConfig> {
    @Mapping(target = "acquirer", source = "acquirer", qualifiedByName = "acquirerId")
    @Mapping(target = "messageTypeIndicator", source = "messageTypeIndicator", qualifiedByName = "messageTypeIndicatorId")
    MessageIsoConfigDto toDto(MessageIsoConfig s);

    @Named("acquirerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AcquirerDto toDtoAcquirerId(Acquirer acquirer);

    @Named("messageTypeIndicatorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MessageTypeIndicatorDto toDtoMessageTypeIndicatorId(MessageTypeIndicator messageTypeIndicator);
}
