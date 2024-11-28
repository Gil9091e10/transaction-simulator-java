package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageTypeIndicator} and its DTO {@link MessageTypeIndicatorDto}.
 */
@Mapper(componentModel = "spring")
public interface MessageTypeIndicatorMapper extends EntityMapper<MessageTypeIndicatorDto, MessageTypeIndicator> {}
