package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.repository.CardTypeRepository;
import com.transaction.project.simulator.app.service.CardTypeService;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import com.transaction.project.simulator.app.service.mapper.CardTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.CardType}.
 */
@Service
@Transactional
public class CardTypeServiceImpl implements CardTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CardTypeServiceImpl.class);

    private final CardTypeRepository cardTypeRepository;

    private final CardTypeMapper cardTypeMapper;

    public CardTypeServiceImpl(CardTypeRepository cardTypeRepository, CardTypeMapper cardTypeMapper) {
        this.cardTypeRepository = cardTypeRepository;
        this.cardTypeMapper = cardTypeMapper;
    }

    @Override
    public CardTypeDto save(CardTypeDto cardTypeDto) {
        LOG.debug("Request to save CardType : {}", cardTypeDto);
        CardType cardType = cardTypeMapper.toEntity(cardTypeDto);
        cardType = cardTypeRepository.save(cardType);
        return cardTypeMapper.toDto(cardType);
    }

    @Override
    public CardTypeDto update(CardTypeDto cardTypeDto) {
        LOG.debug("Request to update CardType : {}", cardTypeDto);
        CardType cardType = cardTypeMapper.toEntity(cardTypeDto);
        cardType = cardTypeRepository.save(cardType);
        return cardTypeMapper.toDto(cardType);
    }

    @Override
    public Optional<CardTypeDto> partialUpdate(CardTypeDto cardTypeDto) {
        LOG.debug("Request to partially update CardType : {}", cardTypeDto);

        return cardTypeRepository
            .findById(cardTypeDto.getId())
            .map(existingCardType -> {
                cardTypeMapper.partialUpdate(existingCardType, cardTypeDto);

                return existingCardType;
            })
            .map(cardTypeRepository::save)
            .map(cardTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardTypeDto> findOne(Long id) {
        LOG.debug("Request to get CardType : {}", id);
        return cardTypeRepository.findById(id).map(cardTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CardType : {}", id);
        cardTypeRepository.deleteById(id);
    }
}
