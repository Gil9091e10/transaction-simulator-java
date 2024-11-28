package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.AcquirerRepository;
import com.transaction.project.simulator.app.service.AcquirerQueryService;
import com.transaction.project.simulator.app.service.AcquirerService;
import com.transaction.project.simulator.app.service.criteria.AcquirerCriteria;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Acquirer}.
 */
@RestController
@RequestMapping("/api/acquirers")
public class AcquirerResource {

    private static final Logger LOG = LoggerFactory.getLogger(AcquirerResource.class);

    private static final String ENTITY_NAME = "acquirer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcquirerService acquirerService;

    private final AcquirerRepository acquirerRepository;

    private final AcquirerQueryService acquirerQueryService;

    public AcquirerResource(
        AcquirerService acquirerService,
        AcquirerRepository acquirerRepository,
        AcquirerQueryService acquirerQueryService
    ) {
        this.acquirerService = acquirerService;
        this.acquirerRepository = acquirerRepository;
        this.acquirerQueryService = acquirerQueryService;
    }

    /**
     * {@code POST  /acquirers} : Create a new acquirer.
     *
     * @param acquirerDto the acquirerDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acquirerDto, or with status {@code 400 (Bad Request)} if the acquirer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AcquirerDto> createAcquirer(@Valid @RequestBody AcquirerDto acquirerDto) throws URISyntaxException {
        LOG.debug("REST request to save Acquirer : {}", acquirerDto);
        if (acquirerDto.getId() != null) {
            throw new BadRequestAlertException("A new acquirer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        acquirerDto = acquirerService.save(acquirerDto);
        return ResponseEntity.created(new URI("/api/acquirers/" + acquirerDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, acquirerDto.getId().toString()))
            .body(acquirerDto);
    }

    /**
     * {@code PUT  /acquirers/:id} : Updates an existing acquirer.
     *
     * @param id the id of the acquirerDto to save.
     * @param acquirerDto the acquirerDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acquirerDto,
     * or with status {@code 400 (Bad Request)} if the acquirerDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acquirerDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AcquirerDto> updateAcquirer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcquirerDto acquirerDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Acquirer : {}, {}", id, acquirerDto);
        if (acquirerDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acquirerDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acquirerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        acquirerDto = acquirerService.update(acquirerDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acquirerDto.getId().toString()))
            .body(acquirerDto);
    }

    /**
     * {@code PATCH  /acquirers/:id} : Partial updates given fields of an existing acquirer, field will ignore if it is null
     *
     * @param id the id of the acquirerDto to save.
     * @param acquirerDto the acquirerDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acquirerDto,
     * or with status {@code 400 (Bad Request)} if the acquirerDto is not valid,
     * or with status {@code 404 (Not Found)} if the acquirerDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the acquirerDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcquirerDto> partialUpdateAcquirer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcquirerDto acquirerDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Acquirer partially : {}, {}", id, acquirerDto);
        if (acquirerDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acquirerDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acquirerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcquirerDto> result = acquirerService.partialUpdate(acquirerDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acquirerDto.getId().toString())
        );
    }

    /**
     * {@code GET  /acquirers} : get all the acquirers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acquirers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AcquirerDto>> getAllAcquirers(
        AcquirerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Acquirers by criteria: {}", criteria);

        Page<AcquirerDto> page = acquirerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acquirers/count} : count all the acquirers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAcquirers(AcquirerCriteria criteria) {
        LOG.debug("REST request to count Acquirers by criteria: {}", criteria);
        return ResponseEntity.ok().body(acquirerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /acquirers/:id} : get the "id" acquirer.
     *
     * @param id the id of the acquirerDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acquirerDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AcquirerDto> getAcquirer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Acquirer : {}", id);
        Optional<AcquirerDto> acquirerDto = acquirerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acquirerDto);
    }

    /**
     * {@code DELETE  /acquirers/:id} : delete the "id" acquirer.
     *
     * @param id the id of the acquirerDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcquirer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Acquirer : {}", id);
        acquirerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
