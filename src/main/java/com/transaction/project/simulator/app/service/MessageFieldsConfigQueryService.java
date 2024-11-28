package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.MessageFieldsConfig;
import com.transaction.project.simulator.app.repository.MessageFieldsConfigRepository;
import com.transaction.project.simulator.app.service.criteria.MessageFieldsConfigCriteria;
import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldsConfigMapper;
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
 * Service for executing complex queries for {@link MessageFieldsConfig} entities in the database.
 * The main input is a {@link MessageFieldsConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MessageFieldsConfigDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageFieldsConfigQueryService extends QueryService<MessageFieldsConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldsConfigQueryService.class);

    private final MessageFieldsConfigRepository messageFieldsConfigRepository;

    private final MessageFieldsConfigMapper messageFieldsConfigMapper;

    public MessageFieldsConfigQueryService(
        MessageFieldsConfigRepository messageFieldsConfigRepository,
        MessageFieldsConfigMapper messageFieldsConfigMapper
    ) {
        this.messageFieldsConfigRepository = messageFieldsConfigRepository;
        this.messageFieldsConfigMapper = messageFieldsConfigMapper;
    }

    /**
     * Return a {@link Page} of {@link MessageFieldsConfigDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageFieldsConfigDto> findByCriteria(MessageFieldsConfigCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MessageFieldsConfig> specification = createSpecification(criteria);
        return messageFieldsConfigRepository.findAll(specification, page).map(messageFieldsConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageFieldsConfigCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MessageFieldsConfig> specification = createSpecification(criteria);
        return messageFieldsConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageFieldsConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MessageFieldsConfig> createSpecification(MessageFieldsConfigCriteria criteria) {
        Specification<MessageFieldsConfig> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MessageFieldsConfig_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MessageFieldsConfig_.name));
            }
            if (criteria.getFieldLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFieldLength(), MessageFieldsConfig_.fieldLength));
            }
            if (criteria.getMessageIsoConfigId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageIsoConfigId(), root ->
                        root.join(MessageFieldsConfig_.messageIsoConfig, JoinType.LEFT).get(MessageIsoConfig_.id)
                    )
                );
            }
            if (criteria.getMessageFieldTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageFieldTypeId(), root ->
                        root.join(MessageFieldsConfig_.messageFieldType, JoinType.LEFT).get(MessageFieldType_.id)
                    )
                );
            }
        }
        return specification;
    }
}
