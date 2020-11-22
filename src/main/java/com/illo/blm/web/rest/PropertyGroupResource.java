package com.illo.blm.web.rest;

import com.illo.blm.domain.PropertyGroup;
import com.illo.blm.repository.PropertyGroupRepository;
import com.illo.blm.repository.search.PropertyGroupSearchRepository;
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
 * REST controller for managing {@link com.illo.blm.domain.PropertyGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PropertyGroupResource {

    private final Logger log = LoggerFactory.getLogger(PropertyGroupResource.class);

    private static final String ENTITY_NAME = "propertyGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyGroupRepository propertyGroupRepository;

    private final PropertyGroupSearchRepository propertyGroupSearchRepository;

    public PropertyGroupResource(PropertyGroupRepository propertyGroupRepository, PropertyGroupSearchRepository propertyGroupSearchRepository) {
        this.propertyGroupRepository = propertyGroupRepository;
        this.propertyGroupSearchRepository = propertyGroupSearchRepository;
    }

    /**
     * {@code POST  /property-groups} : Create a new propertyGroup.
     *
     * @param propertyGroup the propertyGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyGroup, or with status {@code 400 (Bad Request)} if the propertyGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/property-groups")
    public ResponseEntity<PropertyGroup> createPropertyGroup(@RequestBody PropertyGroup propertyGroup) throws URISyntaxException {
        log.debug("REST request to save PropertyGroup : {}", propertyGroup);
        if (propertyGroup.getId() != null) {
            throw new BadRequestAlertException("A new propertyGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyGroup result = propertyGroupRepository.save(propertyGroup);
        propertyGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/property-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /property-groups} : Updates an existing propertyGroup.
     *
     * @param propertyGroup the propertyGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyGroup,
     * or with status {@code 400 (Bad Request)} if the propertyGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/property-groups")
    public ResponseEntity<PropertyGroup> updatePropertyGroup(@RequestBody PropertyGroup propertyGroup) throws URISyntaxException {
        log.debug("REST request to update PropertyGroup : {}", propertyGroup);
        if (propertyGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PropertyGroup result = propertyGroupRepository.save(propertyGroup);
        propertyGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /property-groups} : get all the propertyGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyGroups in body.
     */
    @GetMapping("/property-groups")
    public List<PropertyGroup> getAllPropertyGroups() {
        log.debug("REST request to get all PropertyGroups");
        return propertyGroupRepository.findAll();
    }

    /**
     * {@code GET  /property-groups/:id} : get the "id" propertyGroup.
     *
     * @param id the id of the propertyGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/property-groups/{id}")
    public ResponseEntity<PropertyGroup> getPropertyGroup(@PathVariable Long id) {
        log.debug("REST request to get PropertyGroup : {}", id);
        Optional<PropertyGroup> propertyGroup = propertyGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(propertyGroup);
    }

    /**
     * {@code DELETE  /property-groups/:id} : delete the "id" propertyGroup.
     *
     * @param id the id of the propertyGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/property-groups/{id}")
    public ResponseEntity<Void> deletePropertyGroup(@PathVariable Long id) {
        log.debug("REST request to delete PropertyGroup : {}", id);
        propertyGroupRepository.deleteById(id);
        propertyGroupSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/property-groups?query=:query} : search for the propertyGroup corresponding
     * to the query.
     *
     * @param query the query of the propertyGroup search.
     * @return the result of the search.
     */
    @GetMapping("/_search/property-groups")
    public List<PropertyGroup> searchPropertyGroups(@RequestParam String query) {
        log.debug("REST request to search PropertyGroups for query {}", query);
        return StreamSupport
            .stream(propertyGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
