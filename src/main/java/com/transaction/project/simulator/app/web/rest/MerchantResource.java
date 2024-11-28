package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.MerchantRepository;
import com.transaction.project.simulator.app.service.MerchantQueryService;
import com.transaction.project.simulator.app.service.MerchantService;
import com.transaction.project.simulator.app.service.criteria.MerchantCriteria;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Merchant}.
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantResource {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantResource.class);

    private static final String ENTITY_NAME = "merchant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MerchantService merchantService;

    private final MerchantRepository merchantRepository;

    private final MerchantQueryService merchantQueryService;

    public MerchantResource(
        MerchantService merchantService,
        MerchantRepository merchantRepository,
        MerchantQueryService merchantQueryService
    ) {
        this.merchantService = merchantService;
        this.merchantRepository = merchantRepository;
        this.merchantQueryService = merchantQueryService;
    }

    /**
     * {@code POST  /merchants} : Create a new merchant.
     *
     * @param merchantDto the merchantDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchantDto, or with status {@code 400 (Bad Request)} if the merchant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MerchantDto> createMerchant(@Valid @RequestBody MerchantDto merchantDto) throws URISyntaxException {
        LOG.debug("REST request to save Merchant : {}", merchantDto);
        if (merchantDto.getId() != null) {
            throw new BadRequestAlertException("A new merchant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        merchantDto = merchantService.save(merchantDto);
        return ResponseEntity.created(new URI("/api/merchants/" + merchantDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, merchantDto.getId().toString()))
            .body(merchantDto);
    }

    /**
     * {@code PUT  /merchants/:id} : Updates an existing merchant.
     *
     * @param id the id of the merchantDto to save.
     * @param merchantDto the merchantDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantDto,
     * or with status {@code 400 (Bad Request)} if the merchantDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchantDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MerchantDto> updateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MerchantDto merchantDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Merchant : {}, {}", id, merchantDto);
        if (merchantDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        merchantDto = merchantService.update(merchantDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantDto.getId().toString()))
            .body(merchantDto);
    }

    /**
     * {@code PATCH  /merchants/:id} : Partial updates given fields of an existing merchant, field will ignore if it is null
     *
     * @param id the id of the merchantDto to save.
     * @param merchantDto the merchantDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantDto,
     * or with status {@code 400 (Bad Request)} if the merchantDto is not valid,
     * or with status {@code 404 (Not Found)} if the merchantDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the merchantDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MerchantDto> partialUpdateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MerchantDto merchantDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Merchant partially : {}, {}", id, merchantDto);
        if (merchantDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MerchantDto> result = merchantService.partialUpdate(merchantDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantDto.getId().toString())
        );
    }

    /**
     * {@code GET  /merchants} : get all the merchants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MerchantDto>> getAllMerchants(
        MerchantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Merchants by criteria: {}", criteria);

        Page<MerchantDto> page = merchantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /merchants/count} : count all the merchants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMerchants(MerchantCriteria criteria) {
        LOG.debug("REST request to count Merchants by criteria: {}", criteria);
        return ResponseEntity.ok().body(merchantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /merchants/:id} : get the "id" merchant.
     *
     * @param id the id of the merchantDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchantDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MerchantDto> getMerchant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Merchant : {}", id);
        Optional<MerchantDto> merchantDto = merchantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(merchantDto);
    }

    /**
     * {@code DELETE  /merchants/:id} : delete the "id" merchant.
     *
     * @param id the id of the merchantDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Merchant : {}", id);
        merchantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
