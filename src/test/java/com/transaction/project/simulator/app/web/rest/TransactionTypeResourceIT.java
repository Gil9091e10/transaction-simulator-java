package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.TransactionTypeAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.TransactionType;
import com.transaction.project.simulator.app.repository.TransactionTypeRepository;
import com.transaction.project.simulator.app.service.dto.TransactionTypeDto;
import com.transaction.project.simulator.app.service.mapper.TransactionTypeMapper;
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
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionTypeMapper transactionTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    private TransactionType insertedTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity() {
        return new TransactionType().code(DEFAULT_CODE).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity() {
        return new TransactionType().code(UPDATED_CODE).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        transactionType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransactionType != null) {
            transactionTypeRepository.delete(insertedTransactionType);
            insertedTransactionType = null;
        }
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);
        var returnedTransactionTypeDto = om.readValue(
            restTransactionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionTypeDto.class
        );

        // Validate the TransactionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransactionType = transactionTypeMapper.toEntity(returnedTransactionTypeDto);
        assertTransactionTypeUpdatableFieldsEquals(returnedTransactionType, getPersistedTransactionType(returnedTransactionType));

        insertedTransactionType = returnedTransactionType;
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDto)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionType.setCode(null);

        // Create the TransactionType, which fails.
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionType.setName(null);

        // Create the TransactionType, which fails.
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTransactionTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        Long id = transactionType.getId();

        defaultTransactionTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransactionTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransactionTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code equals to
        defaultTransactionTypeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code in
        defaultTransactionTypeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code is not null
        defaultTransactionTypeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code contains
        defaultTransactionTypeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where code does not contain
        defaultTransactionTypeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name equals to
        defaultTransactionTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name in
        defaultTransactionTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name is not null
        defaultTransactionTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name contains
        defaultTransactionTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList where name does not contain
        defaultTransactionTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultTransactionTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransactionTypeShouldBeFound(shouldBeFound);
        defaultTransactionTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionTypeShouldBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionTypeShouldNotBeFound(String filter) throws Exception {
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.code(UPDATED_CODE).name(UPDATED_NAME);
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(updatedTransactionType);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDto))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionTypeToMatchAllProperties(updatedTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionTypeDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.code(UPDATED_CODE).name(UPDATED_NAME);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionType, transactionType),
            getPersistedTransactionType(transactionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.code(UPDATED_CODE).name(UPDATED_NAME);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            partialUpdatedTransactionType,
            getPersistedTransactionType(partialUpdatedTransactionType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionTypeDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionTypeDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // Create the TransactionType
        TransactionTypeDto transactionTypeDto = transactionTypeMapper.toDto(transactionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionTypeDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionTypeRepository.count();
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

    protected TransactionType getPersistedTransactionType(TransactionType transactionType) {
        return transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionTypeToMatchAllProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllPropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }

    protected void assertPersistedTransactionTypeToMatchUpdatableProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllUpdatablePropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }
}
