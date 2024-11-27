package com.transaction.project.simulator.app.service.mapper;

import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.domain.Advice;
import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import com.transaction.project.simulator.app.service.dto.AdviceDto;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Advice} and its DTO {@link AdviceDto}.
 */
@Mapper(componentModel = "spring")
public interface AdviceMapper extends EntityMapper<AdviceDto, Advice> {
    @Mapping(target = "merchant", source = "merchant", qualifiedByName = "merchantId")
    @Mapping(target = "acquirer", source = "acquirer", qualifiedByName = "acquirerId")
    AdviceDto toDto(Advice s);

    @Named("merchantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MerchantDto toDtoMerchantId(Merchant merchant);

    @Named("acquirerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AcquirerDto toDtoAcquirerId(Acquirer acquirer);
}
