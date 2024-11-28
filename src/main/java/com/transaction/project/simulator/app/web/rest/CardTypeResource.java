package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.CardTypeRepository;
import com.transaction.project.simulator.app.service.CardTypeQueryService;
import com.transaction.project.simulator.app.service.CardTypeService;
import com.transaction.project.simulator.app.service.criteria.CardTypeCriteria;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.CardType}.
 */
@RestController
@RequestMapping("/api/card-types")
public class CardTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CardTypeResource.class);

    private static final String ENTITY_NAME = "cardType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardTypeService cardTypeService;

    private final CardTypeRepository cardTypeRepository;

    private final CardTypeQueryService cardTypeQueryService;

    public CardTypeResource(
        CardTypeService cardTypeService,
        CardTypeRepository cardTypeRepository,
        CardTypeQueryService cardTypeQueryService
    ) {
        this.cardTypeService = cardTypeService;
        this.cardTypeRepository = cardTypeRepository;
        this.cardTypeQueryService = cardTypeQueryService;
    }

    /**
     * {@code POST  /card-types} : Create a new cardType.
     *
     * @param cardTypeDto the cardTypeDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardTypeDto, or with status {@code 400 (Bad Request)} if the cardType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CardTypeDto> createCardType(@Valid @RequestBody CardTypeDto cardTypeDto) throws URISyntaxException {
        LOG.debug("REST request to save CardType : {}", cardTypeDto);
        if (cardTypeDto.getId() != null) {
            throw new BadRequestAlertException("A new cardType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cardTypeDto = cardTypeService.save(cardTypeDto);
        return ResponseEntity.created(new URI("/api/card-types/" + cardTypeDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cardTypeDto.getId().toString()))
            .body(cardTypeDto);
    }

    /**
     * {@code PUT  /card-types/:id} : Updates an existing cardType.
     *
     * @param id the id of the cardTypeDto to save.
     * @param cardTypeDto the cardTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardTypeDto,
     * or with status {@code 400 (Bad Request)} if the cardTypeDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CardTypeDto> updateCardType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardTypeDto cardTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update CardType : {}, {}", id, cardTypeDto);
        if (cardTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cardTypeDto = cardTypeService.update(cardTypeDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardTypeDto.getId().toString()))
            .body(cardTypeDto);
    }

    /**
     * {@code PATCH  /card-types/:id} : Partial updates given fields of an existing cardType, field will ignore if it is null
     *
     * @param id the id of the cardTypeDto to save.
     * @param cardTypeDto the cardTypeDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardTypeDto,
     * or with status {@code 400 (Bad Request)} if the cardTypeDto is not valid,
     * or with status {@code 404 (Not Found)} if the cardTypeDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardTypeDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardTypeDto> partialUpdateCardType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardTypeDto cardTypeDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CardType partially : {}, {}", id, cardTypeDto);
        if (cardTypeDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardTypeDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardTypeDto> result = cardTypeService.partialUpdate(cardTypeDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardTypeDto.getId().toString())
        );
    }

    /**
     * {@code GET  /card-types} : get all the cardTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CardTypeDto>> getAllCardTypes(
        CardTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CardTypes by criteria: {}", criteria);

        Page<CardTypeDto> page = cardTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-types/count} : count all the cardTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCardTypes(CardTypeCriteria criteria) {
        LOG.debug("REST request to count CardTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-types/:id} : get the "id" cardType.
     *
     * @param id the id of the cardTypeDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardTypeDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardTypeDto> getCardType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CardType : {}", id);
        Optional<CardTypeDto> cardTypeDto = cardTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardTypeDto);
    }

    /**
     * {@code DELETE  /card-types/:id} : delete the "id" cardType.
     *
     * @param id the id of the cardTypeDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CardType : {}", id);
        cardTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
