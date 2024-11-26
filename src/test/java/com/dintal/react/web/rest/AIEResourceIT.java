package com.dintal.react.web.rest;

import static com.dintal.react.domain.AIEAsserts.*;
import static com.dintal.react.web.rest.TestUtil.createUpdateProxyForBean;
import static com.dintal.react.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.dintal.react.IntegrationTest;
import com.dintal.react.domain.AIE;
import com.dintal.react.repository.AIERepository;
import com.dintal.react.repository.EntityManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AIEResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AIEResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final String DEFAULT_AIE_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_AIE_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AIERepository aIERepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AIE aIE;

    private AIE insertedAIE;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIE createEntity() {
        return new AIE()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .icon(DEFAULT_ICON)
            .version(DEFAULT_VERSION)
            .category(DEFAULT_CATEGORY)
            .rate(DEFAULT_RATE)
            .aieMetadata(DEFAULT_AIE_METADATA)
            .userID(DEFAULT_USER_ID)
            .isPublic(DEFAULT_IS_PUBLIC)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .tenantID(DEFAULT_TENANT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIE createUpdatedEntity() {
        return new AIE()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .icon(UPDATED_ICON)
            .version(UPDATED_VERSION)
            .category(UPDATED_CATEGORY)
            .rate(UPDATED_RATE)
            .aieMetadata(UPDATED_AIE_METADATA)
            .userID(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .tenantID(UPDATED_TENANT_ID);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AIE.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        aIE = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAIE != null) {
            aIERepository.delete(insertedAIE).block();
            insertedAIE = null;
        }
        deleteEntities(em);
    }

    @Test
    void createAIE() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AIE
        var returnedAIE = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AIE.class)
            .returnResult()
            .getResponseBody();

        // Validate the AIE in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAIEUpdatableFieldsEquals(returnedAIE, getPersistedAIE(returnedAIE));

        insertedAIE = returnedAIE;
    }

    @Test
    void createAIEWithExistingId() throws Exception {
        // Create the AIE with an existing ID
        aIE.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setName(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setType(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setCreatedAt(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setCreatedBy(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAieMetadataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setAieMetadata(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUserIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setUserID(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIE.setIsPublic(null);

        // Create the AIE, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAIES() {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        // Get all the aIEList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(aIE.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].version")
            .value(hasItem(DEFAULT_VERSION))
            .jsonPath("$.[*].category")
            .value(hasItem(DEFAULT_CATEGORY))
            .jsonPath("$.[*].rate")
            .value(hasItem(DEFAULT_RATE.doubleValue()))
            .jsonPath("$.[*].aieMetadata")
            .value(hasItem(DEFAULT_AIE_METADATA))
            .jsonPath("$.[*].userID")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].isPublic")
            .value(hasItem(DEFAULT_IS_PUBLIC.booleanValue()))
            .jsonPath("$.[*].organizationName")
            .value(hasItem(DEFAULT_ORGANIZATION_NAME))
            .jsonPath("$.[*].tenantID")
            .value(hasItem(DEFAULT_TENANT_ID));
    }

    @Test
    void getAIE() {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        // Get the aIE
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aIE.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aIE.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.createdAt")
            .value(is(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.version")
            .value(is(DEFAULT_VERSION))
            .jsonPath("$.category")
            .value(is(DEFAULT_CATEGORY))
            .jsonPath("$.rate")
            .value(is(DEFAULT_RATE.doubleValue()))
            .jsonPath("$.aieMetadata")
            .value(is(DEFAULT_AIE_METADATA))
            .jsonPath("$.userID")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.isPublic")
            .value(is(DEFAULT_IS_PUBLIC.booleanValue()))
            .jsonPath("$.organizationName")
            .value(is(DEFAULT_ORGANIZATION_NAME))
            .jsonPath("$.tenantID")
            .value(is(DEFAULT_TENANT_ID));
    }

    @Test
    void getNonExistingAIE() {
        // Get the aIE
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAIE() throws Exception {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIE
        AIE updatedAIE = aIERepository.findById(aIE.getId()).block();
        updatedAIE
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .icon(UPDATED_ICON)
            .version(UPDATED_VERSION)
            .category(UPDATED_CATEGORY)
            .rate(UPDATED_RATE)
            .aieMetadata(UPDATED_AIE_METADATA)
            .userID(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .tenantID(UPDATED_TENANT_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAIE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedAIE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAIEToMatchAllProperties(updatedAIE);
    }

    @Test
    void putNonExistingAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aIE.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAIEWithPatch() throws Exception {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIE using partial update
        AIE partialUpdatedAIE = new AIE();
        partialUpdatedAIE.setId(aIE.getId());

        partialUpdatedAIE.type(UPDATED_TYPE).icon(UPDATED_ICON).userID(UPDATED_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAIE.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAIE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AIE in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIEUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAIE, aIE), getPersistedAIE(aIE));
    }

    @Test
    void fullUpdateAIEWithPatch() throws Exception {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIE using partial update
        AIE partialUpdatedAIE = new AIE();
        partialUpdatedAIE.setId(aIE.getId());

        partialUpdatedAIE
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .icon(UPDATED_ICON)
            .version(UPDATED_VERSION)
            .category(UPDATED_CATEGORY)
            .rate(UPDATED_RATE)
            .aieMetadata(UPDATED_AIE_METADATA)
            .userID(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .tenantID(UPDATED_TENANT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAIE.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAIE))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AIE in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIEUpdatableFieldsEquals(partialUpdatedAIE, getPersistedAIE(partialUpdatedAIE));
    }

    @Test
    void patchNonExistingAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aIE.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAIE() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIE.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(aIE))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AIE in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAIE() {
        // Initialize the database
        aIE.setId(UUID.randomUUID().toString());
        insertedAIE = aIERepository.save(aIE).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aIE
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aIE.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aIERepository.count().block();
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

    protected AIE getPersistedAIE(AIE aIE) {
        return aIERepository.findById(aIE.getId()).block();
    }

    protected void assertPersistedAIEToMatchAllProperties(AIE expectedAIE) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAIEAllPropertiesEquals(expectedAIE, getPersistedAIE(expectedAIE));
        assertAIEUpdatableFieldsEquals(expectedAIE, getPersistedAIE(expectedAIE));
    }

    protected void assertPersistedAIEToMatchUpdatableProperties(AIE expectedAIE) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAIEAllUpdatablePropertiesEquals(expectedAIE, getPersistedAIE(expectedAIE));
        assertAIEUpdatableFieldsEquals(expectedAIE, getPersistedAIE(expectedAIE));
    }
}
