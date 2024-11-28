package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.CurrencyAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.repository.CurrencyRepository;
import com.transaction.project.simulator.app.service.dto.CurrencyDto;
import com.transaction.project.simulator.app.service.mapper.CurrencyMapper;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    private Currency insertedCurrency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity() {
        return new Currency().name(DEFAULT_NAME).code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity() {
        return new Currency().name(UPDATED_NAME).code(UPDATED_CODE);
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCurrency != null) {
            currencyRepository.delete(insertedCurrency);
            insertedCurrency = null;
        }
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);
        var returnedCurrencyDto = om.readValue(
            restCurrencyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CurrencyDto.class
        );

        // Validate the Currency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCurrency = currencyMapper.toEntity(returnedCurrencyDto);
        assertCurrencyUpdatableFieldsEquals(returnedCurrency, getPersistedCurrency(returnedCurrency));

        insertedCurrency = returnedCurrency;
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDto)))
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setName(null);

        // Create the Currency, which fails.
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        currency.setCode(null);

        // Create the Currency, which fails.
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        restCurrencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        Long id = currency.getId();

        defaultCurrencyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCurrencyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCurrencyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name equals to
        defaultCurrencyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name in
        defaultCurrencyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name is not null
        defaultCurrencyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name contains
        defaultCurrencyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name does not contain
        defaultCurrencyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code equals to
        defaultCurrencyFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code in
        defaultCurrencyFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code is not null
        defaultCurrencyFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code contains
        defaultCurrencyFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code does not contain
        defaultCurrencyFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    private void defaultCurrencyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCurrencyShouldBeFound(shouldBeFound);
        defaultCurrencyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency.name(UPDATED_NAME).code(UPDATED_CODE);
        CurrencyDto currencyDto = currencyMapper.toDto(updatedCurrency);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDto))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCurrencyToMatchAllProperties(updatedCurrency);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencyDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(currencyDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(currencyDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.code(UPDATED_CODE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurrencyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCurrency, currency), getPersistedCurrency(currency));
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.name(UPDATED_NAME).code(UPDATED_CODE);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCurrencyUpdatableFieldsEquals(partialUpdatedCurrency, getPersistedCurrency(partialUpdatedCurrency));
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencyDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(currencyDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(currencyDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDto currencyDto = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(currencyDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        insertedCurrency = currencyRepository.saveAndFlush(currency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return currencyRepository.count();
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

    protected Currency getPersistedCurrency(Currency currency) {
        return currencyRepository.findById(currency.getId()).orElseThrow();
    }

    protected void assertPersistedCurrencyToMatchAllProperties(Currency expectedCurrency) {
        assertCurrencyAllPropertiesEquals(expectedCurrency, getPersistedCurrency(expectedCurrency));
    }

    protected void assertPersistedCurrencyToMatchUpdatableProperties(Currency expectedCurrency) {
        assertCurrencyAllUpdatablePropertiesEquals(expectedCurrency, getPersistedCurrency(expectedCurrency));
    }
}
