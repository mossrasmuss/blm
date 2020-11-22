package com.illo.blm.web.rest;

import com.illo.blm.BlmApp;
import com.illo.blm.domain.AttributeValue;
import com.illo.blm.repository.AttributeValueRepository;
import com.illo.blm.repository.search.AttributeValueSearchRepository;

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
 * Integration tests for the {@link AttributeValueResource} REST controller.
 */
@SpringBootTest(classes = BlmApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AttributeValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    /**
     * This repository is mocked in the com.illo.blm.repository.search test package.
     *
     * @see com.illo.blm.repository.search.AttributeValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private AttributeValueSearchRepository mockAttributeValueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createEntity(EntityManager em) {
        AttributeValue attributeValue = new AttributeValue()
            .value(DEFAULT_VALUE)
            .description(DEFAULT_DESCRIPTION);
        return attributeValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createUpdatedEntity(EntityManager em) {
        AttributeValue attributeValue = new AttributeValue()
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION);
        return attributeValue;
    }

    @BeforeEach
    public void initTest() {
        attributeValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttributeValue() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();
        // Create the AttributeValue
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isCreated());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAttributeValue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).save(testAttributeValue);
    }

    @Test
    @Transactional
    public void createAttributeValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // Create the AttributeValue with an existing ID
        attributeValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc.perform(post("/api/attribute-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(0)).save(attributeValue);
    }


    @Test
    @Transactional
    public void getAllAttributeValues() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc.perform(get("/api/attribute-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get("/api/attribute-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).get();
        // Disconnect from session so that the updates on updatedAttributeValue are not directly saved in db
        em.detach(updatedAttributeValue);
        updatedAttributeValue
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION);

        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttributeValue)))
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAttributeValue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).save(testAttributeValue);
    }

    @Test
    @Transactional
    public void updateNonExistingAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc.perform(put("/api/attribute-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(0)).save(attributeValue);
    }

    @Test
    @Transactional
    public void deleteAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeDelete = attributeValueRepository.findAll().size();

        // Delete the attributeValue
        restAttributeValueMockMvc.perform(delete("/api/attribute-values/{id}", attributeValue.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AttributeValue in Elasticsearch
        verify(mockAttributeValueSearchRepository, times(1)).deleteById(attributeValue.getId());
    }

    @Test
    @Transactional
    public void searchAttributeValue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);
        when(mockAttributeValueSearchRepository.search(queryStringQuery("id:" + attributeValue.getId())))
            .thenReturn(Collections.singletonList(attributeValue));

        // Search the attributeValue
        restAttributeValueMockMvc.perform(get("/api/_search/attribute-values?query=id:" + attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
