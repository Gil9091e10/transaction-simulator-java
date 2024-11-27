package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.AcquirerAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.repository.AcquirerRepository;
import com.transaction.project.simulator.app.service.dto.AcquirerDto;
import com.transaction.project.simulator.app.service.mapper.AcquirerMapper;
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
 * Integration tests for the {@link AcquirerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcquirerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOCKET_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOCKET_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acquirers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AcquirerRepository acquirerRepository;

    @Autowired
    private AcquirerMapper acquirerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcquirerMockMvc;

    private Acquirer acquirer;

    private Acquirer insertedAcquirer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acquirer createEntity() {
        return new Acquirer().name(DEFAULT_NAME).socketUrl(DEFAULT_SOCKET_URL).email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acquirer createUpdatedEntity() {
        return new Acquirer().name(UPDATED_NAME).socketUrl(UPDATED_SOCKET_URL).email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        acquirer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAcquirer != null) {
            acquirerRepository.delete(insertedAcquirer);
            insertedAcquirer = null;
        }
    }

    @Test
    @Transactional
    void createAcquirer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);
        var returnedAcquirerDto = om.readValue(
            restAcquirerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AcquirerDto.class
        );

        // Validate the Acquirer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAcquirer = acquirerMapper.toEntity(returnedAcquirerDto);
        assertAcquirerUpdatableFieldsEquals(returnedAcquirer, getPersistedAcquirer(returnedAcquirer));

        insertedAcquirer = returnedAcquirer;
    }

    @Test
    @Transactional
    void createAcquirerWithExistingId() throws Exception {
        // Create the Acquirer with an existing ID
        acquirer.setId(1L);
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcquirerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isBadRequest());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        acquirer.setName(null);

        // Create the Acquirer, which fails.
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        restAcquirerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSocketUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        acquirer.setSocketUrl(null);

        // Create the Acquirer, which fails.
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        restAcquirerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        acquirer.setEmail(null);

        // Create the Acquirer, which fails.
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        restAcquirerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcquirers() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acquirer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].socketUrl").value(hasItem(DEFAULT_SOCKET_URL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getAcquirer() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get the acquirer
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL_ID, acquirer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acquirer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.socketUrl").value(DEFAULT_SOCKET_URL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getAcquirersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        Long id = acquirer.getId();

        defaultAcquirerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAcquirerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAcquirerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAcquirersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where name equals to
        defaultAcquirerFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAcquirersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where name in
        defaultAcquirerFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAcquirersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where name is not null
        defaultAcquirerFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAcquirersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where name contains
        defaultAcquirerFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAcquirersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where name does not contain
        defaultAcquirerFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAcquirersBySocketUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where socketUrl equals to
        defaultAcquirerFiltering("socketUrl.equals=" + DEFAULT_SOCKET_URL, "socketUrl.equals=" + UPDATED_SOCKET_URL);
    }

    @Test
    @Transactional
    void getAllAcquirersBySocketUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where socketUrl in
        defaultAcquirerFiltering("socketUrl.in=" + DEFAULT_SOCKET_URL + "," + UPDATED_SOCKET_URL, "socketUrl.in=" + UPDATED_SOCKET_URL);
    }

    @Test
    @Transactional
    void getAllAcquirersBySocketUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where socketUrl is not null
        defaultAcquirerFiltering("socketUrl.specified=true", "socketUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAcquirersBySocketUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where socketUrl contains
        defaultAcquirerFiltering("socketUrl.contains=" + DEFAULT_SOCKET_URL, "socketUrl.contains=" + UPDATED_SOCKET_URL);
    }

    @Test
    @Transactional
    void getAllAcquirersBySocketUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where socketUrl does not contain
        defaultAcquirerFiltering("socketUrl.doesNotContain=" + UPDATED_SOCKET_URL, "socketUrl.doesNotContain=" + DEFAULT_SOCKET_URL);
    }

    @Test
    @Transactional
    void getAllAcquirersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where email equals to
        defaultAcquirerFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAcquirersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where email in
        defaultAcquirerFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAcquirersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where email is not null
        defaultAcquirerFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAcquirersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where email contains
        defaultAcquirerFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAcquirersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        // Get all the acquirerList where email does not contain
        defaultAcquirerFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    private void defaultAcquirerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAcquirerShouldBeFound(shouldBeFound);
        defaultAcquirerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAcquirerShouldBeFound(String filter) throws Exception {
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acquirer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].socketUrl").value(hasItem(DEFAULT_SOCKET_URL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAcquirerShouldNotBeFound(String filter) throws Exception {
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAcquirerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAcquirer() throws Exception {
        // Get the acquirer
        restAcquirerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAcquirer() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the acquirer
        Acquirer updatedAcquirer = acquirerRepository.findById(acquirer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAcquirer are not directly saved in db
        em.detach(updatedAcquirer);
        updatedAcquirer.name(UPDATED_NAME).socketUrl(UPDATED_SOCKET_URL).email(UPDATED_EMAIL);
        AcquirerDto acquirerDto = acquirerMapper.toDto(updatedAcquirer);

        restAcquirerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acquirerDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(acquirerDto))
            )
            .andExpect(status().isOk());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAcquirerToMatchAllProperties(updatedAcquirer);
    }

    @Test
    @Transactional
    void putNonExistingAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acquirerDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(acquirerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(acquirerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcquirerWithPatch() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the acquirer using partial update
        Acquirer partialUpdatedAcquirer = new Acquirer();
        partialUpdatedAcquirer.setId(acquirer.getId());

        partialUpdatedAcquirer.email(UPDATED_EMAIL);

        restAcquirerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcquirer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAcquirer))
            )
            .andExpect(status().isOk());

        // Validate the Acquirer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAcquirerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAcquirer, acquirer), getPersistedAcquirer(acquirer));
    }

    @Test
    @Transactional
    void fullUpdateAcquirerWithPatch() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the acquirer using partial update
        Acquirer partialUpdatedAcquirer = new Acquirer();
        partialUpdatedAcquirer.setId(acquirer.getId());

        partialUpdatedAcquirer.name(UPDATED_NAME).socketUrl(UPDATED_SOCKET_URL).email(UPDATED_EMAIL);

        restAcquirerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcquirer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAcquirer))
            )
            .andExpect(status().isOk());

        // Validate the Acquirer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAcquirerUpdatableFieldsEquals(partialUpdatedAcquirer, getPersistedAcquirer(partialUpdatedAcquirer));
    }

    @Test
    @Transactional
    void patchNonExistingAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acquirerDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(acquirerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(acquirerDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcquirer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        acquirer.setId(longCount.incrementAndGet());

        // Create the Acquirer
        AcquirerDto acquirerDto = acquirerMapper.toDto(acquirer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquirerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(acquirerDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acquirer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcquirer() throws Exception {
        // Initialize the database
        insertedAcquirer = acquirerRepository.saveAndFlush(acquirer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the acquirer
        restAcquirerMockMvc
            .perform(delete(ENTITY_API_URL_ID, acquirer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return acquirerRepository.count();
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

    protected Acquirer getPersistedAcquirer(Acquirer acquirer) {
        return acquirerRepository.findById(acquirer.getId()).orElseThrow();
    }

    protected void assertPersistedAcquirerToMatchAllProperties(Acquirer expectedAcquirer) {
        assertAcquirerAllPropertiesEquals(expectedAcquirer, getPersistedAcquirer(expectedAcquirer));
    }

    protected void assertPersistedAcquirerToMatchUpdatableProperties(Acquirer expectedAcquirer) {
        assertAcquirerAllUpdatablePropertiesEquals(expectedAcquirer, getPersistedAcquirer(expectedAcquirer));
    }
}
