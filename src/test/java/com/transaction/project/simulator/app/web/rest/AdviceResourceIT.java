package com.transaction.project.simulator.app.web.rest;

import static com.transaction.project.simulator.app.domain.AdviceAsserts.*;
import static com.transaction.project.simulator.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.project.simulator.app.IntegrationTest;
import com.transaction.project.simulator.app.domain.Acquirer;
import com.transaction.project.simulator.app.domain.Advice;
import com.transaction.project.simulator.app.domain.Merchant;
import com.transaction.project.simulator.app.repository.AdviceRepository;
import com.transaction.project.simulator.app.service.dto.AdviceDto;
import com.transaction.project.simulator.app.service.mapper.AdviceMapper;
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
 * Integration tests for the {@link AdviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdviceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/advice";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdviceRepository adviceRepository;

    @Autowired
    private AdviceMapper adviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdviceMockMvc;

    private Advice advice;

    private Advice insertedAdvice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Advice createEntity() {
        return new Advice().code(DEFAULT_CODE).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Advice createUpdatedEntity() {
        return new Advice().code(UPDATED_CODE).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        advice = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdvice != null) {
            adviceRepository.delete(insertedAdvice);
            insertedAdvice = null;
        }
    }

    @Test
    @Transactional
    void createAdvice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);
        var returnedAdviceDto = om.readValue(
            restAdviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdviceDto.class
        );

        // Validate the Advice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdvice = adviceMapper.toEntity(returnedAdviceDto);
        assertAdviceUpdatableFieldsEquals(returnedAdvice, getPersistedAdvice(returnedAdvice));

        insertedAdvice = returnedAdvice;
    }

    @Test
    @Transactional
    void createAdviceWithExistingId() throws Exception {
        // Create the Advice with an existing ID
        advice.setId(1L);
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto)))
            .andExpect(status().isBadRequest());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        advice.setCode(null);

        // Create the Advice, which fails.
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        restAdviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        advice.setName(null);

        // Create the Advice, which fails.
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        restAdviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdvice() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(advice.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAdvice() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get the advice
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL_ID, advice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(advice.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAdviceByIdFiltering() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        Long id = advice.getId();

        defaultAdviceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAdviceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAdviceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdviceByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where code equals to
        defaultAdviceFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAdviceByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where code in
        defaultAdviceFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAdviceByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where code is not null
        defaultAdviceFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAdviceByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where code contains
        defaultAdviceFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAdviceByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where code does not contain
        defaultAdviceFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAdviceByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where name equals to
        defaultAdviceFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdviceByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where name in
        defaultAdviceFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdviceByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where name is not null
        defaultAdviceFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAdviceByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where name contains
        defaultAdviceFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAdviceByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        // Get all the adviceList where name does not contain
        defaultAdviceFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAdviceByMerchantIsEqualToSomething() throws Exception {
        Merchant merchant;
        if (TestUtil.findAll(em, Merchant.class).isEmpty()) {
            adviceRepository.saveAndFlush(advice);
            merchant = MerchantResourceIT.createEntity();
        } else {
            merchant = TestUtil.findAll(em, Merchant.class).get(0);
        }
        em.persist(merchant);
        em.flush();
        advice.setMerchant(merchant);
        adviceRepository.saveAndFlush(advice);
        Long merchantId = merchant.getId();
        // Get all the adviceList where merchant equals to merchantId
        defaultAdviceShouldBeFound("merchantId.equals=" + merchantId);

        // Get all the adviceList where merchant equals to (merchantId + 1)
        defaultAdviceShouldNotBeFound("merchantId.equals=" + (merchantId + 1));
    }

    @Test
    @Transactional
    void getAllAdviceByAcquirerIsEqualToSomething() throws Exception {
        Acquirer acquirer;
        if (TestUtil.findAll(em, Acquirer.class).isEmpty()) {
            adviceRepository.saveAndFlush(advice);
            acquirer = AcquirerResourceIT.createEntity();
        } else {
            acquirer = TestUtil.findAll(em, Acquirer.class).get(0);
        }
        em.persist(acquirer);
        em.flush();
        advice.setAcquirer(acquirer);
        adviceRepository.saveAndFlush(advice);
        Long acquirerId = acquirer.getId();
        // Get all the adviceList where acquirer equals to acquirerId
        defaultAdviceShouldBeFound("acquirerId.equals=" + acquirerId);

        // Get all the adviceList where acquirer equals to (acquirerId + 1)
        defaultAdviceShouldNotBeFound("acquirerId.equals=" + (acquirerId + 1));
    }

    private void defaultAdviceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAdviceShouldBeFound(shouldBeFound);
        defaultAdviceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdviceShouldBeFound(String filter) throws Exception {
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(advice.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdviceShouldNotBeFound(String filter) throws Exception {
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdvice() throws Exception {
        // Get the advice
        restAdviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdvice() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the advice
        Advice updatedAdvice = adviceRepository.findById(advice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdvice are not directly saved in db
        em.detach(updatedAdvice);
        updatedAdvice.code(UPDATED_CODE).name(UPDATED_NAME);
        AdviceDto adviceDto = adviceMapper.toDto(updatedAdvice);

        restAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adviceDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto))
            )
            .andExpect(status().isOk());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdviceToMatchAllProperties(updatedAdvice);
    }

    @Test
    @Transactional
    void putNonExistingAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adviceDto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adviceDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adviceDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdviceWithPatch() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the advice using partial update
        Advice partialUpdatedAdvice = new Advice();
        partialUpdatedAdvice.setId(advice.getId());

        partialUpdatedAdvice.name(UPDATED_NAME);

        restAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdvice))
            )
            .andExpect(status().isOk());

        // Validate the Advice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdviceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAdvice, advice), getPersistedAdvice(advice));
    }

    @Test
    @Transactional
    void fullUpdateAdviceWithPatch() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the advice using partial update
        Advice partialUpdatedAdvice = new Advice();
        partialUpdatedAdvice.setId(advice.getId());

        partialUpdatedAdvice.code(UPDATED_CODE).name(UPDATED_NAME);

        restAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdvice))
            )
            .andExpect(status().isOk());

        // Validate the Advice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdviceUpdatableFieldsEquals(partialUpdatedAdvice, getPersistedAdvice(partialUpdatedAdvice));
    }

    @Test
    @Transactional
    void patchNonExistingAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adviceDto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adviceDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adviceDto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdvice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        advice.setId(longCount.incrementAndGet());

        // Create the Advice
        AdviceDto adviceDto = adviceMapper.toDto(advice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adviceDto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Advice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdvice() throws Exception {
        // Initialize the database
        insertedAdvice = adviceRepository.saveAndFlush(advice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the advice
        restAdviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, advice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adviceRepository.count();
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

    protected Advice getPersistedAdvice(Advice advice) {
        return adviceRepository.findById(advice.getId()).orElseThrow();
    }

    protected void assertPersistedAdviceToMatchAllProperties(Advice expectedAdvice) {
        assertAdviceAllPropertiesEquals(expectedAdvice, getPersistedAdvice(expectedAdvice));
    }

    protected void assertPersistedAdviceToMatchUpdatableProperties(Advice expectedAdvice) {
        assertAdviceAllUpdatablePropertiesEquals(expectedAdvice, getPersistedAdvice(expectedAdvice));
    }
}
