package com.transaction.project.simulator.app.web.rest;

import com.transaction.project.simulator.app.repository.CardRepository;
import com.transaction.project.simulator.app.service.CardQueryService;
import com.transaction.project.simulator.app.service.CardService;
import com.transaction.project.simulator.app.service.criteria.CardCriteria;
import com.transaction.project.simulator.app.service.dto.CardDto;
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
 * REST controller for managing {@link com.transaction.project.simulator.app.domain.Card}.
 */
@RestController
@RequestMapping("/api/cards")
public class CardResource {

    private static final Logger LOG = LoggerFactory.getLogger(CardResource.class);

    private static final String ENTITY_NAME = "card";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardService cardService;

    private final CardRepository cardRepository;

    private final CardQueryService cardQueryService;

    public CardResource(CardService cardService, CardRepository cardRepository, CardQueryService cardQueryService) {
        this.cardService = cardService;
        this.cardRepository = cardRepository;
        this.cardQueryService = cardQueryService;
    }

    /**
     * {@code POST  /cards} : Create a new card.
     *
     * @param cardDto the cardDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardDto, or with status {@code 400 (Bad Request)} if the card has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto) throws URISyntaxException {
        LOG.debug("REST request to save Card : {}", cardDto);
        if (cardDto.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cardDto = cardService.save(cardDto);
        return ResponseEntity.created(new URI("/api/cards/" + cardDto.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cardDto.getId().toString()))
            .body(cardDto);
    }

    /**
     * {@code PUT  /cards/:id} : Updates an existing card.
     *
     * @param id the id of the cardDto to save.
     * @param cardDto the cardDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardDto,
     * or with status {@code 400 (Bad Request)} if the cardDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardDto cardDto
    ) throws URISyntaxException {
        LOG.debug("REST request to update Card : {}, {}", id, cardDto);
        if (cardDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cardDto = cardService.update(cardDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardDto.getId().toString()))
            .body(cardDto);
    }

    /**
     * {@code PATCH  /cards/:id} : Partial updates given fields of an existing card, field will ignore if it is null
     *
     * @param id the id of the cardDto to save.
     * @param cardDto the cardDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardDto,
     * or with status {@code 400 (Bad Request)} if the cardDto is not valid,
     * or with status {@code 404 (Not Found)} if the cardDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardDto> partialUpdateCard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardDto cardDto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Card partially : {}, {}", id, cardDto);
        if (cardDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardDto> result = cardService.partialUpdate(cardDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardDto.getId().toString())
        );
    }

    /**
     * {@code GET  /cards} : get all the cards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CardDto>> getAllCards(
        CardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cards by criteria: {}", criteria);

        Page<CardDto> page = cardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cards/count} : count all the cards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCards(CardCriteria criteria) {
        LOG.debug("REST request to count Cards by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cards/:id} : get the "id" card.
     *
     * @param id the id of the cardDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Card : {}", id);
        Optional<CardDto> cardDto = cardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardDto);
    }

    /**
     * {@code DELETE  /cards/:id} : delete the "id" card.
     *
     * @param id the id of the cardDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Card : {}", id);
        cardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
