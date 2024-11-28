package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Card;
import com.transaction.project.simulator.app.repository.CardRepository;
import com.transaction.project.simulator.app.service.CardService;
import com.transaction.project.simulator.app.service.dto.CardDto;
import com.transaction.project.simulator.app.service.mapper.CardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger LOG = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDto save(CardDto cardDto) {
        LOG.debug("Request to save Card : {}", cardDto);
        Card card = cardMapper.toEntity(cardDto);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public CardDto update(CardDto cardDto) {
        LOG.debug("Request to update Card : {}", cardDto);
        Card card = cardMapper.toEntity(cardDto);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public Optional<CardDto> partialUpdate(CardDto cardDto) {
        LOG.debug("Request to partially update Card : {}", cardDto);

        return cardRepository
            .findById(cardDto.getId())
            .map(existingCard -> {
                cardMapper.partialUpdate(existingCard, cardDto);

                return existingCard;
            })
            .map(cardRepository::save)
            .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDto> findOne(Long id) {
        LOG.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
