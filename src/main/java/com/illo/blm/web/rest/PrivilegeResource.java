package com.illo.blm.web.rest;

import com.illo.blm.domain.Privilege;
import com.illo.blm.repository.PrivilegeRepository;
import com.illo.blm.repository.search.PrivilegeSearchRepository;
import com.illo.blm.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.illo.blm.domain.Privilege}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrivilegeResource {

    private final Logger log = LoggerFactory.getLogger(PrivilegeResource.class);

    private static final String ENTITY_NAME = "privilege";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrivilegeRepository privilegeRepository;

    private final PrivilegeSearchRepository privilegeSearchRepository;

    public PrivilegeResource(PrivilegeRepository privilegeRepository, PrivilegeSearchRepository privilegeSearchRepository) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeSearchRepository = privilegeSearchRepository;
    }

    /**
     * {@code POST  /privileges} : Create a new privilege.
     *
     * @param privilege the privilege to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new privilege, or with status {@code 400 (Bad Request)} if the privilege has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/privileges")
    public ResponseEntity<Privilege> createPrivilege(@RequestBody Privilege privilege) throws URISyntaxException {
        log.debug("REST request to save Privilege : {}", privilege);
        if (privilege.getId() != null) {
            throw new BadRequestAlertException("A new privilege cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Privilege result = privilegeRepository.save(privilege);
        privilegeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/privileges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /privileges} : Updates an existing privilege.
     *
     * @param privilege the privilege to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privilege,
     * or with status {@code 400 (Bad Request)} if the privilege is not valid,
     * or with status {@code 500 (Internal Server Error)} if the privilege couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/privileges")
    public ResponseEntity<Privilege> updatePrivilege(@RequestBody Privilege privilege) throws URISyntaxException {
        log.debug("REST request to update Privilege : {}", privilege);
        if (privilege.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Privilege result = privilegeRepository.save(privilege);
        privilegeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privilege.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /privileges} : get all the privileges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of privileges in body.
     */
    @GetMapping("/privileges")
    public List<Privilege> getAllPrivileges() {
        log.debug("REST request to get all Privileges");
        return privilegeRepository.findAll();
    }

    /**
     * {@code GET  /privileges/:id} : get the "id" privilege.
     *
     * @param id the id of the privilege to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the privilege, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/privileges/{id}")
    public ResponseEntity<Privilege> getPrivilege(@PathVariable Long id) {
        log.debug("REST request to get Privilege : {}", id);
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(privilege);
    }

    /**
     * {@code DELETE  /privileges/:id} : delete the "id" privilege.
     *
     * @param id the id of the privilege to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/privileges/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable Long id) {
        log.debug("REST request to delete Privilege : {}", id);
        privilegeRepository.deleteById(id);
        privilegeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/privileges?query=:query} : search for the privilege corresponding
     * to the query.
     *
     * @param query the query of the privilege search.
     * @return the result of the search.
     */
    @GetMapping("/_search/privileges")
    public List<Privilege> searchPrivileges(@RequestParam String query) {
        log.debug("REST request to search Privileges for query {}", query);
        return StreamSupport
            .stream(privilegeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
