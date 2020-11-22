package com.illo.blm.web.rest;

import com.illo.blm.BlmApp;
import com.illo.blm.domain.PropertyGroup;
import com.illo.blm.repository.PropertyGroupRepository;
import com.illo.blm.repository.search.PropertyGroupSearchRepository;

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
 * Integration tests for the {@link PropertyGroupResource} REST controller.
 */
@SpringBootTest(classes = BlmApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PropertyGroupResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PropertyGroupRepository propertyGroupRepository;

    /**
     * This repository is mocked in the com.illo.blm.repository.search test package.
     *
     * @see com.illo.blm.repository.search.PropertyGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private PropertyGroupSearchRepository mockPropertyGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropertyGroupMockMvc;

    private PropertyGroup propertyGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyGroup createEntity(EntityManager em) {
        PropertyGroup propertyGroup = new PropertyGroup()
            .groupName(DEFAULT_GROUP_NAME)
            .description(DEFAULT_DESCRIPTION);
        return propertyGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyGroup createUpdatedEntity(EntityManager em) {
        PropertyGroup propertyGroup = new PropertyGroup()
            .groupName(UPDATED_GROUP_NAME)
            .description(UPDATED_DESCRIPTION);
        return propertyGroup;
    }

    @BeforeEach
    public void initTest() {
        propertyGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropertyGroup() throws Exception {
        int databaseSizeBeforeCreate = propertyGroupRepository.findAll().size();
        // Create the PropertyGroup
        restPropertyGroupMockMvc.perform(post("/api/property-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyGroup)))
            .andExpect(status().isCreated());

        // Validate the PropertyGroup in the database
        List<PropertyGroup> propertyGroupList = propertyGroupRepository.findAll();
        assertThat(propertyGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PropertyGroup testPropertyGroup = propertyGroupList.get(propertyGroupList.size() - 1);
        assertThat(testPropertyGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testPropertyGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the PropertyGroup in Elasticsearch
        verify(mockPropertyGroupSearchRepository, times(1)).save(testPropertyGroup);
    }

    @Test
    @Transactional
    public void createPropertyGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propertyGroupRepository.findAll().size();

        // Create the PropertyGroup with an existing ID
        propertyGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyGroupMockMvc.perform(post("/api/property-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyGroup in the database
        List<PropertyGroup> propertyGroupList = propertyGroupRepository.findAll();
        assertThat(propertyGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the PropertyGroup in Elasticsearch
        verify(mockPropertyGroupSearchRepository, times(0)).save(propertyGroup);
    }


    @Test
    @Transactional
    public void getAllPropertyGroups() throws Exception {
        // Initialize the database
        propertyGroupRepository.saveAndFlush(propertyGroup);

        // Get all the propertyGroupList
        restPropertyGroupMockMvc.perform(get("/api/property-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getPropertyGroup() throws Exception {
        // Initialize the database
        propertyGroupRepository.saveAndFlush(propertyGroup);

        // Get the propertyGroup
        restPropertyGroupMockMvc.perform(get("/api/property-groups/{id}", propertyGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(propertyGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingPropertyGroup() throws Exception {
        // Get the propertyGroup
        restPropertyGroupMockMvc.perform(get("/api/property-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropertyGroup() throws Exception {
        // Initialize the database
        propertyGroupRepository.saveAndFlush(propertyGroup);

        int databaseSizeBeforeUpdate = propertyGroupRepository.findAll().size();

        // Update the propertyGroup
        PropertyGroup updatedPropertyGroup = propertyGroupRepository.findById(propertyGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPropertyGroup are not directly saved in db
        em.detach(updatedPropertyGroup);
        updatedPropertyGroup
            .groupName(UPDATED_GROUP_NAME)
            .description(UPDATED_DESCRIPTION);

        restPropertyGroupMockMvc.perform(put("/api/property-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPropertyGroup)))
            .andExpect(status().isOk());

        // Validate the PropertyGroup in the database
        List<PropertyGroup> propertyGroupList = propertyGroupRepository.findAll();
        assertThat(propertyGroupList).hasSize(databaseSizeBeforeUpdate);
        PropertyGroup testPropertyGroup = propertyGroupList.get(propertyGroupList.size() - 1);
        assertThat(testPropertyGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testPropertyGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the PropertyGroup in Elasticsearch
        verify(mockPropertyGroupSearchRepository, times(1)).save(testPropertyGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingPropertyGroup() throws Exception {
        int databaseSizeBeforeUpdate = propertyGroupRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyGroupMockMvc.perform(put("/api/property-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(propertyGroup)))
            .andExpect(status().isBadRequest());

        // Validate the PropertyGroup in the database
        List<PropertyGroup> propertyGroupList = propertyGroupRepository.findAll();
        assertThat(propertyGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PropertyGroup in Elasticsearch
        verify(mockPropertyGroupSearchRepository, times(0)).save(propertyGroup);
    }

    @Test
    @Transactional
    public void deletePropertyGroup() throws Exception {
        // Initialize the database
        propertyGroupRepository.saveAndFlush(propertyGroup);

        int databaseSizeBeforeDelete = propertyGroupRepository.findAll().size();

        // Delete the propertyGroup
        restPropertyGroupMockMvc.perform(delete("/api/property-groups/{id}", propertyGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PropertyGroup> propertyGroupList = propertyGroupRepository.findAll();
        assertThat(propertyGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PropertyGroup in Elasticsearch
        verify(mockPropertyGroupSearchRepository, times(1)).deleteById(propertyGroup.getId());
    }

    @Test
    @Transactional
    public void searchPropertyGroup() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        propertyGroupRepository.saveAndFlush(propertyGroup);
        when(mockPropertyGroupSearchRepository.search(queryStringQuery("id:" + propertyGroup.getId())))
            .thenReturn(Collections.singletonList(propertyGroup));

        // Search the propertyGroup
        restPropertyGroupMockMvc.perform(get("/api/_search/property-groups?query=id:" + propertyGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
