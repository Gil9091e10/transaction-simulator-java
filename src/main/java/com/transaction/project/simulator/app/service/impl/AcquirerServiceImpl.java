package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.repository.AcquirerRepository;
import com.transaction.project.simulator.app.service.AcquirerService;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import com.transaction.project.simulator.app.service.mapper.AcquirerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Acquirer}.
 */
@Service
@Transactional
public class AcquirerServiceImpl implements AcquirerService {

    private static final Logger LOG = LoggerFactory.getLogger(AcquirerServiceImpl.class);

    private final AcquirerRepository acquirerRepository;

    private final AcquirerMapper acquirerMapper;

    public AcquirerServiceImpl(AcquirerRepository acquirerRepository, AcquirerMapper acquirerMapper) {
        this.acquirerRepository = acquirerRepository;
        this.acquirerMapper = acquirerMapper;
    }

    @Override
    public AcquirerDto save(AcquirerDto acquirerDto) {
        LOG.debug("Request to save Acquirer : {}", acquirerDto);
        Acquirer acquirer = acquirerMapper.toEntity(acquirerDto);
        acquirer = acquirerRepository.save(acquirer);
        return acquirerMapper.toDto(acquirer);
    }

    @Override
    public AcquirerDto update(AcquirerDto acquirerDto) {
        LOG.debug("Request to update Acquirer : {}", acquirerDto);
        Acquirer acquirer = acquirerMapper.toEntity(acquirerDto);
        acquirer = acquirerRepository.save(acquirer);
        return acquirerMapper.toDto(acquirer);
    }

    @Override
    public Optional<AcquirerDto> partialUpdate(AcquirerDto acquirerDto) {
        LOG.debug("Request to partially update Acquirer : {}", acquirerDto);

        return acquirerRepository
            .findById(acquirerDto.getId())
            .map(existingAcquirer -> {
                acquirerMapper.partialUpdate(existingAcquirer, acquirerDto);

                return existingAcquirer;
            })
            .map(acquirerRepository::save)
            .map(acquirerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcquirerDto> findOne(Long id) {
        LOG.debug("Request to get Acquirer : {}", id);
        return acquirerRepository.findById(id).map(acquirerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Acquirer : {}", id);
        acquirerRepository.deleteById(id);
    }
}
