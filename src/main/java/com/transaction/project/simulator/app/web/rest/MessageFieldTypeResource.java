package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.MessageFieldTypeRepository;
import com.transaction.project.simulator.app.service.MessageFieldTypeQueryService;
import com.transaction.project.simulator.app.service.MessageFieldTypeService;
import com.transaction.project.simulator.app.service.criteria.MessageFieldTypeCriteria;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.MessageFieldType}.
 */
@RestController
@RequestMapping("/api/message-field-types")
public class MessageFieldTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldTypeResource.class);

    private static final String ENTITY_NAME = "messageFieldType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageFieldTypeService messageFieldTypeService;

    private final MessageFieldTypeRepository messageFieldTypeRepository;

    private final MessageFieldTypeQueryService messageFieldTypeQueryService;

    public MessageFieldTypeResource(
        MessageFieldTypeService messageFieldTypeService,
        MessageFieldTypeRepository messageFieldTypeRepository,
        MessageFieldTypeQueryService messageFieldTypeQueryService
    ) {
        this.messageFieldTypeService = messageFieldTypeService;
        this.messageFieldTypeRepository = messageFieldTypeRepository;
        this.messageFieldTypeQueryService = messageFieldTypeQueryService;
    }

    /**
     * {@code POST  /message-field-types} : Create a new messageFieldType.
     *
     * @param messageFieldTypeDto the messageFieldTypeDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageFieldTypeDto, or with status {@code 400 (Bad Request)} if the messageFieldType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MessageFieldTypeDto> createMessageFieldType(@Valid @RequestBody MessageFieldTypeDto messageFieldTypeDto)
        throws URISyntaxException {
        LOG.debug("REST request to save MessageFieldType : {}", messageFieldTypeDto);
        if (messageFieldTypeDto.getId() != null) {
            throw new BadRequestAlertException("A new messageFieldType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        messageFieldTypeDto = messageFieldTypeService.save(messageFieldTypeDto);
        return ResponseEntity.created(new URI("/api/message-field-types/" + messageFieldTypeDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, messageFieldTypeDto.getId().toString()))
            .body(messageFieldTypeDto);
    }

    /**
     * {@code PUT  /message-field-types/:id} : Updates an existing messageFieldType.
     *
     * @param id the id of the messageFieldTypeDto to save.
     * @param messageFieldTypeDto the messageFieldTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageFieldTypeDto,
     * or with status {@code 400 (Bad Request)} if the messageFieldTypeDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageFieldTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageFieldTypeDto> updateMessageFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MessageFieldTypeDto messageFieldTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update MessageFieldType : {}, {}", id, messageFieldTypeDto);
        if (messageFieldTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageFieldTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageFieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        messageFieldTypeDto = messageFieldTypeService.update(messageFieldTypeDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageFieldTypeDto.getId().toString()))
            .body(messageFieldTypeDto);
    }

    /**
     * {@code PATCH  /message-field-types/:id} : Partial updates given fields of an existing messageFieldType, field will ignore if it is null
     *
     * @param id the id of the messageFieldTypeDto to save.
     * @param messageFieldTypeDto the messageFieldTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageFieldTypeDto,
     * or with status {@code 400 (Bad Request)} if the messageFieldTypeDto is not valid,
     * or with status {@code 404 (Not Found)} if the messageFieldTypeDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the messageFieldTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MessageFieldTypeDto> partialUpdateMessageFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MessageFieldTypeDto messageFieldTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MessageFieldType partially : {}, {}", id, messageFieldTypeDto);
        if (messageFieldTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageFieldTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageFieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MessageFieldTypeDto> result = messageFieldTypeService.partialUpdate(messageFieldTypeDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageFieldTypeDto.getId().toString())
        );
    }

    /**
     * {@code GET  /message-field-types} : get all the messageFieldTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageFieldTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MessageFieldTypeDto>> getAllMessageFieldTypes(
        MessageFieldTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MessageFieldTypes by criteria: {}", criteria);

        Page<MessageFieldTypeDto> page = messageFieldTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-field-types/count} : count all the messageFieldTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMessageFieldTypes(MessageFieldTypeCriteria criteria) {
        LOG.debug("REST request to count MessageFieldTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageFieldTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-field-types/:id} : get the "id" messageFieldType.
     *
     * @param id the id of the messageFieldTypeDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageFieldTypeDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageFieldTypeDto> getMessageFieldType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MessageFieldType : {}", id);
        Optional<MessageFieldTypeDto> messageFieldTypeDto = messageFieldTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageFieldTypeDto);
    }

    /**
     * {@code DELETE  /message-field-types/:id} : delete the "id" messageFieldType.
     *
     * @param id the id of the messageFieldTypeDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageFieldType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MessageFieldType : {}", id);
        messageFieldTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
