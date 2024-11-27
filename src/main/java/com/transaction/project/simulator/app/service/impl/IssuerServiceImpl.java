package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.repository.IssuerRepository;
import com.transaction.project.simulator.app.service.IssuerService;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
import com.transaction.project.simulator.app.service.mapper.IssuerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.Issuer}.
 */
@Service
@Transactional
public class IssuerServiceImpl implements IssuerService {

    private static final Logger LOG = LoggerFactory.getLogger(IssuerServiceImpl.class);

    private final IssuerRepository issuerRepository;

    private final IssuerMapper issuerMapper;

    public IssuerServiceImpl(IssuerRepository issuerRepository, IssuerMapper issuerMapper) {
        this.issuerRepository = issuerRepository;
        this.issuerMapper = issuerMapper;
    }

    @Override
    public IssuerDto save(IssuerDto issuerDto) {
        LOG.debug("Request to save Issuer : {}", issuerDto);
        Issuer issuer = issuerMapper.toEntity(issuerDto);
        issuer = issuerRepository.save(issuer);
        return issuerMapper.toDto(issuer);
    }

    @Override
    public IssuerDto update(IssuerDto issuerDto) {
        LOG.debug("Request to update Issuer : {}", issuerDto);
        Issuer issuer = issuerMapper.toEntity(issuerDto);
        issuer = issuerRepository.save(issuer);
        return issuerMapper.toDto(issuer);
    }

    @Override
    public Optional<IssuerDto> partialUpdate(IssuerDto issuerDto) {
        LOG.debug("Request to partially update Issuer : {}", issuerDto);

        return issuerRepository
            .findById(issuerDto.getId())
            .map(existingIssuer -> {
                issuerMapper.partialUpdate(existingIssuer, issuerDto);

                return existingIssuer;
            })
            .map(issuerRepository::save)
            .map(issuerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IssuerDto> findOne(Long id) {
        LOG.debug("Request to get Issuer : {}", id);
        return issuerRepository.findById(id).map(issuerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Issuer : {}", id);
        issuerRepository.deleteById(id);
    }
}
