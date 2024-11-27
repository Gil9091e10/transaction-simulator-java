package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.CreditCard;
import com.transaction.project.simulator.app.repository.CreditCardRepository;
import com.transaction.project.simulator.app.service.CreditCardService;
import com.transaction.project.simulator.app.service.dto.CreditCardDto;
import com.transaction.project.simulator.app.service.mapper.CreditCardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.CreditCard}.
 */
@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private static final Logger LOG = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    @Override
    public CreditCardDto save(CreditCardDto creditCardDto) {
        LOG.debug("Request to save CreditCard : {}", creditCardDto);
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDto);
        creditCard = creditCardRepository.save(creditCard);
        return creditCardMapper.toDto(creditCard);
    }

    @Override
    public CreditCardDto update(CreditCardDto creditCardDto) {
        LOG.debug("Request to update CreditCard : {}", creditCardDto);
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDto);
        creditCard = creditCardRepository.save(creditCard);
        return creditCardMapper.toDto(creditCard);
    }

    @Override
    public Optional<CreditCardDto> partialUpdate(CreditCardDto creditCardDto) {
        LOG.debug("Request to partially update CreditCard : {}", creditCardDto);

        return creditCardRepository
            .findById(creditCardDto.getId())
            .map(existingCreditCard -> {
                creditCardMapper.partialUpdate(existingCreditCard, creditCardDto);

                return existingCreditCard;
            })
            .map(creditCardRepository::save)
            .map(creditCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardDto> findOne(Long id) {
        LOG.debug("Request to get CreditCard : {}", id);
        return creditCardRepository.findById(id).map(creditCardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CreditCard : {}", id);
        creditCardRepository.deleteById(id);
    }
}
