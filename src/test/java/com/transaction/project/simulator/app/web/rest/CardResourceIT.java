package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.CardAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.AccountBank;
import com.transaction.project.simulator.app.domain.Card;
import com.transaction.project.simulator.app.domain.CardType;
import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.repository.CardRepository;
import com.transaction.project.simulator.app.service.dto.CardDto;
import com.transaction.project.simulator.app.service.mapper.CardMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardResourceIT {

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final Long SMALLER_NUMBER = 1L - 1L;

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_VERIFICATION_VALUE = 1;
    private static final Integer UPDATED_VERIFICATION_VALUE = 2;
    private static final Integer SMALLER_VERIFICATION_VALUE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    private Card insertedCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity() {
        return new Card().number(DEFAULT_NUMBER).expirationDate(DEFAULT_EXPIRATION_DATE).verificationValue(DEFAULT_VERIFICATION_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity() {
        return new Card().number(UPDATED_NUMBER).expirationDate(UPDATED_EXPIRATION_DATE).verificationValue(UPDATED_VERIFICATION_VALUE);
    }

    @BeforeEach
    public void initTest() {
        card = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCard != null) {
            cardRepository.delete(insertedCard);
            insertedCard = null;
        }
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);
        var returnedCardDto = om.readValue(
            restCardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CardDto.class
        );

        // Validate the Card in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCard = cardMapper.toEntity(returnedCardDto);
        assertCardUpdatableFieldsEquals(returnedCard, getPersistedCard(returnedCard));

        insertedCard = returnedCard;
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        // Create the Card with an existing ID
        card.setId(1L);
        CardDto cardDto = cardMapper.toDto(card);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        card.setNumber(null);

        // Create the Card, which fails.
        CardDto cardDto = cardMapper.toDto(card);

        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].verificationValue").value(hasItem(DEFAULT_VERIFICATION_VALUE)));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get(ENTITY_API_URL_ID, card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.verificationValue").value(DEFAULT_VERIFICATION_VALUE));
    }

    @Test
    @Transactional
    void getCardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        Long id = card.getId();

        defaultCardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number equals to
        defaultCardFiltering("number.equals=" + DEFAULT_NUMBER, "number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number in
        defaultCardFiltering("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER, "number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number is not null
        defaultCardFiltering("number.specified=true", "number.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number is greater than or equal to
        defaultCardFiltering("number.greaterThanOrEqual=" + DEFAULT_NUMBER, "number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number is less than or equal to
        defaultCardFiltering("number.lessThanOrEqual=" + DEFAULT_NUMBER, "number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number is less than
        defaultCardFiltering("number.lessThan=" + UPDATED_NUMBER, "number.lessThan=" + DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where number is greater than
        defaultCardFiltering("number.greaterThan=" + SMALLER_NUMBER, "number.greaterThan=" + DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate equals to
        defaultCardFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate in
        defaultCardFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate is not null
        defaultCardFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate is greater than or equal to
        defaultCardFiltering(
            "expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate is less than or equal to
        defaultCardFiltering(
            "expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate is less than
        defaultCardFiltering("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE, "expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCardsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where expirationDate is greater than
        defaultCardFiltering(
            "expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE,
            "expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue equals to
        defaultCardFiltering(
            "verificationValue.equals=" + DEFAULT_VERIFICATION_VALUE,
            "verificationValue.equals=" + UPDATED_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue in
        defaultCardFiltering(
            "verificationValue.in=" + DEFAULT_VERIFICATION_VALUE + "," + UPDATED_VERIFICATION_VALUE,
            "verificationValue.in=" + UPDATED_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue is not null
        defaultCardFiltering("verificationValue.specified=true", "verificationValue.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue is greater than or equal to
        defaultCardFiltering(
            "verificationValue.greaterThanOrEqual=" + DEFAULT_VERIFICATION_VALUE,
            "verificationValue.greaterThanOrEqual=" + UPDATED_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue is less than or equal to
        defaultCardFiltering(
            "verificationValue.lessThanOrEqual=" + DEFAULT_VERIFICATION_VALUE,
            "verificationValue.lessThanOrEqual=" + SMALLER_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue is less than
        defaultCardFiltering(
            "verificationValue.lessThan=" + UPDATED_VERIFICATION_VALUE,
            "verificationValue.lessThan=" + DEFAULT_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByVerificationValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        // Get all the cardList where verificationValue is greater than
        defaultCardFiltering(
            "verificationValue.greaterThan=" + SMALLER_VERIFICATION_VALUE,
            "verificationValue.greaterThan=" + DEFAULT_VERIFICATION_VALUE
        );
    }

    @Test
    @Transactional
    void getAllCardsByAccountBankIsEqualToSomething() throws Exception {
        AccountBank accountBank;
        if (TestUtil.findAll(em, AccountBank.class).isEmpty()) {
            cardRepository.saveAndFlush(card);
            accountBank = AccountBankResourceIT.createEntity();
        } else {
            accountBank = TestUtil.findAll(em, AccountBank.class).get(0);
        }
        em.persist(accountBank);
        em.flush();
        card.setAccountBank(accountBank);
        cardRepository.saveAndFlush(card);
        Long accountBankId = accountBank.getId();
        // Get all the cardList where accountBank equals to accountBankId
        defaultCardShouldBeFound("accountBankId.equals=" + accountBankId);

        // Get all the cardList where accountBank equals to (accountBankId + 1)
        defaultCardShouldNotBeFound("accountBankId.equals=" + (accountBankId + 1));
    }

    @Test
    @Transactional
    void getAllCardsByCardTypeIsEqualToSomething() throws Exception {
        CardType cardType;
        if (TestUtil.findAll(em, CardType.class).isEmpty()) {
            cardRepository.saveAndFlush(card);
            cardType = CardTypeResourceIT.createEntity();
        } else {
            cardType = TestUtil.findAll(em, CardType.class).get(0);
        }
        em.persist(cardType);
        em.flush();
        card.setCardType(cardType);
        cardRepository.saveAndFlush(card);
        Long cardTypeId = cardType.getId();
        // Get all the cardList where cardType equals to cardTypeId
        defaultCardShouldBeFound("cardTypeId.equals=" + cardTypeId);

        // Get all the cardList where cardType equals to (cardTypeId + 1)
        defaultCardShouldNotBeFound("cardTypeId.equals=" + (cardTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardsByIssuerIsEqualToSomething() throws Exception {
        Issuer issuer;
        if (TestUtil.findAll(em, Issuer.class).isEmpty()) {
            cardRepository.saveAndFlush(card);
            issuer = IssuerResourceIT.createEntity();
        } else {
            issuer = TestUtil.findAll(em, Issuer.class).get(0);
        }
        em.persist(issuer);
        em.flush();
        card.setIssuer(issuer);
        cardRepository.saveAndFlush(card);
        Long issuerId = issuer.getId();
        // Get all the cardList where issuer equals to issuerId
        defaultCardShouldBeFound("issuerId.equals=" + issuerId);

        // Get all the cardList where issuer equals to (issuerId + 1)
        defaultCardShouldNotBeFound("issuerId.equals=" + (issuerId + 1));
    }

    private void defaultCardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCardShouldBeFound(shouldBeFound);
        defaultCardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardShouldBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].verificationValue").value(hasItem(DEFAULT_VERIFICATION_VALUE)));

        // Check, that the count call also returns 1
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardShouldNotBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard.number(UPDATED_NUMBER).expirationDate(UPDATED_EXPIRATION_DATE).verificationValue(UPDATED_VERIFICATION_VALUE);
        CardDto cardDto = cardMapper.toDto(updatedCard);

        restCardMockMvc
            .perform(put(ENTITY_API_URL_ID, cardDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCardToMatchAllProperties(updatedCard);
    }

    @Test
    @Transactional
    void putNonExistingCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL_ID, cardDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardWithPatch() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.number(UPDATED_NUMBER).expirationDate(UPDATED_EXPIRATION_DATE);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCard, card), getPersistedCard(card));
    }

    @Test
    @Transactional
    void fullUpdateCardWithPatch() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.number(UPDATED_NUMBER).expirationDate(UPDATED_EXPIRATION_DATE).verificationValue(UPDATED_VERIFICATION_VALUE);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCardUpdatableFieldsEquals(partialUpdatedCard, getPersistedCard(partialUpdatedCard));
    }

    @Test
    @Transactional
    void patchNonExistingCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardDto.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cardDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        card.setId(longCount.incrementAndGet());

        // Create the Card
        CardDto cardDto = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cardDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        insertedCard = cardRepository.saveAndFlush(card);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the card
        restCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, card.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cardRepository.count();
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

    protected Card getPersistedCard(Card card) {
        return cardRepository.findById(card.getId()).orElseThrow();
    }

    protected void assertPersistedCardToMatchAllProperties(Card expectedCard) {
        assertCardAllPropertiesEquals(expectedCard, getPersistedCard(expectedCard));
    }

    protected void assertPersistedCardToMatchUpdatableProperties(Card expectedCard) {
        assertCardAllUpdatablePropertiesEquals(expectedCard, getPersistedCard(expectedCard));
    }
}
