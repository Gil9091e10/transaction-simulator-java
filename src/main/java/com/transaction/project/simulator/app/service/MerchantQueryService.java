package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.repository.MerchantRepository;
import com.transaction.project.simulator.app.service.criteria.MerchantCriteria;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
import com.transaction.project.simulator.app.service.mapper.MerchantMapper;
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
 * Service for executing complex queries for {@link Merchant} entities in the database.
 * The main input is a {@link MerchantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MerchantDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MerchantQueryService extends QueryService<Merchant> {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantQueryService.class);

    private final MerchantRepository merchantRepository;

    private final MerchantMapper merchantMapper;

    public MerchantQueryService(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    /**
     * Return a {@link Page} of {@link MerchantDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MerchantDto> findByCriteria(MerchantCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Merchant> specification = createSpecification(criteria);
        return merchantRepository.findAll(specification, page).map(merchantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MerchantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Merchant> specification = createSpecification(criteria);
        return merchantRepository.count(specification);
    }

    /**
     * Function to convert {@link MerchantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Merchant> createSpecification(MerchantCriteria criteria) {
        Specification<Merchant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Merchant_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Merchant_.name));
            }
            if (criteria.getMcc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMcc(), Merchant_.mcc));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPostalCode(), Merchant_.postalCode));
            }
            if (criteria.getWebsite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsite(), Merchant_.website));
            }
            if (criteria.getAdviceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAdviceId(), root -> root.join(Merchant_.advice, JoinType.LEFT).get(Advice_.id))
                );
            }
        }
        return specification;
    }
}
