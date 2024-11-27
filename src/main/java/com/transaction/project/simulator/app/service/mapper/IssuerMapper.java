package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Issuer} and its DTO {@link IssuerDto}.
 */
@Mapper(componentModel = "spring")
public interface IssuerMapper extends EntityMapper<IssuerDto, Issuer> {}
