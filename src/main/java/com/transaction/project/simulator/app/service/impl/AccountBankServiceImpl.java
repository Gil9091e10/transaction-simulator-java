package com.transaction.project.simulator.app.service.impl;

import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.repository.AccountBankRepository;
import com.transaction.project.simulator.app.service.AccountBankService;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import com.transaction.project.simulator.app.service.mapper.AccountBankMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.transaction.project.simulator.app.domain.AccountBank}.
 */
@Service
@Transactional
public class AccountBankServiceImpl implements AccountBankService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountBankServiceImpl.class);

    private final AccountBankRepository accountBankRepository;

    private final AccountBankMapper accountBankMapper;

    public AccountBankServiceImpl(AccountBankRepository accountBankRepository, AccountBankMapper accountBankMapper) {
        this.accountBankRepository = accountBankRepository;
        this.accountBankMapper = accountBankMapper;
    }

    @Override
    public AccountBankDto save(AccountBankDto accountBankDto) {
        LOG.debug("Request to save AccountBank : {}", accountBankDto);
        AccountBank accountBank = accountBankMapper.toEntity(accountBankDto);
        accountBank = accountBankRepository.save(accountBank);
        return accountBankMapper.toDto(accountBank);
    }

    @Override
    public AccountBankDto update(AccountBankDto accountBankDto) {
        LOG.debug("Request to update AccountBank : {}", accountBankDto);
        AccountBank accountBank = accountBankMapper.toEntity(accountBankDto);
        accountBank = accountBankRepository.save(accountBank);
        return accountBankMapper.toDto(accountBank);
    }

    @Override
    public Optional<AccountBankDto> partialUpdate(AccountBankDto accountBankDto) {
        LOG.debug("Request to partially update AccountBank : {}", accountBankDto);

        return accountBankRepository
            .findById(accountBankDto.getId())
            .map(existingAccountBank -> {
                accountBankMapper.partialUpdate(existingAccountBank, accountBankDto);

                return existingAccountBank;
            })
            .map(accountBankRepository::save)
            .map(accountBankMapper::toDto);
    }

    /**
     *  Get all the accountBanks where Card is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AccountBankDto> findAllWhereCardIsNull() {
        LOG.debug("Request to get all accountBanks where Card is null");
        return StreamSupport.stream(accountBankRepository.findAll().spliterator(), false)
            .filter(accountBank -> accountBank.getCard() == null)
            .map(accountBankMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountBankDto> findOne(Long id) {
        LOG.debug("Request to get AccountBank : {}", id);
        return accountBankRepository.findById(id).map(accountBankMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AccountBank : {}", id);
        accountBankRepository.deleteById(id);
    }
}
