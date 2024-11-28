package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.repository.MessageTypeIndicatorRepository;
import com.transaction.project.simulator.app.service.criteria.MessageTypeIndicatorCriteria;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import com.transaction.project.simulator.app.service.mapper.MessageTypeIndicatorMapper;
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
 * Service for executing complex queries for {@link MessageTypeIndicator} entities in the database.
 * The main input is a {@link MessageTypeIndicatorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MessageTypeIndicatorDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageTypeIndicatorQueryService extends QueryService<MessageTypeIndicator> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageTypeIndicatorQueryService.class);

    private final MessageTypeIndicatorRepository messageTypeIndicatorRepository;

    private final MessageTypeIndicatorMapper messageTypeIndicatorMapper;

    public MessageTypeIndicatorQueryService(
        MessageTypeIndicatorRepository messageTypeIndicatorRepository,
        MessageTypeIndicatorMapper messageTypeIndicatorMapper
    ) {
        this.messageTypeIndicatorRepository = messageTypeIndicatorRepository;
        this.messageTypeIndicatorMapper = messageTypeIndicatorMapper;
    }

    /**
     * Return a {@link Page} of {@link MessageTypeIndicatorDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageTypeIndicatorDto> findByCriteria(MessageTypeIndicatorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MessageTypeIndicator> specification = createSpecification(criteria);
        return messageTypeIndicatorRepository.findAll(specification, page).map(messageTypeIndicatorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageTypeIndicatorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MessageTypeIndicator> specification = createSpecification(criteria);
        return messageTypeIndicatorRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageTypeIndicatorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MessageTypeIndicator> createSpecification(MessageTypeIndicatorCriteria criteria) {
        Specification<MessageTypeIndicator> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MessageTypeIndicator_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MessageTypeIndicator_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MessageTypeIndicator_.code));
            }
            if (criteria.getMessageIsoConfigId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageIsoConfigId(), root ->
                        root.join(MessageTypeIndicator_.messageIsoConfigs, JoinType.LEFT).get(MessageIsoConfig_.id)
                    )
                );
            }
        }
        return specification;
    }
}
