package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Advice;
import com.transaction.project.simulator.app.repository.AdviceRepository;
import com.transaction.project.simulator.app.service.criteria.AdviceCriteria;
import com.transaction.project.simulator.app.service.dto.AdviceDto;
import com.transaction.project.simulator.app.service.mapper.AdviceMapper;
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
 * Service for executing complex queries for {@link Advice} entities in the database.
 * The main input is a {@link AdviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AdviceDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdviceQueryService extends QueryService<Advice> {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceQueryService.class);

    private final AdviceRepository adviceRepository;

    private final AdviceMapper adviceMapper;

    public AdviceQueryService(AdviceRepository adviceRepository, AdviceMapper adviceMapper) {
        this.adviceRepository = adviceRepository;
        this.adviceMapper = adviceMapper;
    }

    /**
     * Return a {@link Page} of {@link AdviceDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdviceDto> findByCriteria(AdviceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Advice> specification = createSpecification(criteria);
        return adviceRepository.findAll(specification, page).map(adviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdviceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Advice> specification = createSpecification(criteria);
        return adviceRepository.count(specification);
    }

    /**
     * Function to convert {@link AdviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Advice> createSpecification(AdviceCriteria criteria) {
        Specification<Advice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Advice_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Advice_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Advice_.name));
            }
            if (criteria.getMerchantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMerchantId(), root -> root.join(Advice_.merchant, JoinType.LEFT).get(Merchant_.id))
                );
            }
            if (criteria.getAcquirerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAcquirerId(), root -> root.join(Advice_.acquirer, JoinType.LEFT).get(Acquirer_.id))
                );
            }
        }
        return specification;
    }
}
