package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.TransactionTypeRepository;
import com.transaction.project.simulator.app.service.TransactionTypeQueryService;
import com.transaction.project.simulator.app.service.TransactionTypeService;
import com.transaction.project.simulator.app.service.criteria.TransactionTypeCriteria;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.TransactionType}.
 */
@RestController
@RequestMapping("/api/transaction-types")
public class TransactionTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionTypeResource.class);

    private static final String ENTITY_NAME = "transactionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionTypeService transactionTypeService;

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeQueryService transactionTypeQueryService;

    public TransactionTypeResource(
        TransactionTypeService transactionTypeService,
        TransactionTypeRepository transactionTypeRepository,
        TransactionTypeQueryService transactionTypeQueryService
    ) {
        this.transactionTypeService = transactionTypeService;
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeQueryService = transactionTypeQueryService;
    }

    /**
     * {@code POST  /transaction-types} : Create a new transactionType.
     *
     * @param transactionTypeDto the transactionTypeDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionTypeDto, or with status {@code 400 (Bad Request)} if the transactionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionTypeDto> createTransactionType(@Valid @RequestBody TransactionTypeDto transactionTypeDto)
        throws URISyntaxException {
        LOG.debug("REST request to save TransactionType : {}", transactionTypeDto);
        if (transactionTypeDto.getId() != null) {
            throw new BadRequestAlertException("A new transactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionTypeDto = transactionTypeService.save(transactionTypeDto);
        return ResponseEntity.created(new URI("/api/transaction-types/" + transactionTypeDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transactionTypeDto.getId().toString()))
            .body(transactionTypeDto);
    }

    /**
     * {@code PUT  /transaction-types/:id} : Updates an existing transactionType.
     *
     * @param id the id of the transactionTypeDto to save.
     * @param transactionTypeDto the transactionTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDto,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionTypeDto> updateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionTypeDto transactionTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransactionType : {}, {}", id, transactionTypeDto);
        if (transactionTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionTypeDto = transactionTypeService.update(transactionTypeDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionTypeDto.getId().toString()))
            .body(transactionTypeDto);
    }

    /**
     * {@code PATCH  /transaction-types/:id} : Partial updates given fields of an existing transactionType, field will ignore if it is null
     *
     * @param id the id of the transactionTypeDto to save.
     * @param transactionTypeDto the transactionTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDto,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDto is not valid,
     * or with status {@code 404 (Not Found)} if the transactionTypeDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionTypeDto> partialUpdateTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionTypeDto transactionTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransactionType partially : {}, {}", id, transactionTypeDto);
        if (transactionTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionTypeDto> result = transactionTypeService.partialUpdate(transactionTypeDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionTypeDto.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-types} : get all the transactionTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionTypeDto>> getAllTransactionTypes(
        TransactionTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransactionTypes by criteria: {}", criteria);

        Page<TransactionTypeDto> page = transactionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-types/count} : count all the transactionTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransactionTypes(TransactionTypeCriteria criteria) {
        LOG.debug("REST request to count TransactionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-types/:id} : get the "id" transactionType.
     *
     * @param id the id of the transactionTypeDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionTypeDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionTypeDto> getTransactionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransactionType : {}", id);
        Optional<TransactionTypeDto> transactionTypeDto = transactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionTypeDto);
    }

    /**
     * {@code DELETE  /transaction-types/:id} : delete the "id" transactionType.
     *
     * @param id the id of the transactionTypeDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransactionType : {}", id);
        transactionTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
