package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.repository.CurrencyRepository;
import com.transaction.project.simulator.app.service.CurrencyService;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import com.transaction.project.simulator.app.service.mapper.CurrencyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Currency}.
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyDto save(CurrencyDto currencyDto) {
        LOG.debug("Request to save Currency : {}", currencyDto);
        Currency currency = currencyMapper.toEntity(currencyDto);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDto(currency);
    }

    @Override
    public CurrencyDto update(CurrencyDto currencyDto) {
        LOG.debug("Request to update Currency : {}", currencyDto);
        Currency currency = currencyMapper.toEntity(currencyDto);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDto(currency);
    }

    @Override
    public Optional<CurrencyDto> partialUpdate(CurrencyDto currencyDto) {
        LOG.debug("Request to partially update Currency : {}", currencyDto);

        return currencyRepository
            .findById(currencyDto.getId())
            .map(existingCurrency -> {
                currencyMapper.partialUpdate(existingCurrency, currencyDto);

                return existingCurrency;
            })
            .map(currencyRepository::save)
            .map(currencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyDto> findOne(Long id) {
        LOG.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id).map(currencyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Currency : {}", id);
        currencyRepository.deleteById(id);
    }
}
