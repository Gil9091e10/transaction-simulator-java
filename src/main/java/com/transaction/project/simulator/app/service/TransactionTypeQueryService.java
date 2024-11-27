package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.TransactionType;
import com.transaction.project.simulator.app.repository.TransactionTypeRepository;
import com.transaction.project.simulator.app.service.criteria.TransactionTypeCriteria;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import com.transaction.project.simulator.app.service.mapper.TransactionTypeMapper;
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
 * Service for executing complex queries for {@link TransactionType} entities in the database.
 * The main input is a {@link TransactionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransactionTypeDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionTypeQueryService extends QueryService<TransactionType> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeQueryService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeMapper transactionTypeMapper;

    public TransactionTypeQueryService(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link TransactionTypeDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionTypeDto> findByCriteria(TransactionTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.findAll(specification, page).map(transactionTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransactionType> specification = createSpecification(criteria);
        return transactionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionType> createSpecification(TransactionTypeCriteria criteria) {
        Specification<TransactionType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), TransactionType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TransactionType_.name));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTransactionId(), root ->
                        root.join(TransactionType_.transactions, JoinType.LEFT).get(Transaction_.id)
                    )
                );
            }
        }
        return specification;
    }
}
