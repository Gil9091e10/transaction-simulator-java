package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.repository.MerchantRepository;
import com.transaction.project.simulator.app.service.MerchantService;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
import com.transaction.project.simulator.app.service.mapper.MerchantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Merchant}.
 */
@Service
@Transactional
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final MerchantRepository merchantRepository;

    private final MerchantMapper merchantMapper;

    public MerchantServiceImpl(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public MerchantDto save(MerchantDto merchantDto) {
        LOG.debug("Request to save Merchant : {}", merchantDto);
        Merchant merchant = merchantMapper.toEntity(merchantDto);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDto(merchant);
    }

    @Override
    public MerchantDto update(MerchantDto merchantDto) {
        LOG.debug("Request to update Merchant : {}", merchantDto);
        Merchant merchant = merchantMapper.toEntity(merchantDto);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDto(merchant);
    }

    @Override
    public Optional<MerchantDto> partialUpdate(MerchantDto merchantDto) {
        LOG.debug("Request to partially update Merchant : {}", merchantDto);

        return merchantRepository
            .findById(merchantDto.getId())
            .map(existingMerchant -> {
                merchantMapper.partialUpdate(existingMerchant, merchantDto);

                return existingMerchant;
            })
            .map(merchantRepository::save)
            .map(merchantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MerchantDto> findOne(Long id) {
        LOG.debug("Request to get Merchant : {}", id);
        return merchantRepository.findById(id).map(merchantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Merchant : {}", id);
        merchantRepository.deleteById(id);
    }
}
