package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.CreditCardAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.transaction.project.simulator.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.CreditCard;
import com.transaction.project.simulator.app.repository.CreditCardRepository;
import com.transaction.project.simulator.app.service.dto.CreditCardDto;
import com.transaction.project.simulator.app.service.mapper.CreditCardMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CreditCardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CreditCardResourceIT {

    private static final BigDecimal DEFAULT_MAX_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_LIMIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAX_LIMIT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/credit-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditCardMockMvc;

    private CreditCard creditCard;

    private CreditCard insertedCreditCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createEntity() {
        return new CreditCard().maxLimit(DEFAULT_MAX_LIMIT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createUpdatedEntity() {
        return new CreditCard().maxLimit(UPDATED_MAX_LIMIT);
    }

    @BeforeEach
    public void initTest() {
        creditCard = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCreditCard != null) {
            creditCardRepository.delete(insertedCreditCard);
            insertedCreditCard = null;
        }
    }

    @Test
    @Transactional
    void createCreditCard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);
        var returnedCreditCardDto = om.readValue(
            restCreditCardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(creditCardDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CreditCardDto.class
        );

        // Validate the CreditCard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCreditCard = creditCardMapper.toEntity(returnedCreditCardDto);
        assertCreditCardUpdatableFieldsEquals(returnedCreditCard, getPersistedCreditCard(returnedCreditCard));

        insertedCreditCard = returnedCreditCard;
    }

    @Test
    @Transactional
    void createCreditCardWithExistingId() throws Exception {
        // Create the CreditCard with an existing ID
        creditCard.setId(1L);
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(creditCardDto)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMaxLimitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        creditCard.setMaxLimit(null);

        // Create the CreditCard, which fails.
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        restCreditCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(creditCardDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreditCards() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxLimit").value(hasItem(sameNumber(DEFAULT_MAX_LIMIT))));
    }

    @Test
    @Transactional
    void getCreditCard() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get the creditCard
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL_ID, creditCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditCard.getId().intValue()))
            .andExpect(jsonPath("$.maxLimit").value(sameNumber(DEFAULT_MAX_LIMIT)));
    }

    @Test
    @Transactional
    void getCreditCardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        Long id = creditCard.getId();

        defaultCreditCardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCreditCardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCreditCardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit equals to
        defaultCreditCardFiltering("maxLimit.equals=" + DEFAULT_MAX_LIMIT, "maxLimit.equals=" + UPDATED_MAX_LIMIT);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit in
        defaultCreditCardFiltering("maxLimit.in=" + DEFAULT_MAX_LIMIT + "," + UPDATED_MAX_LIMIT, "maxLimit.in=" + UPDATED_MAX_LIMIT);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit is not null
        defaultCreditCardFiltering("maxLimit.specified=true", "maxLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit is greater than or equal to
        defaultCreditCardFiltering("maxLimit.greaterThanOrEqual=" + DEFAULT_MAX_LIMIT, "maxLimit.greaterThanOrEqual=" + UPDATED_MAX_LIMIT);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit is less than or equal to
        defaultCreditCardFiltering("maxLimit.lessThanOrEqual=" + DEFAULT_MAX_LIMIT, "maxLimit.lessThanOrEqual=" + SMALLER_MAX_LIMIT);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit is less than
        defaultCreditCardFiltering("maxLimit.lessThan=" + UPDATED_MAX_LIMIT, "maxLimit.lessThan=" + DEFAULT_MAX_LIMIT);
    }

    @Test
    @Transactional
    void getAllCreditCardsByMaxLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList where maxLimit is greater than
        defaultCreditCardFiltering("maxLimit.greaterThan=" + SMALLER_MAX_LIMIT, "maxLimit.greaterThan=" + DEFAULT_MAX_LIMIT);
    }

    private void defaultCreditCardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCreditCardShouldBeFound(shouldBeFound);
        defaultCreditCardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCreditCardShouldBeFound(String filter) throws Exception {
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxLimit").value(hasItem(sameNumber(DEFAULT_MAX_LIMIT))));

        // Check, that the count call also returns 1
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCreditCardShouldNotBeFound(String filter) throws Exception {
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCreditCard() throws Exception {
        // Get the creditCard
        restCreditCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCreditCard() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the creditCard
        CreditCard updatedCreditCard = creditCardRepository.findById(creditCard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCreditCard are not directly saved in db
        em.detach(updatedCreditCard);
        updatedCreditCard.maxLimit(UPDATED_MAX_LIMIT);
        CreditCardDto creditCardDto = creditCardMapper.toDto(updatedCreditCard);

        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(creditCardDto))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCreditCardToMatchAllProperties(updatedCreditCard);
    }

    @Test
    @Transactional
    void putNonExistingCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(creditCardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(creditCardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(creditCardDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreditCardWithPatch() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the creditCard using partial update
        CreditCard partialUpdatedCreditCard = new CreditCard();
        partialUpdatedCreditCard.setId(creditCard.getId());

        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCreditCard))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCreditCardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCreditCard, creditCard),
            getPersistedCreditCard(creditCard)
        );
    }

    @Test
    @Transactional
    void fullUpdateCreditCardWithPatch() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the creditCard using partial update
        CreditCard partialUpdatedCreditCard = new CreditCard();
        partialUpdatedCreditCard.setId(creditCard.getId());

        partialUpdatedCreditCard.maxLimit(UPDATED_MAX_LIMIT);

        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCreditCard))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCreditCardUpdatableFieldsEquals(partialUpdatedCreditCard, getPersistedCreditCard(partialUpdatedCreditCard));
    }

    @Test
    @Transactional
    void patchNonExistingCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditCardDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(creditCardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(creditCardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        creditCard.setId(longCount.incrementAndGet());

        // Create the CreditCard
        CreditCardDto creditCardDto = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(creditCardDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreditCard() throws Exception {
        // Initialize the database
        insertedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the creditCard
        restCreditCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditCard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return creditCardRepository.count();
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

    protected CreditCard getPersistedCreditCard(CreditCard creditCard) {
        return creditCardRepository.findById(creditCard.getId()).orElseThrow();
    }

    protected void assertPersistedCreditCardToMatchAllProperties(CreditCard expectedCreditCard) {
        assertCreditCardAllPropertiesEquals(expectedCreditCard, getPersistedCreditCard(expectedCreditCard));
    }

    protected void assertPersistedCreditCardToMatchUpdatableProperties(CreditCard expectedCreditCard) {
        assertCreditCardAllUpdatablePropertiesEquals(expectedCreditCard, getPersistedCreditCard(expectedCreditCard));
    }
}
