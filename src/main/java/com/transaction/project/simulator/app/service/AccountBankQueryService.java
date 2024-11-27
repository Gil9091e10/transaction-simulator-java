package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.repository.AccountBankRepository;
import com.transaction.project.simulator.app.service.criteria.AccountBankCriteria;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import com.transaction.project.simulator.app.service.mapper.AccountBankMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AccountBank} entities in the database.
 * The main input is a {@link AccountBankCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AccountBankDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountBankQueryService extends QueryService<AccountBank> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountBankQueryService.class);

    private final AccountBankRepository accountBankRepository;

    private final AccountBankMapper accountBankMapper;

    public AccountBankQueryService(AccountBankRepository accountBankRepository, AccountBankMapper accountBankMapper) {
        this.accountBankRepository = accountBankRepository;
        this.accountBankMapper = accountBankMapper;
    }

    /**
     * Return a {@link Page} of {@link AccountBankDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountBankDto> findByCriteria(AccountBankCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountBank> specification = createSpecification(criteria);
        return accountBankRepository.findAll(specification, page).map(accountBankMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountBankCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AccountBank> specification = createSpecification(criteria);
        return accountBankRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountBankCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountBank> createSpecification(AccountBankCriteria criteria) {
        Specification<AccountBank> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountBank_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), AccountBank_.number));
            }
            if (criteria.getNumberIBAN() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumberIBAN(), AccountBank_.numberIBAN));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), AccountBank_.balance));
            }
            if (criteria.getCardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCardId(), root -> root.join(AccountBank_.card, JoinType.LEFT).get(Card_.id))
                );
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCurrencyId(), root -> root.join(AccountBank_.currency, JoinType.LEFT).get(Currency_.id))
                );
            }
        }
        return specification;
    }
}
