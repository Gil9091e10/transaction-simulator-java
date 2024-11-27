package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Merchant} and its DTO {@link MerchantDto}.
 */
@Mapper(componentModel = "spring")
public interface MerchantMapper extends EntityMapper<MerchantDto, Merchant> {}
