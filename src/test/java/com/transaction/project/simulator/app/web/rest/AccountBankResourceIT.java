package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.AccountBankAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.transaction.project.simulator.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.domain.Currency;
import com.transaction.project.simulator.app.repository.AccountBankRepository;
import com.transaction.project.simulator.app.service.dto.AccountBankDto;
import com.transaction.project.simulator.app.service.mapper.AccountBankMapper;
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
 * Integration tests for the {@link AccountBankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountBankResourceIT {

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final Long SMALLER_NUMBER = 1L - 1L;

    private static final String DEFAULT_NUMBER_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_IBAN = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/account-banks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountBankRepository accountBankRepository;

    @Autowired
    private AccountBankMapper accountBankMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountBankMockMvc;

    private AccountBank accountBank;

    private AccountBank insertedAccountBank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBank createEntity() {
        return new AccountBank().number(DEFAULT_NUMBER).numberIBAN(DEFAULT_NUMBER_IBAN).balance(DEFAULT_BALANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBank createUpdatedEntity() {
        return new AccountBank().number(UPDATED_NUMBER).numberIBAN(UPDATED_NUMBER_IBAN).balance(UPDATED_BALANCE);
    }

    @BeforeEach
    public void initTest() {
        accountBank = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccountBank != null) {
            accountBankRepository.delete(insertedAccountBank);
            insertedAccountBank = null;
        }
    }

    @Test
    @Transactional
    void createAccountBank() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);
        var returnedAccountBankDto = om.readValue(
            restAccountBankMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountBankDto.class
        );

        // Validate the AccountBank in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccountBank = accountBankMapper.toEntity(returnedAccountBankDto);
        assertAccountBankUpdatableFieldsEquals(returnedAccountBank, getPersistedAccountBank(returnedAccountBank));

        insertedAccountBank = returnedAccountBank;
    }

    @Test
    @Transactional
    void createAccountBankWithExistingId() throws Exception {
        // Create the AccountBank with an existing ID
        accountBank.setId(1L);
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isBadRequest());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accountBank.setNumber(null);

        // Create the AccountBank, which fails.
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        restAccountBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberIBANIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accountBank.setNumberIBAN(null);

        // Create the AccountBank, which fails.
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        restAccountBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accountBank.setBalance(null);

        // Create the AccountBank, which fails.
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        restAccountBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountBanks() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBank.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].numberIBAN").value(hasItem(DEFAULT_NUMBER_IBAN)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));
    }

    @Test
    @Transactional
    void getAccountBank() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get the accountBank
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL_ID, accountBank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountBank.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.numberIBAN").value(DEFAULT_NUMBER_IBAN))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    void getAccountBanksByIdFiltering() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        Long id = accountBank.getId();

        defaultAccountBankFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAccountBankFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAccountBankFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number equals to
        defaultAccountBankFiltering("number.equals=" + DEFAULT_NUMBER, "number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number in
        defaultAccountBankFiltering("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER, "number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number is not null
        defaultAccountBankFiltering("number.specified=true", "number.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number is greater than or equal to
        defaultAccountBankFiltering("number.greaterThanOrEqual=" + DEFAULT_NUMBER, "number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number is less than or equal to
        defaultAccountBankFiltering("number.lessThanOrEqual=" + DEFAULT_NUMBER, "number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number is less than
        defaultAccountBankFiltering("number.lessThan=" + UPDATED_NUMBER, "number.lessThan=" + DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where number is greater than
        defaultAccountBankFiltering("number.greaterThan=" + SMALLER_NUMBER, "number.greaterThan=" + DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIBANIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where numberIBAN equals to
        defaultAccountBankFiltering("numberIBAN.equals=" + DEFAULT_NUMBER_IBAN, "numberIBAN.equals=" + UPDATED_NUMBER_IBAN);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIBANIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where numberIBAN in
        defaultAccountBankFiltering(
            "numberIBAN.in=" + DEFAULT_NUMBER_IBAN + "," + UPDATED_NUMBER_IBAN,
            "numberIBAN.in=" + UPDATED_NUMBER_IBAN
        );
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIBANIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where numberIBAN is not null
        defaultAccountBankFiltering("numberIBAN.specified=true", "numberIBAN.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIBANContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where numberIBAN contains
        defaultAccountBankFiltering("numberIBAN.contains=" + DEFAULT_NUMBER_IBAN, "numberIBAN.contains=" + UPDATED_NUMBER_IBAN);
    }

    @Test
    @Transactional
    void getAllAccountBanksByNumberIBANNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where numberIBAN does not contain
        defaultAccountBankFiltering("numberIBAN.doesNotContain=" + UPDATED_NUMBER_IBAN, "numberIBAN.doesNotContain=" + DEFAULT_NUMBER_IBAN);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance equals to
        defaultAccountBankFiltering("balance.equals=" + DEFAULT_BALANCE, "balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance in
        defaultAccountBankFiltering("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE, "balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance is not null
        defaultAccountBankFiltering("balance.specified=true", "balance.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance is greater than or equal to
        defaultAccountBankFiltering("balance.greaterThanOrEqual=" + DEFAULT_BALANCE, "balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance is less than or equal to
        defaultAccountBankFiltering("balance.lessThanOrEqual=" + DEFAULT_BALANCE, "balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance is less than
        defaultAccountBankFiltering("balance.lessThan=" + UPDATED_BALANCE, "balance.lessThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        // Get all the accountBankList where balance is greater than
        defaultAccountBankFiltering("balance.greaterThan=" + SMALLER_BALANCE, "balance.greaterThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountBanksByCurrencyIsEqualToSomething() throws Exception {
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            accountBankRepository.saveAndFlush(accountBank);
            currency = CurrencyResourceIT.createEntity();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        em.persist(currency);
        em.flush();
        accountBank.setCurrency(currency);
        accountBankRepository.saveAndFlush(accountBank);
        Long currencyId = currency.getId();
        // Get all the accountBankList where currency equals to currencyId
        defaultAccountBankShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the accountBankList where currency equals to (currencyId + 1)
        defaultAccountBankShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    private void defaultAccountBankFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAccountBankShouldBeFound(shouldBeFound);
        defaultAccountBankShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountBankShouldBeFound(String filter) throws Exception {
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBank.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].numberIBAN").value(hasItem(DEFAULT_NUMBER_IBAN)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));

        // Check, that the count call also returns 1
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountBankShouldNotBeFound(String filter) throws Exception {
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountBankMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountBank() throws Exception {
        // Get the accountBank
        restAccountBankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountBank() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBank
        AccountBank updatedAccountBank = accountBankRepository.findById(accountBank.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountBank are not directly saved in db
        em.detach(updatedAccountBank);
        updatedAccountBank.number(UPDATED_NUMBER).numberIBAN(UPDATED_NUMBER_IBAN).balance(UPDATED_BALANCE);
        AccountBankDto accountBankDto = accountBankMapper.toDto(updatedAccountBank);

        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBankDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountBankDto))
            )
            .andExpect(status().isOk());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountBankToMatchAllProperties(updatedAccountBank);
    }

    @Test
    @Transactional
    void putNonExistingAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBankDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountBankDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountBankDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountBankWithPatch() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBank using partial update
        AccountBank partialUpdatedAccountBank = new AccountBank();
        partialUpdatedAccountBank.setId(accountBank.getId());

        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountBank))
            )
            .andExpect(status().isOk());

        // Validate the AccountBank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountBankUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountBank, accountBank),
            getPersistedAccountBank(accountBank)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountBankWithPatch() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBank using partial update
        AccountBank partialUpdatedAccountBank = new AccountBank();
        partialUpdatedAccountBank.setId(accountBank.getId());

        partialUpdatedAccountBank.number(UPDATED_NUMBER).numberIBAN(UPDATED_NUMBER_IBAN).balance(UPDATED_BALANCE);

        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountBank))
            )
            .andExpect(status().isOk());

        // Validate the AccountBank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountBankUpdatableFieldsEquals(partialUpdatedAccountBank, getPersistedAccountBank(partialUpdatedAccountBank));
    }

    @Test
    @Transactional
    void patchNonExistingAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountBankDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountBankDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountBankDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBank.setId(longCount.incrementAndGet());

        // Create the AccountBank
        AccountBankDto accountBankDto = accountBankMapper.toDto(accountBank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBankMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountBankDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountBank() throws Exception {
        // Initialize the database
        insertedAccountBank = accountBankRepository.saveAndFlush(accountBank);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accountBank
        restAccountBankMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountBank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accountBankRepository.count();
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

    protected AccountBank getPersistedAccountBank(AccountBank accountBank) {
        return accountBankRepository.findById(accountBank.getId()).orElseThrow();
    }

    protected void assertPersistedAccountBankToMatchAllProperties(AccountBank expectedAccountBank) {
        assertAccountBankAllPropertiesEquals(expectedAccountBank, getPersistedAccountBank(expectedAccountBank));
    }

    protected void assertPersistedAccountBankToMatchUpdatableProperties(AccountBank expectedAccountBank) {
        assertAccountBankAllUpdatablePropertiesEquals(expectedAccountBank, getPersistedAccountBank(expectedAccountBank));
    }
}
