package com.illo.blm.web.rest;

import com.illo.blm.domain.Attribute;
import com.illo.blm.repository.AttributeRepository;
import com.illo.blm.repository.search.AttributeSearchRepository;
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
 * REST controller for managing {@link com.illo.blm.domain.Attribute}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttributeResource {

    private final Logger log = LoggerFactory.getLogger(AttributeResource.class);

    private static final String ENTITY_NAME = "attribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeRepository attributeRepository;

    private final AttributeSearchRepository attributeSearchRepository;

    public AttributeResource(AttributeRepository attributeRepository, AttributeSearchRepository attributeSearchRepository) {
        this.attributeRepository = attributeRepository;
        this.attributeSearchRepository = attributeSearchRepository;
    }

    /**
     * {@code POST  /attributes} : Create a new attribute.
     *
     * @param attribute the attribute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attribute, or with status {@code 400 (Bad Request)} if the attribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attributes")
    public ResponseEntity<Attribute> createAttribute(@RequestBody Attribute attribute) throws URISyntaxException {
        log.debug("REST request to save Attribute : {}", attribute);
        if (attribute.getId() != null) {
            throw new BadRequestAlertException("A new attribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attribute result = attributeRepository.save(attribute);
        attributeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attributes} : Updates an existing attribute.
     *
     * @param attribute the attribute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attribute,
     * or with status {@code 400 (Bad Request)} if the attribute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attribute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attributes")
    public ResponseEntity<Attribute> updateAttribute(@RequestBody Attribute attribute) throws URISyntaxException {
        log.debug("REST request to update Attribute : {}", attribute);
        if (attribute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Attribute result = attributeRepository.save(attribute);
        attributeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attribute.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attributes} : get all the attributes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributes in body.
     */
    @GetMapping("/attributes")
    public List<Attribute> getAllAttributes() {
        log.debug("REST request to get all Attributes");
        return attributeRepository.findAll();
    }

    /**
     * {@code GET  /attributes/:id} : get the "id" attribute.
     *
     * @param id the id of the attribute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attribute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attributes/{id}")
    public ResponseEntity<Attribute> getAttribute(@PathVariable Long id) {
        log.debug("REST request to get Attribute : {}", id);
        Optional<Attribute> attribute = attributeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attribute);
    }

    /**
     * {@code DELETE  /attributes/:id} : delete the "id" attribute.
     *
     * @param id the id of the attribute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attributes/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        log.debug("REST request to delete Attribute : {}", id);
        attributeRepository.deleteById(id);
        attributeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/attributes?query=:query} : search for the attribute corresponding
     * to the query.
     *
     * @param query the query of the attribute search.
     * @return the result of the search.
     */
    @GetMapping("/_search/attributes")
    public List<Attribute> searchAttributes(@RequestParam String query) {
        log.debug("REST request to search Attributes for query {}", query);
        return StreamSupport
            .stream(attributeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
