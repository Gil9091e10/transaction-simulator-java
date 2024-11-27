package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.DebitCard;
import com.transaction.project.simulator.app.repository.DebitCardRepository;
import com.transaction.project.simulator.app.service.DebitCardService;
import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import com.transaction.project.simulator.app.service.mapper.DebitCardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.DebitCard}.
 */
@Service
@Transactional
public class DebitCardServiceImpl implements DebitCardService {

    private static final Logger LOG = LoggerFactory.getLogger(DebitCardServiceImpl.class);

    private final DebitCardRepository debitCardRepository;

    private final DebitCardMapper debitCardMapper;

    public DebitCardServiceImpl(DebitCardRepository debitCardRepository, DebitCardMapper debitCardMapper) {
        this.debitCardRepository = debitCardRepository;
        this.debitCardMapper = debitCardMapper;
    }

    @Override
    public DebitCardDto save(DebitCardDto debitCardDto) {
        LOG.debug("Request to save DebitCard : {}", debitCardDto);
        DebitCard debitCard = debitCardMapper.toEntity(debitCardDto);
        debitCard = debitCardRepository.save(debitCard);
        return debitCardMapper.toDto(debitCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DebitCardDto> findOne(Long id) {
        LOG.debug("Request to get DebitCard : {}", id);
        return debitCardRepository.findById(id).map(debitCardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DebitCard : {}", id);
        debitCardRepository.deleteById(id);
    }
}
