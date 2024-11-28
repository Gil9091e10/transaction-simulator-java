package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.DebitCardAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.DebitCard;
import com.transaction.project.simulator.app.repository.DebitCardRepository;
import com.transaction.project.simulator.app.service.dto.DebitCardDto;
import com.transaction.project.simulator.app.service.mapper.DebitCardMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DebitCardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DebitCardResourceIT {

    private static final String ENTITY_API_URL = "/api/debit-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private DebitCardMapper debitCardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDebitCardMockMvc;

    private DebitCard debitCard;

    private DebitCard insertedDebitCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DebitCard createEntity() {
        return new DebitCard();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DebitCard createUpdatedEntity() {
        return new DebitCard();
    }

    @BeforeEach
    public void initTest() {
        debitCard = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDebitCard != null) {
            debitCardRepository.delete(insertedDebitCard);
            insertedDebitCard = null;
        }
    }

    @Test
    @Transactional
    void createDebitCard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DebitCard
        DebitCardDto debitCardDto = debitCardMapper.toDto(debitCard);
        var returnedDebitCardDto = om.readValue(
            restDebitCardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(debitCardDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DebitCardDto.class
        );

        // Validate the DebitCard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDebitCard = debitCardMapper.toEntity(returnedDebitCardDto);
        assertDebitCardUpdatableFieldsEquals(returnedDebitCard, getPersistedDebitCard(returnedDebitCard));

        insertedDebitCard = returnedDebitCard;
    }

    @Test
    @Transactional
    void createDebitCardWithExistingId() throws Exception {
        // Create the DebitCard with an existing ID
        debitCard.setId(1L);
        DebitCardDto debitCardDto = debitCardMapper.toDto(debitCard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDebitCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(debitCardDto)))
            .andExpect(status().isBadRequest());

        // Validate the DebitCard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDebitCards() throws Exception {
        // Initialize the database
        insertedDebitCard = debitCardRepository.saveAndFlush(debitCard);

        // Get all the debitCardList
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debitCard.getId().intValue())));
    }

    @Test
    @Transactional
    void getDebitCard() throws Exception {
        // Initialize the database
        insertedDebitCard = debitCardRepository.saveAndFlush(debitCard);

        // Get the debitCard
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL_ID, debitCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(debitCard.getId().intValue()));
    }

    @Test
    @Transactional
    void getDebitCardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDebitCard = debitCardRepository.saveAndFlush(debitCard);

        Long id = debitCard.getId();

        defaultDebitCardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDebitCardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDebitCardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    private void defaultDebitCardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDebitCardShouldBeFound(shouldBeFound);
        defaultDebitCardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDebitCardShouldBeFound(String filter) throws Exception {
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debitCard.getId().intValue())));

        // Check, that the count call also returns 1
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDebitCardShouldNotBeFound(String filter) throws Exception {
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDebitCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDebitCard() throws Exception {
        // Get the debitCard
        restDebitCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteDebitCard() throws Exception {
        // Initialize the database
        insertedDebitCard = debitCardRepository.saveAndFlush(debitCard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the debitCard
        restDebitCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, debitCard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return debitCardRepository.count();
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

    protected DebitCard getPersistedDebitCard(DebitCard debitCard) {
        return debitCardRepository.findById(debitCard.getId()).orElseThrow();
    }

    protected void assertPersistedDebitCardToMatchAllProperties(DebitCard expectedDebitCard) {
        assertDebitCardAllPropertiesEquals(expectedDebitCard, getPersistedDebitCard(expectedDebitCard));
    }

    protected void assertPersistedDebitCardToMatchUpdatableProperties(DebitCard expectedDebitCard) {
        assertDebitCardAllUpdatablePropertiesEquals(expectedDebitCard, getPersistedDebitCard(expectedDebitCard));
    }
}
