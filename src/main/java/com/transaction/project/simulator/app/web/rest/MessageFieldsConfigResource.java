package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.MessageFieldsConfigRepository;
import com.transaction.project.simulator.app.service.MessageFieldsConfigQueryService;
import com.transaction.project.simulator.app.service.MessageFieldsConfigService;
import com.transaction.project.simulator.app.service.criteria.MessageFieldsConfigCriteria;
import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.MessageFieldsConfig}.
 */
@RestController
@RequestMapping("/api/message-fields-configs")
public class MessageFieldsConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFieldsConfigResource.class);

    private static final String ENTITY_NAME = "messageFieldsConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageFieldsConfigService messageFieldsConfigService;

    private final MessageFieldsConfigRepository messageFieldsConfigRepository;

    private final MessageFieldsConfigQueryService messageFieldsConfigQueryService;

    public MessageFieldsConfigResource(
        MessageFieldsConfigService messageFieldsConfigService,
        MessageFieldsConfigRepository messageFieldsConfigRepository,
        MessageFieldsConfigQueryService messageFieldsConfigQueryService
    ) {
        this.messageFieldsConfigService = messageFieldsConfigService;
        this.messageFieldsConfigRepository = messageFieldsConfigRepository;
        this.messageFieldsConfigQueryService = messageFieldsConfigQueryService;
    }

    /**
     * {@code POST  /message-fields-configs} : Create a new messageFieldsConfig.
     *
     * @param messageFieldsConfigDto the messageFieldsConfigDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageFieldsConfigDto, or with status {@code 400 (Bad Request)} if the messageFieldsConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MessageFieldsConfigDto> createMessageFieldsConfig(
        @Valid @RequestBody MessageFieldsConfigDto messageFieldsConfigDto
    ) throws URISyntaxException {
        LOG.debug("REST request to save MessageFieldsConfig : {}", messageFieldsConfigDto);
        if (messageFieldsConfigDto.getId() != null) {
            throw new BadRequestAlertException("A new messageFieldsConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        messageFieldsConfigDto = messageFieldsConfigService.save(messageFieldsConfigDto);
        return ResponseEntity.created(new URI("/api/message-fields-configs/" + messageFieldsConfigDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, messageFieldsConfigDto.getId().toString()))
            .body(messageFieldsConfigDto);
    }

    /**
     * {@code PUT  /message-fields-configs/:id} : Updates an existing messageFieldsConfig.
     *
     * @param id the id of the messageFieldsConfigDto to save.
     * @param messageFieldsConfigDto the messageFieldsConfigDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageFieldsConfigDto,
     * or with status {@code 400 (Bad Request)} if the messageFieldsConfigDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageFieldsConfigDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageFieldsConfigDto> updateMessageFieldsConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MessageFieldsConfigDto messageFieldsConfigDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update MessageFieldsConfig : {}, {}", id, messageFieldsConfigDto);
        if (messageFieldsConfigDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageFieldsConfigDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageFieldsConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        messageFieldsConfigDto = messageFieldsConfigService.update(messageFieldsConfigDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageFieldsConfigDto.getId().toString()))
            .body(messageFieldsConfigDto);
    }

    /**
     * {@code PATCH  /message-fields-configs/:id} : Partial updates given fields of an existing messageFieldsConfig, field will ignore if it is null
     *
     * @param id the id of the messageFieldsConfigDto to save.
     * @param messageFieldsConfigDto the messageFieldsConfigDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageFieldsConfigDto,
     * or with status {@code 400 (Bad Request)} if the messageFieldsConfigDto is not valid,
     * or with status {@code 404 (Not Found)} if the messageFieldsConfigDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the messageFieldsConfigDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MessageFieldsConfigDto> partialUpdateMessageFieldsConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MessageFieldsConfigDto messageFieldsConfigDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MessageFieldsConfig partially : {}, {}", id, messageFieldsConfigDto);
        if (messageFieldsConfigDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageFieldsConfigDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageFieldsConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MessageFieldsConfigDto> result = messageFieldsConfigService.partialUpdate(messageFieldsConfigDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageFieldsConfigDto.getId().toString())
        );
    }

    /**
     * {@code GET  /message-fields-configs} : get all the messageFieldsConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageFieldsConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MessageFieldsConfigDto>> getAllMessageFieldsConfigs(
        MessageFieldsConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MessageFieldsConfigs by criteria: {}", criteria);

        Page<MessageFieldsConfigDto> page = messageFieldsConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-fields-configs/count} : count all the messageFieldsConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMessageFieldsConfigs(MessageFieldsConfigCriteria criteria) {
        LOG.debug("REST request to count MessageFieldsConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageFieldsConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-fields-configs/:id} : get the "id" messageFieldsConfig.
     *
     * @param id the id of the messageFieldsConfigDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageFieldsConfigDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MessageFieldsConfigDto> getMessageFieldsConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MessageFieldsConfig : {}", id);
        Optional<MessageFieldsConfigDto> messageFieldsConfigDto = messageFieldsConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageFieldsConfigDto);
    }

    /**
     * {@code DELETE  /message-fields-configs/:id} : delete the "id" messageFieldsConfig.
     *
     * @param id the id of the messageFieldsConfigDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageFieldsConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MessageFieldsConfig : {}", id);
        messageFieldsConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
