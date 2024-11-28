package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.CardTypeAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.repository.CardTypeRepository;
import com.transaction.project.simulator.app.service.dto.CardTypeDto;
import com.transaction.project.simulator.app.service.mapper.CardTypeMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CardTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardTypeMockMvc;

    private CardType cardType;

    private CardType insertedCardType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardType createEntity() {
        return new CardType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardType createUpdatedEntity() {
        return new CardType().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        cardType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCardType != null) {
            cardTypeRepository.delete(insertedCardType);
            insertedCardType = null;
        }
    }

    @Test
    @Transactional
    void createCardType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);
        var returnedCardTypeDto = om.readValue(
            restCardTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardTypeDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CardTypeDto.class
        );

        // Validate the CardType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCardType = cardTypeMapper.toEntity(returnedCardTypeDto);
        assertCardTypeUpdatableFieldsEquals(returnedCardType, getPersistedCardType(returnedCardType));

        insertedCardType = returnedCardType;
    }

    @Test
    @Transactional
    void createCardTypeWithExistingId() throws Exception {
        // Create the CardType with an existing ID
        cardType.setId(1L);
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardTypeDto)))
            .andExpect(status().isBadRequest());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cardType.setName(null);

        // Create the CardType, which fails.
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        restCardTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardTypeDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardTypes() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCardType() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get the cardType
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cardType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCardTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        Long id = cardType.getId();

        defaultCardTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCardTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCardTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList where name equals to
        defaultCardTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCardTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList where name in
        defaultCardTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCardTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList where name is not null
        defaultCardTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCardTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList where name contains
        defaultCardTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCardTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        // Get all the cardTypeList where name does not contain
        defaultCardTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultCardTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCardTypeShouldBeFound(shouldBeFound);
        defaultCardTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardTypeShouldBeFound(String filter) throws Exception {
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardTypeShouldNotBeFound(String filter) throws Exception {
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardType() throws Exception {
        // Get the cardType
        restCardTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCardType() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardType
        CardType updatedCardType = cardTypeRepository.findById(cardType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCardType are not directly saved in db
        em.detach(updatedCardType);
        updatedCardType.name(UPDATED_NAME);
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(updatedCardType);

        restCardTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardTypeDto))
            )
            .andExpect(status().isOk());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCardTypeToMatchAllProperties(updatedCardType);
    }

    @Test
    @Transactional
    void putNonExistingCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardType using partial update
        CardType partialUpdatedCardType = new CardType();
        partialUpdatedCardType.setId(cardType.getId());

        partialUpdatedCardType.name(UPDATED_NAME);

        restCardTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCardType))
            )
            .andExpect(status().isOk());

        // Validate the CardType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCardType, cardType), getPersistedCardType(cardType));
    }

    @Test
    @Transactional
    void fullUpdateCardTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cardType using partial update
        CardType partialUpdatedCardType = new CardType();
        partialUpdatedCardType.setId(cardType.getId());

        partialUpdatedCardType.name(UPDATED_NAME);

        restCardTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCardType))
            )
            .andExpect(status().isOk());

        // Validate the CardType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardTypeUpdatableFieldsEquals(partialUpdatedCardType, getPersistedCardType(partialUpdatedCardType));
    }

    @Test
    @Transactional
    void patchNonExistingCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardTypeDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cardType.setId(longCount.incrementAndGet());

        // Create the CardType
        CardTypeDto cardTypeDto = cardTypeMapper.toDto(cardType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCardType() throws Exception {
        // Initialize the database
        insertedCardType = cardTypeRepository.saveAndFlush(cardType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cardType
        restCardTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cardTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CardType getPersistedCardType(CardType cardType) {
        return cardTypeRepository.findById(cardType.getId()).orElseThrow();
    }

    protected void assertPersistedCardTypeToMatchAllProperties(CardType expectedCardType) {
        assertCardTypeAllPropertiesEquals(expectedCardType, getPersistedCardType(expectedCardType));
    }

    protected void assertPersistedCardTypeToMatchUpdatableProperties(CardType expectedCardType) {
        assertCardTypeAllUpdatablePropertiesEquals(expectedCardType, getPersistedCardType(expectedCardType));
    }
}
