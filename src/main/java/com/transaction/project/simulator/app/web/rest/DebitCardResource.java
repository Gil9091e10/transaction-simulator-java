package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.DebitCardRepository;
import com.transaction.project.simulator.app.service.DebitCardQueryService;
import com.transaction.project.simulator.app.service.DebitCardService;
import com.transaction.project.simulator.app.service.criteria.DebitCardCriteria;
import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import com.transaction.project.simulator.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.DebitCard}.
 */
@RestController
@RequestMapping("/api/debit-cards")
public class DebitCardResource {

    private static final Logger LOG = LoggerFactory.getLogger(DebitCardResource.class);

    private static final String ENTITY_NAME = "debitCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DebitCardService debitCardService;

    private final DebitCardRepository debitCardRepository;

    private final DebitCardQueryService debitCardQueryService;

    public DebitCardResource(
        DebitCardService debitCardService,
        DebitCardRepository debitCardRepository,
        DebitCardQueryService debitCardQueryService
    ) {
        this.debitCardService = debitCardService;
        this.debitCardRepository = debitCardRepository;
        this.debitCardQueryService = debitCardQueryService;
    }

    /**
     * {@code POST  /debit-cards} : Create a new debitCard.
     *
     * @param debitCardDto the debitCardDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new debitCardDto, or with status {@code 400 (Bad Request)} if the debitCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DebitCardDto> createDebitCard(@RequestBody DebitCardDto debitCardDto) throws URISyntaxException {
        LOG.debug("REST request to save DebitCard : {}", debitCardDto);
        if (debitCardDto.getId() != null) {
            throw new BadRequestAlertException("A new debitCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        debitCardDto = debitCardService.save(debitCardDto);
        return ResponseEntity.created(new URI("/api/debit-cards/" + debitCardDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, debitCardDto.getId().toString()))
            .body(debitCardDto);
    }

    /**
     * {@code GET  /debit-cards} : get all the debitCards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of debitCards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DebitCardDto>> getAllDebitCards(
        DebitCardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DebitCards by criteria: {}", criteria);

        Page<DebitCardDto> page = debitCardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /debit-cards/count} : count all the debitCards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDebitCards(DebitCardCriteria criteria) {
        LOG.debug("REST request to count DebitCards by criteria: {}", criteria);
        return ResponseEntity.ok().body(debitCardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /debit-cards/:id} : get the "id" debitCard.
     *
     * @param id the id of the debitCardDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the debitCardDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DebitCardDto> getDebitCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DebitCard : {}", id);
        Optional<DebitCardDto> debitCardDto = debitCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(debitCardDto);
    }

    /**
     * {@code DELETE  /debit-cards/:id} : delete the "id" debitCard.
     *
     * @param id the id of the debitCardDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebitCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DebitCard : {}", id);
        debitCardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
