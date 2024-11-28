package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.FieldTypeAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.FieldType;
import com.transaction.project.simulator.app.repository.FieldTypeRepository;
import com.transaction.project.simulator.app.service.dto.FieldTypeDto;
import com.transaction.project.simulator.app.service.mapper.FieldTypeMapper;
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
 * Integration tests for the {@link FieldTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    @Autowired
    private FieldTypeMapper fieldTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldTypeMockMvc;

    private FieldType fieldType;

    private FieldType insertedFieldType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldType createEntity() {
        return new FieldType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldType createUpdatedEntity() {
        return new FieldType().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        fieldType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFieldType != null) {
            fieldTypeRepository.delete(insertedFieldType);
            insertedFieldType = null;
        }
    }

    @Test
    @Transactional
    void createFieldType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);
        var returnedFieldTypeDto = om.readValue(
            restFieldTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fieldTypeDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FieldTypeDto.class
        );

        // Validate the FieldType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFieldType = fieldTypeMapper.toEntity(returnedFieldTypeDto);
        assertFieldTypeUpdatableFieldsEquals(returnedFieldType, getPersistedFieldType(returnedFieldType));

        insertedFieldType = returnedFieldType;
    }

    @Test
    @Transactional
    void createFieldTypeWithExistingId() throws Exception {
        // Create the FieldType with an existing ID
        fieldType.setId(1L);
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fieldTypeDto)))
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        fieldType.setName(null);

        // Create the FieldType, which fails.
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        restFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fieldTypeDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFieldTypes() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFieldType() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get the fieldType
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fieldType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fieldType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getFieldTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        Long id = fieldType.getId();

        defaultFieldTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFieldTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFieldTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFieldTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList where name equals to
        defaultFieldTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList where name in
        defaultFieldTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList where name is not null
        defaultFieldTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFieldTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList where name contains
        defaultFieldTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList where name does not contain
        defaultFieldTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultFieldTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFieldTypeShouldBeFound(shouldBeFound);
        defaultFieldTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFieldTypeShouldBeFound(String filter) throws Exception {
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFieldTypeShouldNotBeFound(String filter) throws Exception {
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFieldType() throws Exception {
        // Get the fieldType
        restFieldTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFieldType() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldType
        FieldType updatedFieldType = fieldTypeRepository.findById(fieldType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFieldType are not directly saved in db
        em.detach(updatedFieldType);
        updatedFieldType.name(UPDATED_NAME);
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(updatedFieldType);

        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fieldTypeDto))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFieldTypeToMatchAllProperties(updatedFieldType);
    }

    @Test
    @Transactional
    void putNonExistingFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fieldTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldType using partial update
        FieldType partialUpdatedFieldType = new FieldType();
        partialUpdatedFieldType.setId(fieldType.getId());

        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFieldType))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFieldType, fieldType),
            getPersistedFieldType(fieldType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fieldType using partial update
        FieldType partialUpdatedFieldType = new FieldType();
        partialUpdatedFieldType.setId(fieldType.getId());

        partialUpdatedFieldType.name(UPDATED_NAME);

        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFieldType))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFieldTypeUpdatableFieldsEquals(partialUpdatedFieldType, getPersistedFieldType(partialUpdatedFieldType));
    }

    @Test
    @Transactional
    void patchNonExistingFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fieldTypeDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fieldTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFieldType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fieldType.setId(longCount.incrementAndGet());

        // Create the FieldType
        FieldTypeDto fieldTypeDto = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fieldTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFieldType() throws Exception {
        // Initialize the database
        insertedFieldType = fieldTypeRepository.saveAndFlush(fieldType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fieldType
        restFieldTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fieldType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fieldTypeRepository.count();
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

    protected FieldType getPersistedFieldType(FieldType fieldType) {
        return fieldTypeRepository.findById(fieldType.getId()).orElseThrow();
    }

    protected void assertPersistedFieldTypeToMatchAllProperties(FieldType expectedFieldType) {
        assertFieldTypeAllPropertiesEquals(expectedFieldType, getPersistedFieldType(expectedFieldType));
    }

    protected void assertPersistedFieldTypeToMatchUpdatableProperties(FieldType expectedFieldType) {
        assertFieldTypeAllUpdatablePropertiesEquals(expectedFieldType, getPersistedFieldType(expectedFieldType));
    }
}
