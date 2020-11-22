package com.illo.blm.web.rest;

import com.illo.blm.BlmApp;
import com.illo.blm.domain.SalesProperty;
import com.illo.blm.repository.SalesPropertyRepository;
import com.illo.blm.repository.search.SalesPropertySearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.illo.blm.domain.enumeration.SalesType;
import com.illo.blm.domain.enumeration.SalesStatus;
/**
 * Integration tests for the {@link SalesPropertyResource} REST controller.
 */
@SpringBootTest(classes = BlmApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SalesPropertyResourceIT {

    private static final Instant DEFAULT_DATE_POSTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_POSTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SalesType DEFAULT_TYPE = SalesType.SALES;
    private static final SalesType UPDATED_TYPE = SalesType.RENTAL;

    private static final SalesStatus DEFAULT_STATUS = SalesStatus.SOLD;
    private static final SalesStatus UPDATED_STATUS = SalesStatus.AVAILABLE;

    private static final Double DEFAULT_DEFAULT_PRICE = 1D;
    private static final Double UPDATED_DEFAULT_PRICE = 2D;

    @Autowired
    private SalesPropertyRepository salesPropertyRepository;

    /**
     * This repository is mocked in the com.illo.blm.repository.search test package.
     *
     * @see com.illo.blm.repository.search.SalesPropertySearchRepositoryMockConfiguration
     */
    @Autowired
    private SalesPropertySearchRepository mockSalesPropertySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesPropertyMockMvc;

    private SalesProperty salesProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesProperty createEntity(EntityManager em) {
        SalesProperty salesProperty = new SalesProperty()
            .datePosted(DEFAULT_DATE_POSTED)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .defaultPrice(DEFAULT_DEFAULT_PRICE);
        return salesProperty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesProperty createUpdatedEntity(EntityManager em) {
        SalesProperty salesProperty = new SalesProperty()
            .datePosted(UPDATED_DATE_POSTED)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .defaultPrice(UPDATED_DEFAULT_PRICE);
        return salesProperty;
    }

    @BeforeEach
    public void initTest() {
        salesProperty = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesProperty() throws Exception {
        int databaseSizeBeforeCreate = salesPropertyRepository.findAll().size();
        // Create the SalesProperty
        restSalesPropertyMockMvc.perform(post("/api/sales-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesProperty)))
            .andExpect(status().isCreated());

        // Validate the SalesProperty in the database
        List<SalesProperty> salesPropertyList = salesPropertyRepository.findAll();
        assertThat(salesPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        SalesProperty testSalesProperty = salesPropertyList.get(salesPropertyList.size() - 1);
        assertThat(testSalesProperty.getDatePosted()).isEqualTo(DEFAULT_DATE_POSTED);
        assertThat(testSalesProperty.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testSalesProperty.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSalesProperty.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSalesProperty.getDefaultPrice()).isEqualTo(DEFAULT_DEFAULT_PRICE);

        // Validate the SalesProperty in Elasticsearch
        verify(mockSalesPropertySearchRepository, times(1)).save(testSalesProperty);
    }

    @Test
    @Transactional
    public void createSalesPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesPropertyRepository.findAll().size();

        // Create the SalesProperty with an existing ID
        salesProperty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesPropertyMockMvc.perform(post("/api/sales-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesProperty)))
            .andExpect(status().isBadRequest());

        // Validate the SalesProperty in the database
        List<SalesProperty> salesPropertyList = salesPropertyRepository.findAll();
        assertThat(salesPropertyList).hasSize(databaseSizeBeforeCreate);

        // Validate the SalesProperty in Elasticsearch
        verify(mockSalesPropertySearchRepository, times(0)).save(salesProperty);
    }


    @Test
    @Transactional
    public void getAllSalesProperties() throws Exception {
        // Initialize the database
        salesPropertyRepository.saveAndFlush(salesProperty);

        // Get all the salesPropertyList
        restSalesPropertyMockMvc.perform(get("/api/sales-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePosted").value(hasItem(DEFAULT_DATE_POSTED.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].defaultPrice").value(hasItem(DEFAULT_DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSalesProperty() throws Exception {
        // Initialize the database
        salesPropertyRepository.saveAndFlush(salesProperty);

        // Get the salesProperty
        restSalesPropertyMockMvc.perform(get("/api/sales-properties/{id}", salesProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesProperty.getId().intValue()))
            .andExpect(jsonPath("$.datePosted").value(DEFAULT_DATE_POSTED.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.defaultPrice").value(DEFAULT_DEFAULT_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSalesProperty() throws Exception {
        // Get the salesProperty
        restSalesPropertyMockMvc.perform(get("/api/sales-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesProperty() throws Exception {
        // Initialize the database
        salesPropertyRepository.saveAndFlush(salesProperty);

        int databaseSizeBeforeUpdate = salesPropertyRepository.findAll().size();

        // Update the salesProperty
        SalesProperty updatedSalesProperty = salesPropertyRepository.findById(salesProperty.getId()).get();
        // Disconnect from session so that the updates on updatedSalesProperty are not directly saved in db
        em.detach(updatedSalesProperty);
        updatedSalesProperty
            .datePosted(UPDATED_DATE_POSTED)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .defaultPrice(UPDATED_DEFAULT_PRICE);

        restSalesPropertyMockMvc.perform(put("/api/sales-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesProperty)))
            .andExpect(status().isOk());

        // Validate the SalesProperty in the database
        List<SalesProperty> salesPropertyList = salesPropertyRepository.findAll();
        assertThat(salesPropertyList).hasSize(databaseSizeBeforeUpdate);
        SalesProperty testSalesProperty = salesPropertyList.get(salesPropertyList.size() - 1);
        assertThat(testSalesProperty.getDatePosted()).isEqualTo(UPDATED_DATE_POSTED);
        assertThat(testSalesProperty.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testSalesProperty.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSalesProperty.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSalesProperty.getDefaultPrice()).isEqualTo(UPDATED_DEFAULT_PRICE);

        // Validate the SalesProperty in Elasticsearch
        verify(mockSalesPropertySearchRepository, times(1)).save(testSalesProperty);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesProperty() throws Exception {
        int databaseSizeBeforeUpdate = salesPropertyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesPropertyMockMvc.perform(put("/api/sales-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesProperty)))
            .andExpect(status().isBadRequest());

        // Validate the SalesProperty in the database
        List<SalesProperty> salesPropertyList = salesPropertyRepository.findAll();
        assertThat(salesPropertyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SalesProperty in Elasticsearch
        verify(mockSalesPropertySearchRepository, times(0)).save(salesProperty);
    }

    @Test
    @Transactional
    public void deleteSalesProperty() throws Exception {
        // Initialize the database
        salesPropertyRepository.saveAndFlush(salesProperty);

        int databaseSizeBeforeDelete = salesPropertyRepository.findAll().size();

        // Delete the salesProperty
        restSalesPropertyMockMvc.perform(delete("/api/sales-properties/{id}", salesProperty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesProperty> salesPropertyList = salesPropertyRepository.findAll();
        assertThat(salesPropertyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SalesProperty in Elasticsearch
        verify(mockSalesPropertySearchRepository, times(1)).deleteById(salesProperty.getId());
    }

    @Test
    @Transactional
    public void searchSalesProperty() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        salesPropertyRepository.saveAndFlush(salesProperty);
        when(mockSalesPropertySearchRepository.search(queryStringQuery("id:" + salesProperty.getId())))
            .thenReturn(Collections.singletonList(salesProperty));

        // Search the salesProperty
        restSalesPropertyMockMvc.perform(get("/api/_search/sales-properties?query=id:" + salesProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePosted").value(hasItem(DEFAULT_DATE_POSTED.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].defaultPrice").value(hasItem(DEFAULT_DEFAULT_PRICE.doubleValue())));
    }
}
