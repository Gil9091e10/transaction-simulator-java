package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.MessageFieldsConfigAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.domain.MessageFieldsConfig;
import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.repository.MessageFieldsConfigRepository;
import com.transaction.project.simulator.app.service.dto.MessageFieldsConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldsConfigMapper;
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
 * Integration tests for the {@link MessageFieldsConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageFieldsConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_LENGTH = 1;
    private static final Integer UPDATED_FIELD_LENGTH = 2;
    private static final Integer SMALLER_FIELD_LENGTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/message-fields-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageFieldsConfigRepository messageFieldsConfigRepository;

    @Autowired
    private MessageFieldsConfigMapper messageFieldsConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageFieldsConfigMockMvc;

    private MessageFieldsConfig messageFieldsConfig;

    private MessageFieldsConfig insertedMessageFieldsConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageFieldsConfig createEntity() {
        return new MessageFieldsConfig().name(DEFAULT_NAME).fieldLength(DEFAULT_FIELD_LENGTH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageFieldsConfig createUpdatedEntity() {
        return new MessageFieldsConfig().name(UPDATED_NAME).fieldLength(UPDATED_FIELD_LENGTH);
    }

    @BeforeEach
    public void initTest() {
        messageFieldsConfig = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessageFieldsConfig != null) {
            messageFieldsConfigRepository.delete(insertedMessageFieldsConfig);
            insertedMessageFieldsConfig = null;
        }
    }

    @Test
    @Transactional
    void createMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);
        var returnedMessageFieldsConfigDto = om.readValue(
            restMessageFieldsConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldsConfigDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MessageFieldsConfigDto.class
        );

        // Validate the MessageFieldsConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessageFieldsConfig = messageFieldsConfigMapper.toEntity(returnedMessageFieldsConfigDto);
        assertMessageFieldsConfigUpdatableFieldsEquals(
            returnedMessageFieldsConfig,
            getPersistedMessageFieldsConfig(returnedMessageFieldsConfig)
        );

        insertedMessageFieldsConfig = returnedMessageFieldsConfig;
    }

    @Test
    @Transactional
    void createMessageFieldsConfigWithExistingId() throws Exception {
        // Create the MessageFieldsConfig with an existing ID
        messageFieldsConfig.setId(1L);
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageFieldsConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldsConfigDto)))
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageFieldsConfig.setName(null);

        // Create the MessageFieldsConfig, which fails.
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        restMessageFieldsConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldsConfigDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFieldLengthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageFieldsConfig.setFieldLength(null);

        // Create the MessageFieldsConfig, which fails.
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        restMessageFieldsConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldsConfigDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigs() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageFieldsConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fieldLength").value(hasItem(DEFAULT_FIELD_LENGTH)));
    }

    @Test
    @Transactional
    void getMessageFieldsConfig() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get the messageFieldsConfig
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, messageFieldsConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageFieldsConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fieldLength").value(DEFAULT_FIELD_LENGTH));
    }

    @Test
    @Transactional
    void getMessageFieldsConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        Long id = messageFieldsConfig.getId();

        defaultMessageFieldsConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMessageFieldsConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMessageFieldsConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where name equals to
        defaultMessageFieldsConfigFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where name in
        defaultMessageFieldsConfigFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where name is not null
        defaultMessageFieldsConfigFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where name contains
        defaultMessageFieldsConfigFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where name does not contain
        defaultMessageFieldsConfigFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength equals to
        defaultMessageFieldsConfigFiltering("fieldLength.equals=" + DEFAULT_FIELD_LENGTH, "fieldLength.equals=" + UPDATED_FIELD_LENGTH);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength in
        defaultMessageFieldsConfigFiltering(
            "fieldLength.in=" + DEFAULT_FIELD_LENGTH + "," + UPDATED_FIELD_LENGTH,
            "fieldLength.in=" + UPDATED_FIELD_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength is not null
        defaultMessageFieldsConfigFiltering("fieldLength.specified=true", "fieldLength.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength is greater than or equal to
        defaultMessageFieldsConfigFiltering(
            "fieldLength.greaterThanOrEqual=" + DEFAULT_FIELD_LENGTH,
            "fieldLength.greaterThanOrEqual=" + UPDATED_FIELD_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength is less than or equal to
        defaultMessageFieldsConfigFiltering(
            "fieldLength.lessThanOrEqual=" + DEFAULT_FIELD_LENGTH,
            "fieldLength.lessThanOrEqual=" + SMALLER_FIELD_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength is less than
        defaultMessageFieldsConfigFiltering("fieldLength.lessThan=" + UPDATED_FIELD_LENGTH, "fieldLength.lessThan=" + DEFAULT_FIELD_LENGTH);
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByFieldLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        // Get all the messageFieldsConfigList where fieldLength is greater than
        defaultMessageFieldsConfigFiltering(
            "fieldLength.greaterThan=" + SMALLER_FIELD_LENGTH,
            "fieldLength.greaterThan=" + DEFAULT_FIELD_LENGTH
        );
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByMessageIsoConfigIsEqualToSomething() throws Exception {
        MessageIsoConfig messageIsoConfig;
        if (TestUtil.findAll(em, MessageIsoConfig.class).isEmpty()) {
            messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);
            messageIsoConfig = MessageIsoConfigResourceIT.createEntity();
        } else {
            messageIsoConfig = TestUtil.findAll(em, MessageIsoConfig.class).get(0);
        }
        em.persist(messageIsoConfig);
        em.flush();
        messageFieldsConfig.setMessageIsoConfig(messageIsoConfig);
        messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);
        Long messageIsoConfigId = messageIsoConfig.getId();
        // Get all the messageFieldsConfigList where messageIsoConfig equals to messageIsoConfigId
        defaultMessageFieldsConfigShouldBeFound("messageIsoConfigId.equals=" + messageIsoConfigId);

        // Get all the messageFieldsConfigList where messageIsoConfig equals to (messageIsoConfigId + 1)
        defaultMessageFieldsConfigShouldNotBeFound("messageIsoConfigId.equals=" + (messageIsoConfigId + 1));
    }

    @Test
    @Transactional
    void getAllMessageFieldsConfigsByMessageFieldTypeIsEqualToSomething() throws Exception {
        MessageFieldType messageFieldType;
        if (TestUtil.findAll(em, MessageFieldType.class).isEmpty()) {
            messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);
            messageFieldType = MessageFieldTypeResourceIT.createEntity();
        } else {
            messageFieldType = TestUtil.findAll(em, MessageFieldType.class).get(0);
        }
        em.persist(messageFieldType);
        em.flush();
        messageFieldsConfig.setMessageFieldType(messageFieldType);
        messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);
        Long messageFieldTypeId = messageFieldType.getId();
        // Get all the messageFieldsConfigList where messageFieldType equals to messageFieldTypeId
        defaultMessageFieldsConfigShouldBeFound("messageFieldTypeId.equals=" + messageFieldTypeId);

        // Get all the messageFieldsConfigList where messageFieldType equals to (messageFieldTypeId + 1)
        defaultMessageFieldsConfigShouldNotBeFound("messageFieldTypeId.equals=" + (messageFieldTypeId + 1));
    }

    private void defaultMessageFieldsConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMessageFieldsConfigShouldBeFound(shouldBeFound);
        defaultMessageFieldsConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageFieldsConfigShouldBeFound(String filter) throws Exception {
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageFieldsConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fieldLength").value(hasItem(DEFAULT_FIELD_LENGTH)));

        // Check, that the count call also returns 1
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageFieldsConfigShouldNotBeFound(String filter) throws Exception {
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageFieldsConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessageFieldsConfig() throws Exception {
        // Get the messageFieldsConfig
        restMessageFieldsConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessageFieldsConfig() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldsConfig
        MessageFieldsConfig updatedMessageFieldsConfig = messageFieldsConfigRepository.findById(messageFieldsConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessageFieldsConfig are not directly saved in db
        em.detach(updatedMessageFieldsConfig);
        updatedMessageFieldsConfig.name(UPDATED_NAME).fieldLength(UPDATED_FIELD_LENGTH);
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(updatedMessageFieldsConfig);

        restMessageFieldsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageFieldsConfigDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageFieldsConfigToMatchAllProperties(updatedMessageFieldsConfig);
    }

    @Test
    @Transactional
    void putNonExistingMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageFieldsConfigDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldsConfigDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageFieldsConfigWithPatch() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldsConfig using partial update
        MessageFieldsConfig partialUpdatedMessageFieldsConfig = new MessageFieldsConfig();
        partialUpdatedMessageFieldsConfig.setId(messageFieldsConfig.getId());

        partialUpdatedMessageFieldsConfig.name(UPDATED_NAME).fieldLength(UPDATED_FIELD_LENGTH);

        restMessageFieldsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageFieldsConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageFieldsConfig))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldsConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageFieldsConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMessageFieldsConfig, messageFieldsConfig),
            getPersistedMessageFieldsConfig(messageFieldsConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateMessageFieldsConfigWithPatch() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldsConfig using partial update
        MessageFieldsConfig partialUpdatedMessageFieldsConfig = new MessageFieldsConfig();
        partialUpdatedMessageFieldsConfig.setId(messageFieldsConfig.getId());

        partialUpdatedMessageFieldsConfig.name(UPDATED_NAME).fieldLength(UPDATED_FIELD_LENGTH);

        restMessageFieldsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageFieldsConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageFieldsConfig))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldsConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageFieldsConfigUpdatableFieldsEquals(
            partialUpdatedMessageFieldsConfig,
            getPersistedMessageFieldsConfig(partialUpdatedMessageFieldsConfig)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageFieldsConfigDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessageFieldsConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldsConfig.setId(longCount.incrementAndGet());

        // Create the MessageFieldsConfig
        MessageFieldsConfigDto messageFieldsConfigDto = messageFieldsConfigMapper.toDto(messageFieldsConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldsConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messageFieldsConfigDto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageFieldsConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessageFieldsConfig() throws Exception {
        // Initialize the database
        insertedMessageFieldsConfig = messageFieldsConfigRepository.saveAndFlush(messageFieldsConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the messageFieldsConfig
        restMessageFieldsConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, messageFieldsConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messageFieldsConfigRepository.count();
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

    protected MessageFieldsConfig getPersistedMessageFieldsConfig(MessageFieldsConfig messageFieldsConfig) {
        return messageFieldsConfigRepository.findById(messageFieldsConfig.getId()).orElseThrow();
    }

    protected void assertPersistedMessageFieldsConfigToMatchAllProperties(MessageFieldsConfig expectedMessageFieldsConfig) {
        assertMessageFieldsConfigAllPropertiesEquals(
            expectedMessageFieldsConfig,
            getPersistedMessageFieldsConfig(expectedMessageFieldsConfig)
        );
    }

    protected void assertPersistedMessageFieldsConfigToMatchUpdatableProperties(MessageFieldsConfig expectedMessageFieldsConfig) {
        assertMessageFieldsConfigAllUpdatablePropertiesEquals(
            expectedMessageFieldsConfig,
            getPersistedMessageFieldsConfig(expectedMessageFieldsConfig)
        );
    }
}
