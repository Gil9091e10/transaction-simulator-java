package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.MessageTypeIndicatorRepository;
import com.transaction.project.simulator.app.service.MessageTypeIndicatorQueryService;
import com.transaction.project.simulator.app.service.MessageTypeIndicatorService;
import com.transaction.project.simulator.app.service.criteria.MessageTypeIndicatorCriteria;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.MessageTypeIndicator}.
 */
@RestController
@RequestMapping("/api/message-type-indicators")
public class MessageTypeIndicatorResource {

    private static final Logger LOG = LoggerFactory.getLogger(MessageTypeIndicatorResource.class);

    private static final String ENTITY_NAME = "messageTypeIndicator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageTypeIndicatorService messageTypeIndicatorService;

    private final MessageTypeIndicatorRepository messageTypeIndicatorRepository;

    private final MessageTypeIndicatorQueryService messageTypeIndicatorQueryService;

    public MessageTypeIndicatorResource(
        MessageTypeIndicatorService messageTypeIndicatorService,
        MessageTypeIndicatorRepository messageTypeIndicatorRepository,
        MessageTypeIndicatorQueryService messageTypeIndicatorQueryService
    ) {
        this.messageTypeIndicatorService = messageTypeIndicatorService;
        this.messageTypeIndicatorRepository = messageTypeIndicatorRepository;
        this.messageTypeIndicatorQueryService = messageTypeIndicatorQueryService;
    }

    /**
     * {@code POST  /message-type-indicators} : Create a new messageTypeIndicator.
     *
     * @param messageTypeIndicatorDto the messageTypeIndicatorDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageTypeIndicatorDto, or with status {@code 400 (Bad Request)} if the messageTypeIndicator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MessageTypeIndicatorDto> createMessageTypeIndicator(
        @Valid @RequestBody MessageTypeIndicatorDto messageTypeIndicatorDto
    ) throws URISyntaxException {
        LOG.debug("REST request to save MessageTypeIndicator : {}", messageTypeIndicatorDto);
        if (messageTypeIndicatorDto.getId() != null) {
            throw new BadRequestAlertException("A new messageTypeIndicator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        messageTypeIndicatorDto = messageTypeIndicatorService.save(messageTypeIndicatorDto);
        return ResponseEntity.created(new URI("/api/message-type-indicators/" + messageTypeIndicatorDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, messageTypeIndicatorDto.getId().toString()))
            .body(messageTypeIndicatorDto);
    }

    /**
     * {@code PUT  /message-type-indicators/:id} : Updates an existing messageTypeIndicator.
     *
     * @param id the id of the messageTypeIndicatorDto to save.
     * @param messageTypeIndicatorDto the messageTypeIndicatorDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageTypeIndicatorDto,
     * or with status {@code 400 (Bad Request)} if the messageTypeIndicatorDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageTypeIndicatorDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageTypeIndicatorDto> updateMessageTypeIndicator(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MessageTypeIndicatorDto messageTypeIndicatorDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update MessageTypeIndicator : {}, {}", id, messageTypeIndicatorDto);
        if (messageTypeIndicatorDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageTypeIndicatorDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageTypeIndicatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        messageTypeIndicatorDto = messageTypeIndicatorService.update(messageTypeIndicatorDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageTypeIndicatorDto.getId().toString()))
            .body(messageTypeIndicatorDto);
    }

    /**
     * {@code PATCH  /message-type-indicators/:id} : Partial updates given fields of an existing messageTypeIndicator, field will ignore if it is null
     *
     * @param id the id of the messageTypeIndicatorDto to save.
     * @param messageTypeIndicatorDto the messageTypeIndicatorDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageTypeIndicatorDto,
     * or with status {@code 400 (Bad Request)} if the messageTypeIndicatorDto is not valid,
     * or with status {@code 404 (Not Found)} if the messageTypeIndicatorDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the messageTypeIndicatorDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MessageTypeIndicatorDto> partialUpdateMessageTypeIndicator(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MessageTypeIndicatorDto messageTypeIndicatorDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MessageTypeIndicator partially : {}, {}", id, messageTypeIndicatorDto);
        if (messageTypeIndicatorDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageTypeIndicatorDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageTypeIndicatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MessageTypeIndicatorDto> result = messageTypeIndicatorService.partialUpdate(messageTypeIndicatorDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageTypeIndicatorDto.getId().toString())
        );
    }

    /**
     * {@code GET  /message-type-indicators} : get all the messageTypeIndicators.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageTypeIndicators in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MessageTypeIndicatorDto>> getAllMessageTypeIndicators(
        MessageTypeIndicatorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MessageTypeIndicators by criteria: {}", criteria);

        Page<MessageTypeIndicatorDto> page = messageTypeIndicatorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-type-indicators/count} : count all the messageTypeIndicators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMessageTypeIndicators(MessageTypeIndicatorCriteria criteria) {
        LOG.debug("REST request to count MessageTypeIndicators by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageTypeIndicatorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-type-indicators/:id} : get the "id" messageTypeIndicator.
     *
     * @param id the id of the messageTypeIndicatorDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageTypeIndicatorDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageTypeIndicatorDto> getMessageTypeIndicator(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MessageTypeIndicator : {}", id);
        Optional<MessageTypeIndicatorDto> messageTypeIndicatorDto = messageTypeIndicatorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageTypeIndicatorDto);
    }

    /**
     * {@code DELETE  /message-type-indicators/:id} : delete the "id" messageTypeIndicator.
     *
     * @param id the id of the messageTypeIndicatorDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageTypeIndicator(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MessageTypeIndicator : {}", id);
        messageTypeIndicatorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
