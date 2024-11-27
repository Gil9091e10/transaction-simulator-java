package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.MessageTypeIndicatorAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.repository.MessageTypeIndicatorRepository;
import com.transaction.project.simulator.app.service.dto.MessageTypeIndicatorDto;
import com.transaction.project.simulator.app.service.mapper.MessageTypeIndicatorMapper;
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
 * Integration tests for the {@link MessageTypeIndicatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageTypeIndicatorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAA";
    private static final String UPDATED_CODE = "BBBB";

    private static final String ENTITY_API_URL = "/api/message-type-indicators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageTypeIndicatorRepository messageTypeIndicatorRepository;

    @Autowired
    private MessageTypeIndicatorMapper messageTypeIndicatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageTypeIndicatorMockMvc;

    private MessageTypeIndicator messageTypeIndicator;

    private MessageTypeIndicator insertedMessageTypeIndicator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageTypeIndicator createEntity() {
        return new MessageTypeIndicator().name(DEFAULT_NAME).code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageTypeIndicator createUpdatedEntity() {
        return new MessageTypeIndicator().name(UPDATED_NAME).code(UPDATED_CODE);
    }

    @BeforeEach
    public void initTest() {
        messageTypeIndicator = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessageTypeIndicator != null) {
            messageTypeIndicatorRepository.delete(insertedMessageTypeIndicator);
            insertedMessageTypeIndicator = null;
        }
    }

    @Test
    @Transactional
    void createMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);
        var returnedMessageTypeIndicatorDto = om.readValue(
            restMessageTypeIndicatorMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageTypeIndicatorDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MessageTypeIndicatorDto.class
        );

        // Validate the MessageTypeIndicator in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessageTypeIndicator = messageTypeIndicatorMapper.toEntity(returnedMessageTypeIndicatorDto);
        assertMessageTypeIndicatorUpdatableFieldsEquals(
            returnedMessageTypeIndicator,
            getPersistedMessageTypeIndicator(returnedMessageTypeIndicator)
        );

        insertedMessageTypeIndicator = returnedMessageTypeIndicator;
    }

    @Test
    @Transactional
    void createMessageTypeIndicatorWithExistingId() throws Exception {
        // Create the MessageTypeIndicator with an existing ID
        messageTypeIndicator.setId(1L);
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageTypeIndicatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageTypeIndicatorDto)))
            .andExpect(status().isBadRequest());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageTypeIndicator.setName(null);

        // Create the MessageTypeIndicator, which fails.
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        restMessageTypeIndicatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageTypeIndicatorDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageTypeIndicator.setCode(null);

        // Create the MessageTypeIndicator, which fails.
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        restMessageTypeIndicatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageTypeIndicatorDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicators() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageTypeIndicator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMessageTypeIndicator() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get the messageTypeIndicator
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL_ID, messageTypeIndicator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageTypeIndicator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getMessageTypeIndicatorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        Long id = messageTypeIndicator.getId();

        defaultMessageTypeIndicatorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMessageTypeIndicatorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMessageTypeIndicatorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where name equals to
        defaultMessageTypeIndicatorFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where name in
        defaultMessageTypeIndicatorFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where name is not null
        defaultMessageTypeIndicatorFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where name contains
        defaultMessageTypeIndicatorFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where name does not contain
        defaultMessageTypeIndicatorFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where code equals to
        defaultMessageTypeIndicatorFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where code in
        defaultMessageTypeIndicatorFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where code is not null
        defaultMessageTypeIndicatorFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where code contains
        defaultMessageTypeIndicatorFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMessageTypeIndicatorsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        // Get all the messageTypeIndicatorList where code does not contain
        defaultMessageTypeIndicatorFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    private void defaultMessageTypeIndicatorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMessageTypeIndicatorShouldBeFound(shouldBeFound);
        defaultMessageTypeIndicatorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageTypeIndicatorShouldBeFound(String filter) throws Exception {
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageTypeIndicator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageTypeIndicatorShouldNotBeFound(String filter) throws Exception {
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageTypeIndicatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessageTypeIndicator() throws Exception {
        // Get the messageTypeIndicator
        restMessageTypeIndicatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessageTypeIndicator() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageTypeIndicator
        MessageTypeIndicator updatedMessageTypeIndicator = messageTypeIndicatorRepository
            .findById(messageTypeIndicator.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMessageTypeIndicator are not directly saved in db
        em.detach(updatedMessageTypeIndicator);
        updatedMessageTypeIndicator.name(UPDATED_NAME).code(UPDATED_CODE);
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(updatedMessageTypeIndicator);

        restMessageTypeIndicatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageTypeIndicatorDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isOk());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageTypeIndicatorToMatchAllProperties(updatedMessageTypeIndicator);
    }

    @Test
    @Transactional
    void putNonExistingMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageTypeIndicatorDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageTypeIndicatorDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageTypeIndicatorWithPatch() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageTypeIndicator using partial update
        MessageTypeIndicator partialUpdatedMessageTypeIndicator = new MessageTypeIndicator();
        partialUpdatedMessageTypeIndicator.setId(messageTypeIndicator.getId());

        partialUpdatedMessageTypeIndicator.name(UPDATED_NAME).code(UPDATED_CODE);

        restMessageTypeIndicatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageTypeIndicator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageTypeIndicator))
            )
            .andExpect(status().isOk());

        // Validate the MessageTypeIndicator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageTypeIndicatorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMessageTypeIndicator, messageTypeIndicator),
            getPersistedMessageTypeIndicator(messageTypeIndicator)
        );
    }

    @Test
    @Transactional
    void fullUpdateMessageTypeIndicatorWithPatch() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageTypeIndicator using partial update
        MessageTypeIndicator partialUpdatedMessageTypeIndicator = new MessageTypeIndicator();
        partialUpdatedMessageTypeIndicator.setId(messageTypeIndicator.getId());

        partialUpdatedMessageTypeIndicator.name(UPDATED_NAME).code(UPDATED_CODE);

        restMessageTypeIndicatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageTypeIndicator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageTypeIndicator))
            )
            .andExpect(status().isOk());

        // Validate the MessageTypeIndicator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageTypeIndicatorUpdatableFieldsEquals(
            partialUpdatedMessageTypeIndicator,
            getPersistedMessageTypeIndicator(partialUpdatedMessageTypeIndicator)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageTypeIndicatorDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessageTypeIndicator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageTypeIndicator.setId(longCount.incrementAndGet());

        // Create the MessageTypeIndicator
        MessageTypeIndicatorDto messageTypeIndicatorDto = messageTypeIndicatorMapper.toDto(messageTypeIndicator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTypeIndicatorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messageTypeIndicatorDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageTypeIndicator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessageTypeIndicator() throws Exception {
        // Initialize the database
        insertedMessageTypeIndicator = messageTypeIndicatorRepository.saveAndFlush(messageTypeIndicator);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the messageTypeIndicator
        restMessageTypeIndicatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, messageTypeIndicator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messageTypeIndicatorRepository.count();
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

    protected MessageTypeIndicator getPersistedMessageTypeIndicator(MessageTypeIndicator messageTypeIndicator) {
        return messageTypeIndicatorRepository.findById(messageTypeIndicator.getId()).orElseThrow();
    }

    protected void assertPersistedMessageTypeIndicatorToMatchAllProperties(MessageTypeIndicator expectedMessageTypeIndicator) {
        assertMessageTypeIndicatorAllPropertiesEquals(
            expectedMessageTypeIndicator,
            getPersistedMessageTypeIndicator(expectedMessageTypeIndicator)
        );
    }

    protected void assertPersistedMessageTypeIndicatorToMatchUpdatableProperties(MessageTypeIndicator expectedMessageTypeIndicator) {
        assertMessageTypeIndicatorAllUpdatablePropertiesEquals(
            expectedMessageTypeIndicator,
            getPersistedMessageTypeIndicator(expectedMessageTypeIndicator)
        );
    }
}
