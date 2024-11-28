package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.DebitCard;
import com.transaction.project.simulator.app.repository.DebitCardRepository;
import com.transaction.project.simulator.app.service.criteria.DebitCardCriteria;
import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import com.transaction.project.simulator.app.service.mapper.DebitCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DebitCard} entities in the database.
 * The main input is a {@link DebitCardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DebitCardDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DebitCardQueryService extends QueryService<DebitCard> {

    private static final Logger LOG = LoggerFactory.getLogger(DebitCardQueryService.class);

    private final DebitCardRepository debitCardRepository;

    private final DebitCardMapper debitCardMapper;

    public DebitCardQueryService(DebitCardRepository debitCardRepository, DebitCardMapper debitCardMapper) {
        this.debitCardRepository = debitCardRepository;
        this.debitCardMapper = debitCardMapper;
    }

    /**
     * Return a {@link Page} of {@link DebitCardDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DebitCardDto> findByCriteria(DebitCardCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DebitCard> specification = createSpecification(criteria);
        return debitCardRepository.findAll(specification, page).map(debitCardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DebitCardCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DebitCard> specification = createSpecification(criteria);
        return debitCardRepository.count(specification);
    }

    /**
     * Function to convert {@link DebitCardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DebitCard> createSpecification(DebitCardCriteria criteria) {
        Specification<DebitCard> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DebitCard_.id));
            }
        }
        return specification;
    }
}
