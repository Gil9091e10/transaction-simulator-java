package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.transaction.project.simulator.app.domain.AccountBank}.
 */
public interface AccountBankService {
    /**
     * Save a accountBank.
     *
     * @param accountBankDto the entity to save.
     * @return the persisted entity.
     */
    AccountBankDto save(AccountBankDto accountBankDto);

    /**
     * Updates a accountBank.
     *
     * @param accountBankDto the entity to update.
     * @return the persisted entity.
     */
    AccountBankDto update(AccountBankDto accountBankDto);

    /**
     * Partially updates a accountBank.
     *
     * @param accountBankDto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountBankDto> partialUpdate(AccountBankDto accountBankDto);

    /**
     * Get all the AccountBankDto where Card is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AccountBankDto> findAllWhereCardIsNull();

    /**
     * Get the "id" accountBank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountBankDto> findOne(Long id);

    /**
     * Delete the "id" accountBank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
