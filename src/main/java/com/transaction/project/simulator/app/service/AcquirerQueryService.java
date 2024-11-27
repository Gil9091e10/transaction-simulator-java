package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.repository.AcquirerRepository;
import com.transaction.project.simulator.app.service.criteria.AcquirerCriteria;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import com.transaction.project.simulator.app.service.mapper.AcquirerMapper;
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
 * Service for executing complex queries for {@link Acquirer} entities in the database.
 * The main input is a {@link AcquirerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AcquirerDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AcquirerQueryService extends QueryService<Acquirer> {

    private static final Logger LOG = LoggerFactory.getLogger(AcquirerQueryService.class);

    private final AcquirerRepository acquirerRepository;

    private final AcquirerMapper acquirerMapper;

    public AcquirerQueryService(AcquirerRepository acquirerRepository, AcquirerMapper acquirerMapper) {
        this.acquirerRepository = acquirerRepository;
        this.acquirerMapper = acquirerMapper;
    }

    /**
     * Return a {@link Page} of {@link AcquirerDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AcquirerDto> findByCriteria(AcquirerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Acquirer> specification = createSpecification(criteria);
        return acquirerRepository.findAll(specification, page).map(acquirerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AcquirerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Acquirer> specification = createSpecification(criteria);
        return acquirerRepository.count(specification);
    }

    /**
     * Function to convert {@link AcquirerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Acquirer> createSpecification(AcquirerCriteria criteria) {
        Specification<Acquirer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Acquirer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Acquirer_.name));
            }
            if (criteria.getSocketUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSocketUrl(), Acquirer_.socketUrl));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Acquirer_.email));
            }
            if (criteria.getAdviceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAdviceId(), root -> root.join(Acquirer_.advice, JoinType.LEFT).get(Advice_.id))
                );
            }
            if (criteria.getMessageIsoConfigId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageIsoConfigId(), root ->
                        root.join(Acquirer_.messageIsoConfigs, JoinType.LEFT).get(MessageIsoConfig_.id)
                    )
                );
            }
        }
        return specification;
    }
}
