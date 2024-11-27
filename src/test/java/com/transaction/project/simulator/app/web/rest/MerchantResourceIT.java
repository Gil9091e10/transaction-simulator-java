package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.MerchantAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.repository.MerchantRepository;
import com.transaction.project.simulator.app.service.dto.MerchantDto;
import com.transaction.project.simulator.app.service.mapper.MerchantMapper;
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
 * Integration tests for the {@link MerchantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MerchantResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MCC = "AAAAAAAAAA";
    private static final String UPDATED_MCC = "BBBBBBBBBB";

    private static final Integer DEFAULT_POSTAL_CODE = 1;
    private static final Integer UPDATED_POSTAL_CODE = 2;
    private static final Integer SMALLER_POSTAL_CODE = 1 - 1;

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/merchants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    private Merchant insertedMerchant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createEntity() {
        return new Merchant().name(DEFAULT_NAME).mcc(DEFAULT_MCC).postalCode(DEFAULT_POSTAL_CODE).website(DEFAULT_WEBSITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createUpdatedEntity() {
        return new Merchant().name(UPDATED_NAME).mcc(UPDATED_MCC).postalCode(UPDATED_POSTAL_CODE).website(UPDATED_WEBSITE);
    }

    @BeforeEach
    public void initTest() {
        merchant = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMerchant != null) {
            merchantRepository.delete(insertedMerchant);
            insertedMerchant = null;
        }
    }

    @Test
    @Transactional
    void createMerchant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);
        var returnedMerchantDto = om.readValue(
            restMerchantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(merchantDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MerchantDto.class
        );

        // Validate the Merchant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMerchant = merchantMapper.toEntity(returnedMerchantDto);
        assertMerchantUpdatableFieldsEquals(returnedMerchant, getPersistedMerchant(returnedMerchant));

        insertedMerchant = returnedMerchant;
    }

    @Test
    @Transactional
    void createMerchantWithExistingId() throws Exception {
        // Create the Merchant with an existing ID
        merchant.setId(1L);
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(merchantDto)))
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        merchant.setName(null);

        // Create the Merchant, which fails.
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(merchantDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMccIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        merchant.setMcc(null);

        // Create the Merchant, which fails.
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(merchantDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMerchants() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcc").value(hasItem(DEFAULT_MCC)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    void getMerchant() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL_ID, merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mcc").value(DEFAULT_MCC))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    void getMerchantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        Long id = merchant.getId();

        defaultMerchantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMerchantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMerchantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name equals to
        defaultMerchantFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name in
        defaultMerchantFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name is not null
        defaultMerchantFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name contains
        defaultMerchantFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where name does not contain
        defaultMerchantFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMerchantsByMccIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where mcc equals to
        defaultMerchantFiltering("mcc.equals=" + DEFAULT_MCC, "mcc.equals=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    void getAllMerchantsByMccIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where mcc in
        defaultMerchantFiltering("mcc.in=" + DEFAULT_MCC + "," + UPDATED_MCC, "mcc.in=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    void getAllMerchantsByMccIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where mcc is not null
        defaultMerchantFiltering("mcc.specified=true", "mcc.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByMccContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where mcc contains
        defaultMerchantFiltering("mcc.contains=" + DEFAULT_MCC, "mcc.contains=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    void getAllMerchantsByMccNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where mcc does not contain
        defaultMerchantFiltering("mcc.doesNotContain=" + UPDATED_MCC, "mcc.doesNotContain=" + DEFAULT_MCC);
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode equals to
        defaultMerchantFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode in
        defaultMerchantFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode is not null
        defaultMerchantFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode is greater than or equal to
        defaultMerchantFiltering(
            "postalCode.greaterThanOrEqual=" + DEFAULT_POSTAL_CODE,
            "postalCode.greaterThanOrEqual=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode is less than or equal to
        defaultMerchantFiltering("postalCode.lessThanOrEqual=" + DEFAULT_POSTAL_CODE, "postalCode.lessThanOrEqual=" + SMALLER_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode is less than
        defaultMerchantFiltering("postalCode.lessThan=" + UPDATED_POSTAL_CODE, "postalCode.lessThan=" + DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByPostalCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where postalCode is greater than
        defaultMerchantFiltering("postalCode.greaterThan=" + SMALLER_POSTAL_CODE, "postalCode.greaterThan=" + DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantsByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where website equals to
        defaultMerchantFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllMerchantsByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where website in
        defaultMerchantFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllMerchantsByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where website is not null
        defaultMerchantFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantsByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where website contains
        defaultMerchantFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllMerchantsByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList where website does not contain
        defaultMerchantFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    private void defaultMerchantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMerchantShouldBeFound(shouldBeFound);
        defaultMerchantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMerchantShouldBeFound(String filter) throws Exception {
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcc").value(hasItem(DEFAULT_MCC)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));

        // Check, that the count call also returns 1
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMerchantShouldNotBeFound(String filter) throws Exception {
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMerchant() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the merchant
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMerchant are not directly saved in db
        em.detach(updatedMerchant);
        updatedMerchant.name(UPDATED_NAME).mcc(UPDATED_MCC).postalCode(UPDATED_POSTAL_CODE).website(UPDATED_WEBSITE);
        MerchantDto merchantDto = merchantMapper.toDto(updatedMerchant);

        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(merchantDto))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMerchantToMatchAllProperties(updatedMerchant);
    }

    @Test
    @Transactional
    void putNonExistingMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(merchantDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(merchantDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(merchantDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant.name(UPDATED_NAME).mcc(UPDATED_MCC).website(UPDATED_WEBSITE);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMerchantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMerchant, merchant), getPersistedMerchant(merchant));
    }

    @Test
    @Transactional
    void fullUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant.name(UPDATED_NAME).mcc(UPDATED_MCC).postalCode(UPDATED_POSTAL_CODE).website(UPDATED_WEBSITE);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMerchantUpdatableFieldsEquals(partialUpdatedMerchant, getPersistedMerchant(partialUpdatedMerchant));
    }

    @Test
    @Transactional
    void patchNonExistingMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, merchantDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(merchantDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(merchantDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMerchant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        merchant.setId(longCount.incrementAndGet());

        // Create the Merchant
        MerchantDto merchantDto = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(merchantDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMerchant() throws Exception {
        // Initialize the database
        insertedMerchant = merchantRepository.saveAndFlush(merchant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the merchant
        restMerchantMockMvc
            .perform(delete(ENTITY_API_URL_ID, merchant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return merchantRepository.count();
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

    protected Merchant getPersistedMerchant(Merchant merchant) {
        return merchantRepository.findById(merchant.getId()).orElseThrow();
    }

    protected void assertPersistedMerchantToMatchAllProperties(Merchant expectedMerchant) {
        assertMerchantAllPropertiesEquals(expectedMerchant, getPersistedMerchant(expectedMerchant));
    }

    protected void assertPersistedMerchantToMatchUpdatableProperties(Merchant expectedMerchant) {
        assertMerchantAllUpdatablePropertiesEquals(expectedMerchant, getPersistedMerchant(expectedMerchant));
    }
}
