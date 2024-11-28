package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.MessageIsoConfigRepository;
import com.transaction.project.simulator.app.service.MessageIsoConfigQueryService;
import com.transaction.project.simulator.app.service.MessageIsoConfigService;
import com.transaction.project.simulator.app.service.criteria.MessageIsoConfigCriteria;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.MessageIsoConfig}.
 */
@RestController
@RequestMapping("/api/message-iso-configs")
public class MessageIsoConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(MessageIsoConfigResource.class);

    private static final String ENTITY_NAME = "messageIsoConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageIsoConfigService messageIsoConfigService;

    private final MessageIsoConfigRepository messageIsoConfigRepository;

    private final MessageIsoConfigQueryService messageIsoConfigQueryService;

    public MessageIsoConfigResource(
        MessageIsoConfigService messageIsoConfigService,
        MessageIsoConfigRepository messageIsoConfigRepository,
        MessageIsoConfigQueryService messageIsoConfigQueryService
    ) {
        this.messageIsoConfigService = messageIsoConfigService;
        this.messageIsoConfigRepository = messageIsoConfigRepository;
        this.messageIsoConfigQueryService = messageIsoConfigQueryService;
    }

    /**
     * {@code POST  /message-iso-configs} : Create a new messageIsoConfig.
     *
     * @param messageIsoConfigDto the messageIsoConfigDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageIsoConfigDto, or with status {@code 400 (Bad Request)} if the messageIsoConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MessageIsoConfigDto> createMessageIsoConfig(@Valid @RequestBody MessageIsoConfigDto messageIsoConfigDto)
        throws URISyntaxException {
        LOG.debug("REST request to save MessageIsoConfig : {}", messageIsoConfigDto);
        if (messageIsoConfigDto.getId() != null) {
            throw new BadRequestAlertException("A new messageIsoConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        messageIsoConfigDto = messageIsoConfigService.save(messageIsoConfigDto);
        return ResponseEntity.created(new URI("/api/message-iso-configs/" + messageIsoConfigDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, messageIsoConfigDto.getId().toString()))
            .body(messageIsoConfigDto);
    }

    /**
     * {@code PUT  /message-iso-configs/:id} : Updates an existing messageIsoConfig.
     *
     * @param id the id of the messageIsoConfigDto to save.
     * @param messageIsoConfigDto the messageIsoConfigDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageIsoConfigDto,
     * or with status {@code 400 (Bad Request)} if the messageIsoConfigDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageIsoConfigDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageIsoConfigDto> updateMessageIsoConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MessageIsoConfigDto messageIsoConfigDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update MessageIsoConfig : {}, {}", id, messageIsoConfigDto);
        if (messageIsoConfigDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageIsoConfigDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageIsoConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        messageIsoConfigDto = messageIsoConfigService.update(messageIsoConfigDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageIsoConfigDto.getId().toString()))
            .body(messageIsoConfigDto);
    }

    /**
     * {@code PATCH  /message-iso-configs/:id} : Partial updates given fields of an existing messageIsoConfig, field will ignore if it is null
     *
     * @param id the id of the messageIsoConfigDto to save.
     * @param messageIsoConfigDto the messageIsoConfigDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageIsoConfigDto,
     * or with status {@code 400 (Bad Request)} if the messageIsoConfigDto is not valid,
     * or with status {@code 404 (Not Found)} if the messageIsoConfigDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the messageIsoConfigDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MessageIsoConfigDto> partialUpdateMessageIsoConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MessageIsoConfigDto messageIsoConfigDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MessageIsoConfig partially : {}, {}", id, messageIsoConfigDto);
        if (messageIsoConfigDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageIsoConfigDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageIsoConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MessageIsoConfigDto> result = messageIsoConfigService.partialUpdate(messageIsoConfigDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageIsoConfigDto.getId().toString())
        );
    }

    /**
     * {@code GET  /message-iso-configs} : get all the messageIsoConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageIsoConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MessageIsoConfigDto>> getAllMessageIsoConfigs(
        MessageIsoConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MessageIsoConfigs by criteria: {}", criteria);

        Page<MessageIsoConfigDto> page = messageIsoConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-iso-configs/count} : count all the messageIsoConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMessageIsoConfigs(MessageIsoConfigCriteria criteria) {
        LOG.debug("REST request to count MessageIsoConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageIsoConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-iso-configs/:id} : get the "id" messageIsoConfig.
     *
     * @param id the id of the messageIsoConfigDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageIsoConfigDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageIsoConfigDto> getMessageIsoConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MessageIsoConfig : {}", id);
        Optional<MessageIsoConfigDto> messageIsoConfigDto = messageIsoConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageIsoConfigDto);
    }

    /**
     * {@code DELETE  /message-iso-configs/:id} : delete the "id" messageIsoConfig.
     *
     * @param id the id of the messageIsoConfigDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageIsoConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MessageIsoConfig : {}", id);
        messageIsoConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
