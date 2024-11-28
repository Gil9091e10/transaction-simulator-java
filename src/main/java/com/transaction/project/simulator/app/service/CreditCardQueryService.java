package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.CreditCard;
import com.transaction.project.simulator.app.repository.CreditCardRepository;
import com.transaction.project.simulator.app.service.criteria.CreditCardCriteria;
import com.transaction.project.simulator.app.service.dto.CreditCardDto;
import com.transaction.project.simulator.app.service.mapper.CreditCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CreditCard} entities in the database.
 * The main input is a {@link CreditCardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CreditCardDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CreditCardQueryService extends QueryService<CreditCard> {

    private static final Logger LOG = LoggerFactory.getLogger(CreditCardQueryService.class);

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    public CreditCardQueryService(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    /**
     * Return a {@link Page} of {@link CreditCardDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CreditCardDto> findByCriteria(CreditCardCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CreditCard> specification = createSpecification(criteria);
        return creditCardRepository.findAll(specification, page).map(creditCardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CreditCardCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CreditCard> specification = createSpecification(criteria);
        return creditCardRepository.count(specification);
    }

    /**
     * Function to convert {@link CreditCardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CreditCard> createSpecification(CreditCardCriteria criteria) {
        Specification<CreditCard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CreditCard_.id));
            }
            if (criteria.getMaxLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxLimit(), CreditCard_.maxLimit));
            }
        }
        return specification;
    }
}
