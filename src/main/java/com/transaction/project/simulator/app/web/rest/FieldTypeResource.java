package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.FieldTypeRepository;
import com.transaction.project.simulator.app.service.FieldTypeQueryService;
import com.transaction.project.simulator.app.service.FieldTypeService;
import com.transaction.project.simulator.app.service.criteria.FieldTypeCriteria;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import com.transaction.project.simulator.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.FieldType}.
 */
@RestController
@RequestMapping("/api/field-types")
public class FieldTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(FieldTypeResource.class);

    private static final String ENTITY_NAME = "fieldType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FieldTypeService fieldTypeService;

    private final FieldTypeRepository fieldTypeRepository;

    private final FieldTypeQueryService fieldTypeQueryService;

    public FieldTypeResource(
        FieldTypeService fieldTypeService,
        FieldTypeRepository fieldTypeRepository,
        FieldTypeQueryService fieldTypeQueryService
    ) {
        this.fieldTypeService = fieldTypeService;
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldTypeQueryService = fieldTypeQueryService;
    }

    /**
     * {@code POST  /field-types} : Create a new fieldType.
     *
     * @param fieldTypeDto the fieldTypeDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTypeDto, or with status {@code 400 (Bad Request)} if the fieldType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FieldTypeDto> createFieldType(@Valid @RequestBody FieldTypeDto fieldTypeDto) throws URISyntaxException {
        LOG.debug("REST request to save FieldType : {}", fieldTypeDto);
        if (fieldTypeDto.getId() != null) {
            throw new BadRequestAlertException("A new fieldType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fieldTypeDto = fieldTypeService.save(fieldTypeDto);
        return ResponseEntity.created(new URI("/api/field-types/" + fieldTypeDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fieldTypeDto.getId().toString()))
            .body(fieldTypeDto);
    }

    /**
     * {@code PUT  /field-types/:id} : Updates an existing fieldType.
     *
     * @param id the id of the fieldTypeDto to save.
     * @param fieldTypeDto the fieldTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTypeDto,
     * or with status {@code 400 (Bad Request)} if the fieldTypeDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FieldTypeDto> updateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FieldTypeDto fieldTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update FieldType : {}, {}", id, fieldTypeDto);
        if (fieldTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fieldTypeDto = fieldTypeService.update(fieldTypeDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldTypeDto.getId().toString()))
            .body(fieldTypeDto);
    }

    /**
     * {@code PATCH  /field-types/:id} : Partial updates given fields of an existing fieldType, field will ignore if it is null
     *
     * @param id the id of the fieldTypeDto to save.
     * @param fieldTypeDto the fieldTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTypeDto,
     * or with status {@code 400 (Bad Request)} if the fieldTypeDto is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTypeDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FieldTypeDto> partialUpdateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FieldTypeDto fieldTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FieldType partially : {}, {}", id, fieldTypeDto);
        if (fieldTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FieldTypeDto> result = fieldTypeService.partialUpdate(fieldTypeDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldTypeDto.getId().toString())
        );
    }

    /**
     * {@code GET  /field-types} : get all the fieldTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FieldTypeDto>> getAllFieldTypes(
        FieldTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FieldTypes by criteria: {}", criteria);

        Page<FieldTypeDto> page = fieldTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /field-types/count} : count all the fieldTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFieldTypes(FieldTypeCriteria criteria) {
        LOG.debug("REST request to count FieldTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fieldTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /field-types/:id} : get the "id" fieldType.
     *
     * @param id the id of the fieldTypeDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTypeDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FieldTypeDto> getFieldType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FieldType : {}", id);
        Optional<FieldTypeDto> fieldTypeDto = fieldTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldTypeDto);
    }

    /**
     * {@code DELETE  /field-types/:id} : delete the "id" fieldType.
     *
     * @param id the id of the fieldTypeDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FieldType : {}", id);
        fieldTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
