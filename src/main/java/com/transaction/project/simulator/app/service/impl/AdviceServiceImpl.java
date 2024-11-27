package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Advice;
import com.transaction.project.simulator.app.repository.AdviceRepository;
import com.transaction.project.simulator.app.service.AdviceService;
import com.transaction.project.simulator.app.service.dto.AdviceDto;
import com.transaction.project.simulator.app.service.mapper.AdviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Advice}.
 */
@Service
@Transactional
public class AdviceServiceImpl implements AdviceService {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceServiceImpl.class);

    private final AdviceRepository adviceRepository;

    private final AdviceMapper adviceMapper;

    public AdviceServiceImpl(AdviceRepository adviceRepository, AdviceMapper adviceMapper) {
        this.adviceRepository = adviceRepository;
        this.adviceMapper = adviceMapper;
    }

    @Override
    public AdviceDto save(AdviceDto adviceDto) {
        LOG.debug("Request to save Advice : {}", adviceDto);
        Advice advice = adviceMapper.toEntity(adviceDto);
        advice = adviceRepository.save(advice);
        return adviceMapper.toDto(advice);
    }

    @Override
    public AdviceDto update(AdviceDto adviceDto) {
        LOG.debug("Request to update Advice : {}", adviceDto);
        Advice advice = adviceMapper.toEntity(adviceDto);
        advice = adviceRepository.save(advice);
        return adviceMapper.toDto(advice);
    }

    @Override
    public Optional<AdviceDto> partialUpdate(AdviceDto adviceDto) {
        LOG.debug("Request to partially update Advice : {}", adviceDto);

        return adviceRepository
            .findById(adviceDto.getId())
            .map(existingAdvice -> {
                adviceMapper.partialUpdate(existingAdvice, adviceDto);

                return existingAdvice;
            })
            .map(adviceRepository::save)
            .map(adviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdviceDto> findOne(Long id) {
        LOG.debug("Request to get Advice : {}", id);
        return adviceRepository.findById(id).map(adviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Advice : {}", id);
        adviceRepository.deleteById(id);
    }
}
