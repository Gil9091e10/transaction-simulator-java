package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Card;
import com.transaction.project.simulator.app.repository.CardRepository;
import com.transaction.project.simulator.app.service.criteria.CardCriteria;
import com.transaction.project.simulator.app.service.dto.CardDto;
import com.transaction.project.simulator.app.service.mapper.CardMapper;
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
 * Service for executing complex queries for {@link Card} entities in the database.
 * The main input is a {@link CardCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CardDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardQueryService extends QueryService<Card> {

    private static final Logger LOG = LoggerFactory.getLogger(CardQueryService.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardQueryService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Return a {@link Page} of {@link CardDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardDto> findByCriteria(CardCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.findAll(specification, page).map(cardMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Card> specification = createSpecification(criteria);
        return cardRepository.count(specification);
    }

    /**
     * Function to convert {@link CardCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Card> createSpecification(CardCriteria criteria) {
        Specification<Card> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Card_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Card_.number));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), Card_.expirationDate));
            }
            if (criteria.getVerificationValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVerificationValue(), Card_.verificationValue));
            }
            if (criteria.getAccountBankId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAccountBankId(), root -> root.join(Card_.accountBank, JoinType.LEFT).get(AccountBank_.id)
                    )
                );
            }
            if (criteria.getCardTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCardTypeId(), root -> root.join(Card_.cardType, JoinType.LEFT).get(CardType_.id))
                );
            }
            if (criteria.getIssuerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIssuerId(), root -> root.join(Card_.issuer, JoinType.LEFT).get(Issuer_.id))
                );
            }
        }
        return specification;
    }
}
