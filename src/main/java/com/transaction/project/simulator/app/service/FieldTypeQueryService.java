package com.transaction.project.simulator.app.service;

import com.transaction.project.simulator.app.domain.*; // for static metamodels
import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.repository.FieldTypeRepository;
import com.transaction.project.simulator.app.service.criteria.FieldTypeCriteria;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.FieldTypeMapper;
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
 * Service for executing complex queries for {@link FieldType} entities in the database.
 * The main input is a {@link FieldTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FieldTypeDto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FieldTypeQueryService extends QueryService<FieldType> {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTypeQueryService.class);

    private final FieldTypeRepository fieldTypeRepository;

    private final FieldTypeMapper fieldTypeMapper;

    public FieldTypeQueryService(FieldTypeRepository fieldTypeRepository, FieldTypeMapper fieldTypeMapper) {
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldTypeMapper = fieldTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link FieldTypeDto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FieldTypeDto> findByCriteria(FieldTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FieldType> specification = createSpecification(criteria);
        return fieldTypeRepository.findAll(specification, page).map(fieldTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FieldTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FieldType> specification = createSpecification(criteria);
        return fieldTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FieldTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FieldType> createSpecification(FieldTypeCriteria criteria) {
        Specification<FieldType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FieldType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FieldType_.name));
            }
            if (criteria.getMessageFieldTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMessageFieldTypeId(), root ->
                        root.join(FieldType_.messageFieldTypes, JoinType.LEFT).get(MessageFieldType_.id)
                    )
                );
            }
        }
        return specification;
    }
}
