package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.CurrencyRepository;
import com.transaction.project.simulator.app.service.CurrencyQueryService;
import com.transaction.project.simulator.app.service.CurrencyService;
import com.transaction.project.simulator.app.service.criteria.CurrencyCriteria;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Currency}.
 */
@RestController
@RequestMapping("/api/currencies")
public class CurrencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    private final CurrencyQueryService currencyQueryService;

    public CurrencyResource(
        CurrencyService currencyService,
        CurrencyRepository currencyRepository,
        CurrencyQueryService currencyQueryService
    ) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
        this.currencyQueryService = currencyQueryService;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currencyDto the currencyDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyDto, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CurrencyDto> createCurrency(@Valid @RequestBody CurrencyDto currencyDto) throws URISyntaxException {
        LOG.debug("REST request to save Currency : {}", currencyDto);
        if (currencyDto.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        currencyDto = currencyService.save(currencyDto);
        return ResponseEntity.created(new URI("/api/currencies/" + currencyDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, currencyDto.getId().toString()))
            .body(currencyDto);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currency.
     *
     * @param id the id of the currencyDto to save.
     * @param currencyDto the currencyDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDto,
     * or with status {@code 400 (Bad Request)} if the currencyDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyDto currencyDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Currency : {}, {}", id, currencyDto);
        if (currencyDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        currencyDto = currencyService.update(currencyDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDto.getId().toString()))
            .body(currencyDto);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currency, field will ignore if it is null
     *
     * @param id the id of the currencyDto to save.
     * @param currencyDto the currencyDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDto,
     * or with status {@code 400 (Bad Request)} if the currencyDto is not valid,
     * or with status {@code 404 (Not Found)} if the currencyDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyDto> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyDto currencyDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Currency partially : {}, {}", id, currencyDto);
        if (currencyDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyDto> result = currencyService.partialUpdate(currencyDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDto.getId().toString())
        );
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies(
        CurrencyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Currencies by criteria: {}", criteria);

        Page<CurrencyDto> page = currencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currencies/count} : count all the currencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCurrencies(CurrencyCriteria criteria) {
        LOG.debug("REST request to count Currencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currencyDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDto> currencyDto = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDto);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currencyDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
