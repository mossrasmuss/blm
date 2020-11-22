package com.illo.blm.web.rest;

import com.illo.blm.BlmApp;
import com.illo.blm.domain.Privilege;
import com.illo.blm.repository.PrivilegeRepository;
import com.illo.blm.repository.search.PrivilegeSearchRepository;

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
 * Integration tests for the {@link PrivilegeResource} REST controller.
 */
@SpringBootTest(classes = BlmApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PrivilegeResourceIT {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    /**
     * This repository is mocked in the com.illo.blm.repository.search test package.
     *
     * @see com.illo.blm.repository.search.PrivilegeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrivilegeSearchRepository mockPrivilegeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrivilegeMockMvc;

    private Privilege privilege;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Privilege createEntity(EntityManager em) {
        Privilege privilege = new Privilege();
        return privilege;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Privilege createUpdatedEntity(EntityManager em) {
        Privilege privilege = new Privilege();
        return privilege;
    }

    @BeforeEach
    public void initTest() {
        privilege = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrivilege() throws Exception {
        int databaseSizeBeforeCreate = privilegeRepository.findAll().size();
        // Create the Privilege
        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isCreated());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeCreate + 1);
        Privilege testPrivilege = privilegeList.get(privilegeList.size() - 1);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).save(testPrivilege);
    }

    @Test
    @Transactional
    public void createPrivilegeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = privilegeRepository.findAll().size();

        // Create the Privilege with an existing ID
        privilege.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(0)).save(privilege);
    }


    @Test
    @Transactional
    public void getAllPrivileges() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        // Get all the privilegeList
        restPrivilegeMockMvc.perform(get("/api/privileges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privilege.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPrivilege() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        // Get the privilege
        restPrivilegeMockMvc.perform(get("/api/privileges/{id}", privilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(privilege.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPrivilege() throws Exception {
        // Get the privilege
        restPrivilegeMockMvc.perform(get("/api/privileges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrivilege() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        int databaseSizeBeforeUpdate = privilegeRepository.findAll().size();

        // Update the privilege
        Privilege updatedPrivilege = privilegeRepository.findById(privilege.getId()).get();
        // Disconnect from session so that the updates on updatedPrivilege are not directly saved in db
        em.detach(updatedPrivilege);

        restPrivilegeMockMvc.perform(put("/api/privileges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrivilege)))
            .andExpect(status().isOk());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeUpdate);
        Privilege testPrivilege = privilegeList.get(privilegeList.size() - 1);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).save(testPrivilege);
    }

    @Test
    @Transactional
    public void updateNonExistingPrivilege() throws Exception {
        int databaseSizeBeforeUpdate = privilegeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc.perform(put("/api/privileges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(0)).save(privilege);
    }

    @Test
    @Transactional
    public void deletePrivilege() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        int databaseSizeBeforeDelete = privilegeRepository.findAll().size();

        // Delete the privilege
        restPrivilegeMockMvc.perform(delete("/api/privileges/{id}", privilege.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).deleteById(privilege.getId());
    }

    @Test
    @Transactional
    public void searchPrivilege() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);
        when(mockPrivilegeSearchRepository.search(queryStringQuery("id:" + privilege.getId())))
            .thenReturn(Collections.singletonList(privilege));

        // Search the privilege
        restPrivilegeMockMvc.perform(get("/api/_search/privileges?query=id:" + privilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privilege.getId().intValue())));
    }
}
