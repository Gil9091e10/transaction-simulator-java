package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.IssuerRepository;
import com.transaction.project.simulator.app.service.IssuerQueryService;
import com.transaction.project.simulator.app.service.IssuerService;
import com.transaction.project.simulator.app.service.criteria.IssuerCriteria;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Issuer}.
 */
@RestController
@RequestMapping("/api/issuers")
public class IssuerResource {

    private static final Logger LOG = LoggerFactory.getLogger(IssuerResource.class);

    private static final String ENTITY_NAME = "issuer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IssuerService issuerService;

    private final IssuerRepository issuerRepository;

    private final IssuerQueryService issuerQueryService;

    public IssuerResource(IssuerService issuerService, IssuerRepository issuerRepository, IssuerQueryService issuerQueryService) {
        this.issuerService = issuerService;
        this.issuerRepository = issuerRepository;
        this.issuerQueryService = issuerQueryService;
    }

    /**
     * {@code POST  /issuers} : Create a new issuer.
     *
     * @param issuerDto the issuerDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new issuerDto, or with status {@code 400 (Bad Request)} if the issuer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IssuerDto> createIssuer(@Valid @RequestBody IssuerDto issuerDto) throws URISyntaxException {
        LOG.debug("REST request to save Issuer : {}", issuerDto);
        if (issuerDto.getId() != null) {
            throw new BadRequestAlertException("A new issuer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        issuerDto = issuerService.save(issuerDto);
        return ResponseEntity.created(new URI("/api/issuers/" + issuerDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, issuerDto.getId().toString()))
            .body(issuerDto);
    }

    /**
     * {@code PUT  /issuers/:id} : Updates an existing issuer.
     *
     * @param id the id of the issuerDto to save.
     * @param issuerDto the issuerDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issuerDto,
     * or with status {@code 400 (Bad Request)} if the issuerDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the issuerDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IssuerDto> updateIssuer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IssuerDto issuerDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Issuer : {}, {}", id, issuerDto);
        if (issuerDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issuerDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!issuerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        issuerDto = issuerService.update(issuerDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, issuerDto.getId().toString()))
            .body(issuerDto);
    }

    /**
     * {@code PATCH  /issuers/:id} : Partial updates given fields of an existing issuer, field will ignore if it is null
     *
     * @param id the id of the issuerDto to save.
     * @param issuerDto the issuerDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issuerDto,
     * or with status {@code 400 (Bad Request)} if the issuerDto is not valid,
     * or with status {@code 404 (Not Found)} if the issuerDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the issuerDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IssuerDto> partialUpdateIssuer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IssuerDto issuerDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Issuer partially : {}, {}", id, issuerDto);
        if (issuerDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issuerDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!issuerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IssuerDto> result = issuerService.partialUpdate(issuerDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, issuerDto.getId().toString())
        );
    }

    /**
     * {@code GET  /issuers} : get all the issuers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issuers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IssuerDto>> getAllIssuers(
        IssuerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Issuers by criteria: {}", criteria);

        Page<IssuerDto> page = issuerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /issuers/count} : count all the issuers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIssuers(IssuerCriteria criteria) {
        LOG.debug("REST request to count Issuers by criteria: {}", criteria);
        return ResponseEntity.ok().body(issuerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /issuers/:id} : get the "id" issuer.
     *
     * @param id the id of the issuerDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the issuerDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IssuerDto> getIssuer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Issuer : {}", id);
        Optional<IssuerDto> issuerDto = issuerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issuerDto);
    }

    /**
     * {@code DELETE  /issuers/:id} : delete the "id" issuer.
     *
     * @param id the id of the issuerDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssuer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Issuer : {}", id);
        issuerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
