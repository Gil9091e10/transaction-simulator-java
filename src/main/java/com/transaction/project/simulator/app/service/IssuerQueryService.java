package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.repository.IssuerRepository;
import com.transaction.project.simulator.app.service.criteria.IssuerCriteria;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
import com.transaction.project.simulator.app.service.mapper.IssuerMapper;
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
 * Service for executing complex queries for {@link Issuer} entities in the database.
 * The main input is a {@link IssuerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IssuerDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IssuerQueryService extends QueryService<Issuer> {

    private static final Logger LOG = LoggerFactory.getLogger(IssuerQueryService.class);

    private final IssuerRepository issuerRepository;

    private final IssuerMapper issuerMapper;

    public IssuerQueryService(IssuerRepository issuerRepository, IssuerMapper issuerMapper) {
        this.issuerRepository = issuerRepository;
        this.issuerMapper = issuerMapper;
    }

    /**
     * Return a {@link Page} of {@link IssuerDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IssuerDto> findByCriteria(IssuerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Issuer> specification = createSpecification(criteria);
        return issuerRepository.findAll(specification, page).map(issuerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IssuerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Issuer> specification = createSpecification(criteria);
        return issuerRepository.count(specification);
    }

    /**
     * Function to convert {@link IssuerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Issuer> createSpecification(IssuerCriteria criteria) {
        Specification<Issuer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Issuer_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Issuer_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Issuer_.name));
            }
            if (criteria.getCardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCardId(), root -> root.join(Issuer_.cards, JoinType.LEFT).get(Card_.id))
                );
            }
        }
        return specification;
    }
}
