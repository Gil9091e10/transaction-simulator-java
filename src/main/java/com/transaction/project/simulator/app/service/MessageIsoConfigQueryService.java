package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.repository.MessageIsoConfigRepository;
import com.transaction.project.simulator.app.service.criteria.MessageIsoConfigCriteria;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageIsoConfigMapper;
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
 * Service for executing complex queries for {@link MessageIsoConfig} entities in the database.
 * The main input is a {@link MessageIsoConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MessageIsoConfigDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageIsoConfigQueryService extends QueryService<MessageIsoConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageIsoConfigQueryService.class);

    private final MessageIsoConfigRepository messageIsoConfigRepository;

    private final MessageIsoConfigMapper messageIsoConfigMapper;

    public MessageIsoConfigQueryService(
        MessageIsoConfigRepository messageIsoConfigRepository,
        MessageIsoConfigMapper messageIsoConfigMapper
    ) {
        this.messageIsoConfigRepository = messageIsoConfigRepository;
        this.messageIsoConfigMapper = messageIsoConfigMapper;
    }

    /**
     * Return a {@link Page} of {@link MessageIsoConfigDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageIsoConfigDto> findByCriteria(MessageIsoConfigCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MessageIsoConfig> specification = createSpecification(criteria);
        return messageIsoConfigRepository.findAll(specification, page).map(messageIsoConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageIsoConfigCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MessageIsoConfig> specification = createSpecification(criteria);
        return messageIsoConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageIsoConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MessageIsoConfig> createSpecification(MessageIsoConfigCriteria criteria) {
        Specification<MessageIsoConfig> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MessageIsoConfig_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MessageIsoConfig_.name));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilename(), MessageIsoConfig_.filename));
            }
            if (criteria.getAcquirerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAcquirerId(), root ->
                        root.join(MessageIsoConfig_.acquirer, JoinType.LEFT).get(Acquirer_.id)
                    )
                );
            }
            if (criteria.getMessageTypeIndicatorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageTypeIndicatorId(), root ->
                        root.join(MessageIsoConfig_.messageTypeIndicator, JoinType.LEFT).get(MessageTypeIndicator_.id)
                    )
                );
            }
            if (criteria.getMessageFieldsConfigId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageFieldsConfigId(), root ->
                        root.join(MessageIsoConfig_.messageFieldsConfigs, JoinType.LEFT).get(MessageFieldsConfig_.id)
                    )
                );
            }
        }
        return specification;
    }
}
