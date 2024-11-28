package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.repository.MessageFieldTypeRepository;
import com.transaction.project.simulator.app.service.criteria.MessageFieldTypeCriteria;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldTypeMapper;
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
 * Service for executing complex queries for {@link MessageFieldType} entities in the database.
 * The main input is a {@link MessageFieldTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MessageFieldTypeDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageFieldTypeQueryService extends QueryService<MessageFieldType> {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldTypeQueryService.class);

    private final MessageFieldTypeRepository messageFieldTypeRepository;

    private final MessageFieldTypeMapper messageFieldTypeMapper;

    public MessageFieldTypeQueryService(
        MessageFieldTypeRepository messageFieldTypeRepository,
        MessageFieldTypeMapper messageFieldTypeMapper
    ) {
        this.messageFieldTypeRepository = messageFieldTypeRepository;
        this.messageFieldTypeMapper = messageFieldTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link MessageFieldTypeDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageFieldTypeDto> findByCriteria(MessageFieldTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MessageFieldType> specification = createSpecification(criteria);
        return messageFieldTypeRepository.findAll(specification, page).map(messageFieldTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageFieldTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MessageFieldType> specification = createSpecification(criteria);
        return messageFieldTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageFieldTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MessageFieldType> createSpecification(MessageFieldTypeCriteria criteria) {
        Specification<MessageFieldType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MessageFieldType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MessageFieldType_.name));
            }
            if (criteria.getMessageFieldsConfigId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageFieldsConfigId(), root ->
                        root.join(MessageFieldType_.messageFieldsConfigs, JoinType.LEFT).get(MessageFieldsConfig_.id)
                    )
                );
            }
            if (criteria.getFieldTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFieldTypeId(), root ->
                        root.join(MessageFieldType_.fieldType, JoinType.LEFT).get(FieldType_.id)
                    )
                );
            }
        }
        return specification;
    }
}
