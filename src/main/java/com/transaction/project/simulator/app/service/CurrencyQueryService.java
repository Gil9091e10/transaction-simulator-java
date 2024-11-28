package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.repository.CurrencyRepository;
import com.transaction.project.simulator.app.service.criteria.CurrencyCriteria;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import com.transaction.project.simulator.app.service.mapper.CurrencyMapper;
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
 * Service for executing complex queries for {@link Currency} entities in the database.
 * The main input is a {@link CurrencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CurrencyDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyQueryService extends QueryService<Currency> {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyQueryService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyQueryService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Return a {@link Page} of {@link CurrencyDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyDto> findByCriteria(CurrencyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.findAll(specification, page).map(currencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Currency> createSpecification(CurrencyCriteria criteria) {
        Specification<Currency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Currency_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Currency_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Currency_.code));
            }
            if (criteria.getAccountBankId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAccountBankId(), root ->
                        root.join(Currency_.accountBanks, JoinType.LEFT).get(AccountBank_.id)
                    )
                );
            }
        }
        return specification;
    }
}
