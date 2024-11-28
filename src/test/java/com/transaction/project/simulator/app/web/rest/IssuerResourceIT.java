package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.IssuerAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Issuer;
import com.transaction.project.simulator.app.repository.IssuerRepository;
import com.transaction.project.simulator.app.service.dto.IssuerDto;
import com.transaction.project.simulator.app.service.mapper.IssuerMapper;
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
 * Integration tests for the {@link IssuerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IssuerResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/issuers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IssuerRepository issuerRepository;

    @Autowired
    private IssuerMapper issuerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIssuerMockMvc;

    private Issuer issuer;

    private Issuer insertedIssuer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issuer createEntity() {
        return new Issuer().code(DEFAULT_CODE).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issuer createUpdatedEntity() {
        return new Issuer().code(UPDATED_CODE).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        issuer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedIssuer != null) {
            issuerRepository.delete(insertedIssuer);
            insertedIssuer = null;
        }
    }

    @Test
    @Transactional
    void createIssuer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);
        var returnedIssuerDto = om.readValue(
            restIssuerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IssuerDto.class
        );

        // Validate the Issuer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIssuer = issuerMapper.toEntity(returnedIssuerDto);
        assertIssuerUpdatableFieldsEquals(returnedIssuer, getPersistedIssuer(returnedIssuer));

        insertedIssuer = returnedIssuer;
    }

    @Test
    @Transactional
    void createIssuerWithExistingId() throws Exception {
        // Create the Issuer with an existing ID
        issuer.setId(1L);
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssuerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto)))
            .andExpect(status().isBadRequest());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        issuer.setCode(null);

        // Create the Issuer, which fails.
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        restIssuerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        issuer.setName(null);

        // Create the Issuer, which fails.
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        restIssuerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIssuers() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issuer.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIssuer() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get the issuer
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL_ID, issuer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(issuer.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getIssuersByIdFiltering() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        Long id = issuer.getId();

        defaultIssuerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIssuerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIssuerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIssuersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where code equals to
        defaultIssuerFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where code in
        defaultIssuerFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where code is not null
        defaultIssuerFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllIssuersByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where code contains
        defaultIssuerFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where code does not contain
        defaultIssuerFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where name equals to
        defaultIssuerFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIssuersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where name in
        defaultIssuerFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIssuersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where name is not null
        defaultIssuerFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIssuersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where name contains
        defaultIssuerFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIssuersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        // Get all the issuerList where name does not contain
        defaultIssuerFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultIssuerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIssuerShouldBeFound(shouldBeFound);
        defaultIssuerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIssuerShouldBeFound(String filter) throws Exception {
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issuer.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIssuerShouldNotBeFound(String filter) throws Exception {
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIssuerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIssuer() throws Exception {
        // Get the issuer
        restIssuerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIssuer() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the issuer
        Issuer updatedIssuer = issuerRepository.findById(issuer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIssuer are not directly saved in db
        em.detach(updatedIssuer);
        updatedIssuer.code(UPDATED_CODE).name(UPDATED_NAME);
        IssuerDto issuerDto = issuerMapper.toDto(updatedIssuer);

        restIssuerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issuerDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto))
            )
            .andExpect(status().isOk());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIssuerToMatchAllProperties(updatedIssuer);
    }

    @Test
    @Transactional
    void putNonExistingIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issuerDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(issuerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(issuerDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIssuerWithPatch() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the issuer using partial update
        Issuer partialUpdatedIssuer = new Issuer();
        partialUpdatedIssuer.setId(issuer.getId());

        partialUpdatedIssuer.code(UPDATED_CODE);

        restIssuerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssuer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIssuer))
            )
            .andExpect(status().isOk());

        // Validate the Issuer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIssuerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedIssuer, issuer), getPersistedIssuer(issuer));
    }

    @Test
    @Transactional
    void fullUpdateIssuerWithPatch() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the issuer using partial update
        Issuer partialUpdatedIssuer = new Issuer();
        partialUpdatedIssuer.setId(issuer.getId());

        partialUpdatedIssuer.code(UPDATED_CODE).name(UPDATED_NAME);

        restIssuerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssuer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIssuer))
            )
            .andExpect(status().isOk());

        // Validate the Issuer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIssuerUpdatableFieldsEquals(partialUpdatedIssuer, getPersistedIssuer(partialUpdatedIssuer));
    }

    @Test
    @Transactional
    void patchNonExistingIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, issuerDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(issuerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(issuerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIssuer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        issuer.setId(longCount.incrementAndGet());

        // Create the Issuer
        IssuerDto issuerDto = issuerMapper.toDto(issuer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(issuerDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Issuer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIssuer() throws Exception {
        // Initialize the database
        insertedIssuer = issuerRepository.saveAndFlush(issuer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the issuer
        restIssuerMockMvc
            .perform(delete(ENTITY_API_URL_ID, issuer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return issuerRepository.count();
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

    protected Issuer getPersistedIssuer(Issuer issuer) {
        return issuerRepository.findById(issuer.getId()).orElseThrow();
    }

    protected void assertPersistedIssuerToMatchAllProperties(Issuer expectedIssuer) {
        assertIssuerAllPropertiesEquals(expectedIssuer, getPersistedIssuer(expectedIssuer));
    }

    protected void assertPersistedIssuerToMatchUpdatableProperties(Issuer expectedIssuer) {
        assertIssuerAllUpdatablePropertiesEquals(expectedIssuer, getPersistedIssuer(expectedIssuer));
    }
}
