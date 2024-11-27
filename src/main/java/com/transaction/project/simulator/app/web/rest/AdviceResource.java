package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.AdviceRepository;
import com.transaction.project.simulator.app.service.AdviceQueryService;
import com.transaction.project.simulator.app.service.AdviceService;
import com.transaction.project.simulator.app.service.criteria.AdviceCriteria;
import com.transaction.project.simulator.app.service.dto.AdviceDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Advice}.
 */
@RestController
@RequestMapping("/api/advice")
public class AdviceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceResource.class);

    private static final String ENTITY_NAME = "advice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdviceService adviceService;

    private final AdviceRepository adviceRepository;

    private final AdviceQueryService adviceQueryService;

    public AdviceResource(AdviceService adviceService, AdviceRepository adviceRepository, AdviceQueryService adviceQueryService) {
        this.adviceService = adviceService;
        this.adviceRepository = adviceRepository;
        this.adviceQueryService = adviceQueryService;
    }

    /**
     * {@code POST  /advice} : Create a new advice.
     *
     * @param adviceDto the adviceDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adviceDto, or with status {@code 400 (Bad Request)} if the advice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdviceDto> createAdvice(@Valid @RequestBody AdviceDto adviceDto) throws URISyntaxException {
        LOG.debug("REST request to save Advice : {}", adviceDto);
        if (adviceDto.getId() != null) {
            throw new BadRequestAlertException("A new advice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        adviceDto = adviceService.save(adviceDto);
        return ResponseEntity.created(new URI("/api/advice/" + adviceDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, adviceDto.getId().toString()))
            .body(adviceDto);
    }

    /**
     * {@code PUT  /advice/:id} : Updates an existing advice.
     *
     * @param id the id of the adviceDto to save.
     * @param adviceDto the adviceDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adviceDto,
     * or with status {@code 400 (Bad Request)} if the adviceDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adviceDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdviceDto> updateAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdviceDto adviceDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Advice : {}, {}", id, adviceDto);
        if (adviceDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adviceDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        adviceDto = adviceService.update(adviceDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adviceDto.getId().toString()))
            .body(adviceDto);
    }

    /**
     * {@code PATCH  /advice/:id} : Partial updates given fields of an existing advice, field will ignore if it is null
     *
     * @param id the id of the adviceDto to save.
     * @param adviceDto the adviceDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adviceDto,
     * or with status {@code 400 (Bad Request)} if the adviceDto is not valid,
     * or with status {@code 404 (Not Found)} if the adviceDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the adviceDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdviceDto> partialUpdateAdvice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdviceDto adviceDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Advice partially : {}, {}", id, adviceDto);
        if (adviceDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adviceDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdviceDto> result = adviceService.partialUpdate(adviceDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adviceDto.getId().toString())
        );
    }

    /**
     * {@code GET  /advice} : get all the advice.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of advice in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdviceDto>> getAllAdvice(
        AdviceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Advice by criteria: {}", criteria);

        Page<AdviceDto> page = adviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /advice/count} : count all the advice.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAdvice(AdviceCriteria criteria) {
        LOG.debug("REST request to count Advice by criteria: {}", criteria);
        return ResponseEntity.ok().body(adviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /advice/:id} : get the "id" advice.
     *
     * @param id the id of the adviceDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adviceDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdviceDto> getAdvice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Advice : {}", id);
        Optional<AdviceDto> adviceDto = adviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adviceDto);
    }

    /**
     * {@code DELETE  /advice/:id} : delete the "id" advice.
     *
     * @param id the id of the adviceDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Advice : {}", id);
        adviceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
