package com.illo.blm.web.rest;

import com.illo.blm.BlmApp;
import com.illo.blm.domain.PropertyPricing;
import com.illo.blm.repository.PropertyPricingRepository;
import com.illo.blm.repository.search.PropertyPricingSearchRepository;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PropertyPricingResource} REST controller.
 */
@SpringBootTest(classes = BlmApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PropertyPricingResourceIT {

    private static final Double DEFAULT_DEFAULT_PRICE = 1D;
    private static final Double UPDATED_DEFAULT_PRICE = 2D;

    private static final Double DEFAULT_CURRENT_PRICE = 1D;
    private static final Double UPDATED_CURRENT_PRICE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PropertyPricingRepository propertyPricingRepository;

    /**
     * This repository is mocked in the com.illo.blm.repository.search test package.
     *
     * @see com.illo.blm.repository.search.PropertyPricingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PropertyPricingSearchRepository mockPropertyPricingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropertyPricingMockMvc;

    private PropertyPricing propertyPricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyPricing createEntity(EntityManager em) {
        PropertyPricing propertyPricing = new PropertyPricing()
            .defaultPrice(DEFAULT_DEFAULT_PRICE)
            .currentPrice(DEFAULT_CURRENT_PRICE)
            .description(DEFAULT_DESCRIPTION);
        return propertyPricing;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyPricing createUpdatedEntity(EntityManager em) {
        PropertyPricing propertyPricing = new PropertyPricing()
            .defaultPrice(UPDATED_DEFAULT_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .description(UPDATED_DESCRIPTION);
        return propertyPricing;
    }

    @BeforeEach
    public void initTest() {
        propertyPricing = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropertyPricing() throws Exception {
        int databaseSizeBeforeCreate = propertyPricingRepository.findAll().size();
        // Create the PropertyPricing
        restPropertyPricingMockMvc.perform(post("/api/property-pricings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyPricing)))
            .andExpect(status().isCreated());

        // Validate the PropertyPricing in the database
        List<PropertyPricing> propertyPricingList = propertyPricingRepository.findAll();
        assertThat(propertyPricingList).hasSize(databaseSizeBeforeCreate + 1);
        PropertyPricing testPropertyPricing = propertyPricingList.get(propertyPricingList.size() - 1);
        assertThat(testPropertyPricing.getDefaultPrice()).isEqualTo(DEFAULT_DEFAULT_PRICE);
        assertThat(testPropertyPricing.getCurrentPrice()).isEqualTo(DEFAULT_CURRENT_PRICE);
        assertThat(testPropertyPricing.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the PropertyPricing in Elasticsearch
        verify(mockPropertyPricingSearchRepository, times(1)).save(testPropertyPricing);
    }

    @Test
    @Transactional
    public void createPropertyPricingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyPricingRepository.findAll().size();

        // Create the PropertyPricing with an existing ID
        propertyPricing.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyPricingMockMvc.perform(post("/api/property-pricings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyPricing)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyPricing in the database
        List<PropertyPricing> propertyPricingList = propertyPricingRepository.findAll();
        assertThat(propertyPricingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PropertyPricing in Elasticsearch
        verify(mockPropertyPricingSearchRepository, times(0)).save(propertyPricing);
    }


    @Test
    @Transactional
    public void getAllPropertyPricings() throws Exception {
        // Initialize the database
        propertyPricingRepository.saveAndFlush(propertyPricing);

        // Get all the propertyPricingList
        restPropertyPricingMockMvc.perform(get("/api/property-pricings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyPricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultPrice").value(hasItem(DEFAULT_DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(DEFAULT_CURRENT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getPropertyPricing() throws Exception {
        // Initialize the database
        propertyPricingRepository.saveAndFlush(propertyPricing);

        // Get the propertyPricing
        restPropertyPricingMockMvc.perform(get("/api/property-pricings/{id}", propertyPricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(propertyPricing.getId().intValue()))
            .andExpect(jsonPath("$.defaultPrice").value(DEFAULT_DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.currentPrice").value(DEFAULT_CURRENT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingPropertyPricing() throws Exception {
        // Get the propertyPricing
        restPropertyPricingMockMvc.perform(get("/api/property-pricings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropertyPricing() throws Exception {
        // Initialize the database
        propertyPricingRepository.saveAndFlush(propertyPricing);

        int databaseSizeBeforeUpdate = propertyPricingRepository.findAll().size();

        // Update the propertyPricing
        PropertyPricing updatedPropertyPricing = propertyPricingRepository.findById(propertyPricing.getId()).get();
        // Disconnect from session so that the updates on updatedPropertyPricing are not directly saved in db
        em.detach(updatedPropertyPricing);
        updatedPropertyPricing
            .defaultPrice(UPDATED_DEFAULT_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .description(UPDATED_DESCRIPTION);

        restPropertyPricingMockMvc.perform(put("/api/property-pricings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPropertyPricing)))
            .andExpect(status().isOk());

        // Validate the PropertyPricing in the database
        List<PropertyPricing> propertyPricingList = propertyPricingRepository.findAll();
        assertThat(propertyPricingList).hasSize(databaseSizeBeforeUpdate);
        PropertyPricing testPropertyPricing = propertyPricingList.get(propertyPricingList.size() - 1);
        assertThat(testPropertyPricing.getDefaultPrice()).isEqualTo(UPDATED_DEFAULT_PRICE);
        assertThat(testPropertyPricing.getCurrentPrice()).isEqualTo(UPDATED_CURRENT_PRICE);
        assertThat(testPropertyPricing.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the PropertyPricing in Elasticsearch
        verify(mockPropertyPricingSearchRepository, times(1)).save(testPropertyPricing);
    }

    @Test
    @Transactional
    public void updateNonExistingPropertyPricing() throws Exception {
        int databaseSizeBeforeUpdate = propertyPricingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyPricingMockMvc.perform(put("/api/property-pricings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyPricing)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyPricing in the database
        List<PropertyPricing> propertyPricingList = propertyPricingRepository.findAll();
        assertThat(propertyPricingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PropertyPricing in Elasticsearch
        verify(mockPropertyPricingSearchRepository, times(0)).save(propertyPricing);
    }

    @Test
    @Transactional
    public void deletePropertyPricing() throws Exception {
        // Initialize the database
        propertyPricingRepository.saveAndFlush(propertyPricing);

        int databaseSizeBeforeDelete = propertyPricingRepository.findAll().size();

        // Delete the propertyPricing
        restPropertyPricingMockMvc.perform(delete("/api/property-pricings/{id}", propertyPricing.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PropertyPricing> propertyPricingList = propertyPricingRepository.findAll();
        assertThat(propertyPricingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PropertyPricing in Elasticsearch
        verify(mockPropertyPricingSearchRepository, times(1)).deleteById(propertyPricing.getId());
    }

    @Test
    @Transactional
    public void searchPropertyPricing() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        propertyPricingRepository.saveAndFlush(propertyPricing);
        when(mockPropertyPricingSearchRepository.search(queryStringQuery("id:" + propertyPricing.getId())))
            .thenReturn(Collections.singletonList(propertyPricing));

        // Search the propertyPricing
        restPropertyPricingMockMvc.perform(get("/api/_search/property-pricings?query=id:" + propertyPricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyPricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultPrice").value(hasItem(DEFAULT_DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(DEFAULT_CURRENT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
