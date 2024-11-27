package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Acquirer} and its DTO {@link AcquirerDto}.
 */
@Mapper(componentModel = "spring")
public interface AcquirerMapper extends EntityMapper<AcquirerDto, Acquirer> {}
