package com.illo.blm.web.rest;

import com.illo.blm.domain.AttributeValue;
import com.illo.blm.repository.AttributeValueRepository;
import com.illo.blm.repository.search.AttributeValueSearchRepository;
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
 * REST controller for managing {@link com.illo.blm.domain.AttributeValue}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttributeValueResource {

    private final Logger log = LoggerFactory.getLogger(AttributeValueResource.class);

    private static final String ENTITY_NAME = "attributeValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueSearchRepository attributeValueSearchRepository;

    public AttributeValueResource(AttributeValueRepository attributeValueRepository, AttributeValueSearchRepository attributeValueSearchRepository) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeValueSearchRepository = attributeValueSearchRepository;
    }

    /**
     * {@code POST  /attribute-values} : Create a new attributeValue.
     *
     * @param attributeValue the attributeValue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeValue, or with status {@code 400 (Bad Request)} if the attributeValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribute-values")
    public ResponseEntity<AttributeValue> createAttributeValue(@RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to save AttributeValue : {}", attributeValue);
        if (attributeValue.getId() != null) {
            throw new BadRequestAlertException("A new attributeValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeValue result = attributeValueRepository.save(attributeValue);
        attributeValueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attribute-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attribute-values} : Updates an existing attributeValue.
     *
     * @param attributeValue the attributeValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeValue,
     * or with status {@code 400 (Bad Request)} if the attributeValue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribute-values")
    public ResponseEntity<AttributeValue> updateAttributeValue(@RequestBody AttributeValue attributeValue) throws URISyntaxException {
        log.debug("REST request to update AttributeValue : {}", attributeValue);
        if (attributeValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttributeValue result = attributeValueRepository.save(attributeValue);
        attributeValueSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributeValue.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attribute-values} : get all the attributeValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributeValues in body.
     */
    @GetMapping("/attribute-values")
    public List<AttributeValue> getAllAttributeValues() {
        log.debug("REST request to get all AttributeValues");
        return attributeValueRepository.findAll();
    }

    /**
     * {@code GET  /attribute-values/:id} : get the "id" attributeValue.
     *
     * @param id the id of the attributeValue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribute-values/{id}")
    public ResponseEntity<AttributeValue> getAttributeValue(@PathVariable Long id) {
        log.debug("REST request to get AttributeValue : {}", id);
        Optional<AttributeValue> attributeValue = attributeValueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributeValue);
    }

    /**
     * {@code DELETE  /attribute-values/:id} : delete the "id" attributeValue.
     *
     * @param id the id of the attributeValue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribute-values/{id}")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable Long id) {
        log.debug("REST request to delete AttributeValue : {}", id);
        attributeValueRepository.deleteById(id);
        attributeValueSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/attribute-values?query=:query} : search for the attributeValue corresponding
     * to the query.
     *
     * @param query the query of the attributeValue search.
     * @return the result of the search.
     */
    @GetMapping("/_search/attribute-values")
    public List<AttributeValue> searchAttributeValues(@RequestParam String query) {
        log.debug("REST request to search AttributeValues for query {}", query);
        return StreamSupport
            .stream(attributeValueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
