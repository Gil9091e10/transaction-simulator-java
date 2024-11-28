package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.MessageIsoConfigAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.domain.MessageIsoConfig;
import com.transaction.project.simulator.app.domain.MessageTypeIndicator;
import com.transaction.project.simulator.app.repository.MessageIsoConfigRepository;
import com.transaction.project.simulator.app.service.dto.MessageIsoConfigDto;
import com.transaction.project.simulator.app.service.mapper.MessageIsoConfigMapper;
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
 * Integration tests for the {@link MessageIsoConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageIsoConfigResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/message-iso-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageIsoConfigRepository messageIsoConfigRepository;

    @Autowired
    private MessageIsoConfigMapper messageIsoConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageIsoConfigMockMvc;

    private MessageIsoConfig messageIsoConfig;

    private MessageIsoConfig insertedMessageIsoConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageIsoConfig createEntity() {
        return new MessageIsoConfig().name(DEFAULT_NAME).filename(DEFAULT_FILENAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageIsoConfig createUpdatedEntity() {
        return new MessageIsoConfig().name(UPDATED_NAME).filename(UPDATED_FILENAME);
    }

    @BeforeEach
    public void initTest() {
        messageIsoConfig = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessageIsoConfig != null) {
            messageIsoConfigRepository.delete(insertedMessageIsoConfig);
            insertedMessageIsoConfig = null;
        }
    }

    @Test
    @Transactional
    void createMessageIsoConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);
        var returnedMessageIsoConfigDto = om.readValue(
            restMessageIsoConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageIsoConfigDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MessageIsoConfigDto.class
        );

        // Validate the MessageIsoConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessageIsoConfig = messageIsoConfigMapper.toEntity(returnedMessageIsoConfigDto);
        assertMessageIsoConfigUpdatableFieldsEquals(returnedMessageIsoConfig, getPersistedMessageIsoConfig(returnedMessageIsoConfig));

        insertedMessageIsoConfig = returnedMessageIsoConfig;
    }

    @Test
    @Transactional
    void createMessageIsoConfigWithExistingId() throws Exception {
        // Create the MessageIsoConfig with an existing ID
        messageIsoConfig.setId(1L);
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageIsoConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageIsoConfigDto)))
            .andExpect(status().isBadRequest());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageIsoConfig.setName(null);

        // Create the MessageIsoConfig, which fails.
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        restMessageIsoConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageIsoConfigDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilenameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageIsoConfig.setFilename(null);

        // Create the MessageIsoConfig, which fails.
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        restMessageIsoConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageIsoConfigDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigs() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageIsoConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));
    }

    @Test
    @Transactional
    void getMessageIsoConfig() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get the messageIsoConfig
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, messageIsoConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageIsoConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME));
    }

    @Test
    @Transactional
    void getMessageIsoConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        Long id = messageIsoConfig.getId();

        defaultMessageIsoConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMessageIsoConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMessageIsoConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where name equals to
        defaultMessageIsoConfigFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where name in
        defaultMessageIsoConfigFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where name is not null
        defaultMessageIsoConfigFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where name contains
        defaultMessageIsoConfigFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where name does not contain
        defaultMessageIsoConfigFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where filename equals to
        defaultMessageIsoConfigFiltering("filename.equals=" + DEFAULT_FILENAME, "filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where filename in
        defaultMessageIsoConfigFiltering("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME, "filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where filename is not null
        defaultMessageIsoConfigFiltering("filename.specified=true", "filename.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByFilenameContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where filename contains
        defaultMessageIsoConfigFiltering("filename.contains=" + DEFAULT_FILENAME, "filename.contains=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByFilenameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        // Get all the messageIsoConfigList where filename does not contain
        defaultMessageIsoConfigFiltering("filename.doesNotContain=" + UPDATED_FILENAME, "filename.doesNotContain=" + DEFAULT_FILENAME);
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByAcquirerIsEqualToSomething() throws Exception {
        Acquirer acquirer;
        if (TestUtil.findAll(em, Acquirer.class).isEmpty()) {
            messageIsoConfigRepository.saveAndFlush(messageIsoConfig);
            acquirer = AcquirerResourceIT.createEntity();
        } else {
            acquirer = TestUtil.findAll(em, Acquirer.class).get(0);
        }
        em.persist(acquirer);
        em.flush();
        messageIsoConfig.setAcquirer(acquirer);
        messageIsoConfigRepository.saveAndFlush(messageIsoConfig);
        Long acquirerId = acquirer.getId();
        // Get all the messageIsoConfigList where acquirer equals to acquirerId
        defaultMessageIsoConfigShouldBeFound("acquirerId.equals=" + acquirerId);

        // Get all the messageIsoConfigList where acquirer equals to (acquirerId + 1)
        defaultMessageIsoConfigShouldNotBeFound("acquirerId.equals=" + (acquirerId + 1));
    }

    @Test
    @Transactional
    void getAllMessageIsoConfigsByMessageTypeIndicatorIsEqualToSomething() throws Exception {
        MessageTypeIndicator messageTypeIndicator;
        if (TestUtil.findAll(em, MessageTypeIndicator.class).isEmpty()) {
            messageIsoConfigRepository.saveAndFlush(messageIsoConfig);
            messageTypeIndicator = MessageTypeIndicatorResourceIT.createEntity();
        } else {
            messageTypeIndicator = TestUtil.findAll(em, MessageTypeIndicator.class).get(0);
        }
        em.persist(messageTypeIndicator);
        em.flush();
        messageIsoConfig.setMessageTypeIndicator(messageTypeIndicator);
        messageIsoConfigRepository.saveAndFlush(messageIsoConfig);
        Long messageTypeIndicatorId = messageTypeIndicator.getId();
        // Get all the messageIsoConfigList where messageTypeIndicator equals to messageTypeIndicatorId
        defaultMessageIsoConfigShouldBeFound("messageTypeIndicatorId.equals=" + messageTypeIndicatorId);

        // Get all the messageIsoConfigList where messageTypeIndicator equals to (messageTypeIndicatorId + 1)
        defaultMessageIsoConfigShouldNotBeFound("messageTypeIndicatorId.equals=" + (messageTypeIndicatorId + 1));
    }

    private void defaultMessageIsoConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMessageIsoConfigShouldBeFound(shouldBeFound);
        defaultMessageIsoConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageIsoConfigShouldBeFound(String filter) throws Exception {
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageIsoConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME)));

        // Check, that the count call also returns 1
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageIsoConfigShouldNotBeFound(String filter) throws Exception {
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageIsoConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessageIsoConfig() throws Exception {
        // Get the messageIsoConfig
        restMessageIsoConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessageIsoConfig() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageIsoConfig
        MessageIsoConfig updatedMessageIsoConfig = messageIsoConfigRepository.findById(messageIsoConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessageIsoConfig are not directly saved in db
        em.detach(updatedMessageIsoConfig);
        updatedMessageIsoConfig.name(UPDATED_NAME).filename(UPDATED_FILENAME);
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(updatedMessageIsoConfig);

        restMessageIsoConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageIsoConfigDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageIsoConfigDto))
            )
            .andExpect(status().isOk());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageIsoConfigToMatchAllProperties(updatedMessageIsoConfig);
    }

    @Test
    @Transactional
    void putNonExistingMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageIsoConfigDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageIsoConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageIsoConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageIsoConfigDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageIsoConfigWithPatch() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageIsoConfig using partial update
        MessageIsoConfig partialUpdatedMessageIsoConfig = new MessageIsoConfig();
        partialUpdatedMessageIsoConfig.setId(messageIsoConfig.getId());

        partialUpdatedMessageIsoConfig.filename(UPDATED_FILENAME);

        restMessageIsoConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageIsoConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageIsoConfig))
            )
            .andExpect(status().isOk());

        // Validate the MessageIsoConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageIsoConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMessageIsoConfig, messageIsoConfig),
            getPersistedMessageIsoConfig(messageIsoConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateMessageIsoConfigWithPatch() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageIsoConfig using partial update
        MessageIsoConfig partialUpdatedMessageIsoConfig = new MessageIsoConfig();
        partialUpdatedMessageIsoConfig.setId(messageIsoConfig.getId());

        partialUpdatedMessageIsoConfig.name(UPDATED_NAME).filename(UPDATED_FILENAME);

        restMessageIsoConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageIsoConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageIsoConfig))
            )
            .andExpect(status().isOk());

        // Validate the MessageIsoConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageIsoConfigUpdatableFieldsEquals(
            partialUpdatedMessageIsoConfig,
            getPersistedMessageIsoConfig(partialUpdatedMessageIsoConfig)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageIsoConfigDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageIsoConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageIsoConfigDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessageIsoConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageIsoConfig.setId(longCount.incrementAndGet());

        // Create the MessageIsoConfig
        MessageIsoConfigDto messageIsoConfigDto = messageIsoConfigMapper.toDto(messageIsoConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageIsoConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messageIsoConfigDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageIsoConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessageIsoConfig() throws Exception {
        // Initialize the database
        insertedMessageIsoConfig = messageIsoConfigRepository.saveAndFlush(messageIsoConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the messageIsoConfig
        restMessageIsoConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, messageIsoConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messageIsoConfigRepository.count();
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

    protected MessageIsoConfig getPersistedMessageIsoConfig(MessageIsoConfig messageIsoConfig) {
        return messageIsoConfigRepository.findById(messageIsoConfig.getId()).orElseThrow();
    }

    protected void assertPersistedMessageIsoConfigToMatchAllProperties(MessageIsoConfig expectedMessageIsoConfig) {
        assertMessageIsoConfigAllPropertiesEquals(expectedMessageIsoConfig, getPersistedMessageIsoConfig(expectedMessageIsoConfig));
    }

    protected void assertPersistedMessageIsoConfigToMatchUpdatableProperties(MessageIsoConfig expectedMessageIsoConfig) {
        assertMessageIsoConfigAllUpdatablePropertiesEquals(
            expectedMessageIsoConfig,
            getPersistedMessageIsoConfig(expectedMessageIsoConfig)
        );
    }
}
