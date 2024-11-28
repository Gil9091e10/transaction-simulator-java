package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.repository.CardTypeRepository;
import com.transaction.project.simulator.app.service.criteria.CardTypeCriteria;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import com.transaction.project.simulator.app.service.mapper.CardTypeMapper;
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
 * Service for executing complex queries for {@link CardType} entities in the database.
 * The main input is a {@link CardTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CardTypeDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardTypeQueryService extends QueryService<CardType> {

    private static final Logger LOG = LoggerFactory.getLogger(CardTypeQueryService.class);

    private final CardTypeRepository cardTypeRepository;

    private final CardTypeMapper cardTypeMapper;

    public CardTypeQueryService(CardTypeRepository cardTypeRepository, CardTypeMapper cardTypeMapper) {
        this.cardTypeRepository = cardTypeRepository;
        this.cardTypeMapper = cardTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link CardTypeDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardTypeDto> findByCriteria(CardTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardType> specification = createSpecification(criteria);
        return cardTypeRepository.findAll(specification, page).map(cardTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CardType> specification = createSpecification(criteria);
        return cardTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CardTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardType> createSpecification(CardTypeCriteria criteria) {
        Specification<CardType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CardType_.name));
            }
            if (criteria.getCardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCardId(), root -> root.join(CardType_.cards, JoinType.LEFT).get(Card_.id))
                );
            }
        }
        return specification;
    }
}
