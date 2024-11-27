package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.CreditCardRepository;
import com.transaction.project.simulator.app.service.CreditCardQueryService;
import com.transaction.project.simulator.app.service.CreditCardService;
import com.transaction.project.simulator.app.service.criteria.CreditCardCriteria;
import com.transaction.project.simulator.app.service.dto.CreditCardDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.CreditCard}.
 */
@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardResource {

    private static final Logger LOG = LoggerFactory.getLogger(CreditCardResource.class);

    private static final String ENTITY_NAME = "creditCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditCardService creditCardService;

    private final CreditCardRepository creditCardRepository;

    private final CreditCardQueryService creditCardQueryService;

    public CreditCardResource(
        CreditCardService creditCardService,
        CreditCardRepository creditCardRepository,
        CreditCardQueryService creditCardQueryService
    ) {
        this.creditCardService = creditCardService;
        this.creditCardRepository = creditCardRepository;
        this.creditCardQueryService = creditCardQueryService;
    }

    /**
     * {@code POST  /credit-cards} : Create a new creditCard.
     *
     * @param creditCardDto the creditCardDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditCardDto, or with status {@code 400 (Bad Request)} if the creditCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CreditCardDto> createCreditCard(@Valid @RequestBody CreditCardDto creditCardDto) throws URISyntaxException {
        LOG.debug("REST request to save CreditCard : {}", creditCardDto);
        if (creditCardDto.getId() != null) {
            throw new BadRequestAlertException("A new creditCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        creditCardDto = creditCardService.save(creditCardDto);
        return ResponseEntity.created(new URI("/api/credit-cards/" + creditCardDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, creditCardDto.getId().toString()))
            .body(creditCardDto);
    }

    /**
     * {@code PUT  /credit-cards/:id} : Updates an existing creditCard.
     *
     * @param id the id of the creditCardDto to save.
     * @param creditCardDto the creditCardDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardDto,
     * or with status {@code 400 (Bad Request)} if the creditCardDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditCardDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditCardDto> updateCreditCard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreditCardDto creditCardDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update CreditCard : {}, {}", id, creditCardDto);
        if (creditCardDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        creditCardDto = creditCardService.update(creditCardDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardDto.getId().toString()))
            .body(creditCardDto);
    }

    /**
     * {@code PATCH  /credit-cards/:id} : Partial updates given fields of an existing creditCard, field will ignore if it is null
     *
     * @param id the id of the creditCardDto to save.
     * @param creditCardDto the creditCardDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardDto,
     * or with status {@code 400 (Bad Request)} if the creditCardDto is not valid,
     * or with status {@code 404 (Not Found)} if the creditCardDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditCardDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreditCardDto> partialUpdateCreditCard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreditCardDto creditCardDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CreditCard partially : {}, {}", id, creditCardDto);
        if (creditCardDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreditCardDto> result = creditCardService.partialUpdate(creditCardDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardDto.getId().toString())
        );
    }

    /**
     * {@code GET  /credit-cards} : get all the creditCards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditCards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CreditCardDto>> getAllCreditCards(
        CreditCardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CreditCards by criteria: {}", criteria);

        Page<CreditCardDto> page = creditCardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /credit-cards/count} : count all the creditCards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCreditCards(CreditCardCriteria criteria) {
        LOG.debug("REST request to count CreditCards by criteria: {}", criteria);
        return ResponseEntity.ok().body(creditCardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /credit-cards/:id} : get the "id" creditCard.
     *
     * @param id the id of the creditCardDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditCardDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDto> getCreditCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CreditCard : {}", id);
        Optional<CreditCardDto> creditCardDto = creditCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditCardDto);
    }

    /**
     * {@code DELETE  /credit-cards/:id} : delete the "id" creditCard.
     *
     * @param id the id of the creditCardDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CreditCard : {}", id);
        creditCardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
