package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.MessageFieldTypeAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.domain.MessageFieldType;
import com.transaction.project.simulator.app.repository.MessageFieldTypeRepository;
import com.transaction.project.simulator.app.service.dto.MessageFieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.MessageFieldTypeMapper;
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
 * Integration tests for the {@link MessageFieldTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageFieldTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/message-field-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageFieldTypeRepository messageFieldTypeRepository;

    @Autowired
    private MessageFieldTypeMapper messageFieldTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageFieldTypeMockMvc;

    private MessageFieldType messageFieldType;

    private MessageFieldType insertedMessageFieldType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageFieldType createEntity() {
        return new MessageFieldType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageFieldType createUpdatedEntity() {
        return new MessageFieldType().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        messageFieldType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessageFieldType != null) {
            messageFieldTypeRepository.delete(insertedMessageFieldType);
            insertedMessageFieldType = null;
        }
    }

    @Test
    @Transactional
    void createMessageFieldType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);
        var returnedMessageFieldTypeDto = om.readValue(
            restMessageFieldTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldTypeDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MessageFieldTypeDto.class
        );

        // Validate the MessageFieldType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessageFieldType = messageFieldTypeMapper.toEntity(returnedMessageFieldTypeDto);
        assertMessageFieldTypeUpdatableFieldsEquals(returnedMessageFieldType, getPersistedMessageFieldType(returnedMessageFieldType));

        insertedMessageFieldType = returnedMessageFieldType;
    }

    @Test
    @Transactional
    void createMessageFieldTypeWithExistingId() throws Exception {
        // Create the MessageFieldType with an existing ID
        messageFieldType.setId(1L);
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldTypeDto)))
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        messageFieldType.setName(null);

        // Create the MessageFieldType, which fails.
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        restMessageFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldTypeDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypes() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageFieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMessageFieldType() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get the messageFieldType
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, messageFieldType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageFieldType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getMessageFieldTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        Long id = messageFieldType.getId();

        defaultMessageFieldTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMessageFieldTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMessageFieldTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList where name equals to
        defaultMessageFieldTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList where name in
        defaultMessageFieldTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList where name is not null
        defaultMessageFieldTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList where name contains
        defaultMessageFieldTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        // Get all the messageFieldTypeList where name does not contain
        defaultMessageFieldTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMessageFieldTypesByFieldTypeIsEqualToSomething() throws Exception {
        FieldType fieldType;
        if (TestUtil.findAll(em, FieldType.class).isEmpty()) {
            messageFieldTypeRepository.saveAndFlush(messageFieldType);
            fieldType = FieldTypeResourceIT.createEntity();
        } else {
            fieldType = TestUtil.findAll(em, FieldType.class).get(0);
        }
        em.persist(fieldType);
        em.flush();
        messageFieldType.setFieldType(fieldType);
        messageFieldTypeRepository.saveAndFlush(messageFieldType);
        Long fieldTypeId = fieldType.getId();
        // Get all the messageFieldTypeList where fieldType equals to fieldTypeId
        defaultMessageFieldTypeShouldBeFound("fieldTypeId.equals=" + fieldTypeId);

        // Get all the messageFieldTypeList where fieldType equals to (fieldTypeId + 1)
        defaultMessageFieldTypeShouldNotBeFound("fieldTypeId.equals=" + (fieldTypeId + 1));
    }

    private void defaultMessageFieldTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMessageFieldTypeShouldBeFound(shouldBeFound);
        defaultMessageFieldTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageFieldTypeShouldBeFound(String filter) throws Exception {
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageFieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageFieldTypeShouldNotBeFound(String filter) throws Exception {
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessageFieldType() throws Exception {
        // Get the messageFieldType
        restMessageFieldTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessageFieldType() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldType
        MessageFieldType updatedMessageFieldType = messageFieldTypeRepository.findById(messageFieldType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessageFieldType are not directly saved in db
        em.detach(updatedMessageFieldType);
        updatedMessageFieldType.name(UPDATED_NAME);
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(updatedMessageFieldType);

        restMessageFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageFieldTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldTypeDto))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageFieldTypeToMatchAllProperties(updatedMessageFieldType);
    }

    @Test
    @Transactional
    void putNonExistingMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageFieldTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageFieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageFieldTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageFieldTypeWithPatch() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldType using partial update
        MessageFieldType partialUpdatedMessageFieldType = new MessageFieldType();
        partialUpdatedMessageFieldType.setId(messageFieldType.getId());

        restMessageFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageFieldType))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageFieldTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMessageFieldType, messageFieldType),
            getPersistedMessageFieldType(messageFieldType)
        );
    }

    @Test
    @Transactional
    void fullUpdateMessageFieldTypeWithPatch() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messageFieldType using partial update
        MessageFieldType partialUpdatedMessageFieldType = new MessageFieldType();
        partialUpdatedMessageFieldType.setId(messageFieldType.getId());

        partialUpdatedMessageFieldType.name(UPDATED_NAME);

        restMessageFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessageFieldType))
            )
            .andExpect(status().isOk());

        // Validate the MessageFieldType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageFieldTypeUpdatableFieldsEquals(
            partialUpdatedMessageFieldType,
            getPersistedMessageFieldType(partialUpdatedMessageFieldType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageFieldTypeDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageFieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageFieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessageFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messageFieldType.setId(longCount.incrementAndGet());

        // Create the MessageFieldType
        MessageFieldTypeDto messageFieldTypeDto = messageFieldTypeMapper.toDto(messageFieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageFieldTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messageFieldTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageFieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessageFieldType() throws Exception {
        // Initialize the database
        insertedMessageFieldType = messageFieldTypeRepository.saveAndFlush(messageFieldType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the messageFieldType
        restMessageFieldTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, messageFieldType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messageFieldTypeRepository.count();
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

    protected MessageFieldType getPersistedMessageFieldType(MessageFieldType messageFieldType) {
        return messageFieldTypeRepository.findById(messageFieldType.getId()).orElseThrow();
    }

    protected void assertPersistedMessageFieldTypeToMatchAllProperties(MessageFieldType expectedMessageFieldType) {
        assertMessageFieldTypeAllPropertiesEquals(expectedMessageFieldType, getPersistedMessageFieldType(expectedMessageFieldType));
    }

    protected void assertPersistedMessageFieldTypeToMatchUpdatableProperties(MessageFieldType expectedMessageFieldType) {
        assertMessageFieldTypeAllUpdatablePropertiesEquals(
            expectedMessageFieldType,
            getPersistedMessageFieldType(expectedMessageFieldType)
        );
    }
}
