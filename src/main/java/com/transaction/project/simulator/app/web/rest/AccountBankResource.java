package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.AccountBankRepository;
import com.transaction.project.simulator.app.service.AccountBankQueryService;
import com.transaction.project.simulator.app.service.AccountBankService;
import com.transaction.project.simulator.app.service.criteria.AccountBankCriteria;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.AccountBank}.
 */
@RestController
@RequestMapping("/api/account-banks")
public class AccountBankResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountBankResource.class);

    private static final String ENTITY_NAME = "accountBank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountBankService accountBankService;

    private final AccountBankRepository accountBankRepository;

    private final AccountBankQueryService accountBankQueryService;

    public AccountBankResource(
        AccountBankService accountBankService,
        AccountBankRepository accountBankRepository,
        AccountBankQueryService accountBankQueryService
    ) {
        this.accountBankService = accountBankService;
        this.accountBankRepository = accountBankRepository;
        this.accountBankQueryService = accountBankQueryService;
    }

    /**
     * {@code POST  /account-banks} : Create a new accountBank.
     *
     * @param accountBankDto the accountBankDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountBankDto, or with status {@code 400 (Bad Request)} if the accountBank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountBankDto> createAccountBank(@Valid @RequestBody AccountBankDto accountBankDto) throws URISyntaxException {
        LOG.debug("REST request to save AccountBank : {}", accountBankDto);
        if (accountBankDto.getId() != null) {
            throw new BadRequestAlertException("A new accountBank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountBankDto = accountBankService.save(accountBankDto);
        return ResponseEntity.created(new URI("/api/account-banks/" + accountBankDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accountBankDto.getId().toString()))
            .body(accountBankDto);
    }

    /**
     * {@code PUT  /account-banks/:id} : Updates an existing accountBank.
     *
     * @param id the id of the accountBankDto to save.
     * @param accountBankDto the accountBankDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBankDto,
     * or with status {@code 400 (Bad Request)} if the accountBankDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountBankDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountBankDto> updateAccountBank(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountBankDto accountBankDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccountBank : {}, {}", id, accountBankDto);
        if (accountBankDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBankDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountBankDto = accountBankService.update(accountBankDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBankDto.getId().toString()))
            .body(accountBankDto);
    }

    /**
     * {@code PATCH  /account-banks/:id} : Partial updates given fields of an existing accountBank, field will ignore if it is null
     *
     * @param id the id of the accountBankDto to save.
     * @param accountBankDto the accountBankDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBankDto,
     * or with status {@code 400 (Bad Request)} if the accountBankDto is not valid,
     * or with status {@code 404 (Not Found)} if the accountBankDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountBankDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountBankDto> partialUpdateAccountBank(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountBankDto accountBankDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AccountBank partially : {}, {}", id, accountBankDto);
        if (accountBankDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBankDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountBankDto> result = accountBankService.partialUpdate(accountBankDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBankDto.getId().toString())
        );
    }

    /**
     * {@code GET  /account-banks} : get all the accountBanks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountBanks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccountBankDto>> getAllAccountBanks(
        AccountBankCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AccountBanks by criteria: {}", criteria);

        Page<AccountBankDto> page = accountBankQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-banks/count} : count all the accountBanks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAccountBanks(AccountBankCriteria criteria) {
        LOG.debug("REST request to count AccountBanks by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountBankQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-banks/:id} : get the "id" accountBank.
     *
     * @param id the id of the accountBankDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountBankDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountBankDto> getAccountBank(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccountBank : {}", id);
        Optional<AccountBankDto> accountBankDto = accountBankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountBankDto);
    }

    /**
     * {@code DELETE  /account-banks/:id} : delete the "id" accountBank.
     *
     * @param id the id of the accountBankDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountBank(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccountBank : {}", id);
        accountBankService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
