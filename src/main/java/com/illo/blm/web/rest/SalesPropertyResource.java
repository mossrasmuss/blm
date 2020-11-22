package com.illo.blm.web.rest;

import com.illo.blm.domain.SalesProperty;
import com.illo.blm.repository.SalesPropertyRepository;
import com.illo.blm.repository.search.SalesPropertySearchRepository;
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
 * REST controller for managing {@link com.illo.blm.domain.SalesProperty}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SalesPropertyResource {

    private final Logger log = LoggerFactory.getLogger(SalesPropertyResource.class);

    private static final String ENTITY_NAME = "salesProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesPropertyRepository salesPropertyRepository;

    private final SalesPropertySearchRepository salesPropertySearchRepository;

    public SalesPropertyResource(SalesPropertyRepository salesPropertyRepository, SalesPropertySearchRepository salesPropertySearchRepository) {
        this.salesPropertyRepository = salesPropertyRepository;
        this.salesPropertySearchRepository = salesPropertySearchRepository;
    }

    /**
     * {@code POST  /sales-properties} : Create a new salesProperty.
     *
     * @param salesProperty the salesProperty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesProperty, or with status {@code 400 (Bad Request)} if the salesProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales-properties")
    public ResponseEntity<SalesProperty> createSalesProperty(@RequestBody SalesProperty salesProperty) throws URISyntaxException {
        log.debug("REST request to save SalesProperty : {}", salesProperty);
        if (salesProperty.getId() != null) {
            throw new BadRequestAlertException("A new salesProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesProperty result = salesPropertyRepository.save(salesProperty);
        salesPropertySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sales-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-properties} : Updates an existing salesProperty.
     *
     * @param salesProperty the salesProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesProperty,
     * or with status {@code 400 (Bad Request)} if the salesProperty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales-properties")
    public ResponseEntity<SalesProperty> updateSalesProperty(@RequestBody SalesProperty salesProperty) throws URISyntaxException {
        log.debug("REST request to update SalesProperty : {}", salesProperty);
        if (salesProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalesProperty result = salesPropertyRepository.save(salesProperty);
        salesPropertySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesProperty.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sales-properties} : get all the salesProperties.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesProperties in body.
     */
    @GetMapping("/sales-properties")
    public List<SalesProperty> getAllSalesProperties(@RequestParam(required = false) String filter) {
        if ("property-is-null".equals(filter)) {
            log.debug("REST request to get all SalesPropertys where property is null");
            return StreamSupport
                .stream(salesPropertyRepository.findAll().spliterator(), false)
                .filter(salesProperty -> salesProperty.getProperty() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all SalesProperties");
        return salesPropertyRepository.findAll();
    }

    /**
     * {@code GET  /sales-properties/:id} : get the "id" salesProperty.
     *
     * @param id the id of the salesProperty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesProperty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales-properties/{id}")
    public ResponseEntity<SalesProperty> getSalesProperty(@PathVariable Long id) {
        log.debug("REST request to get SalesProperty : {}", id);
        Optional<SalesProperty> salesProperty = salesPropertyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salesProperty);
    }

    /**
     * {@code DELETE  /sales-properties/:id} : delete the "id" salesProperty.
     *
     * @param id the id of the salesProperty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales-properties/{id}")
    public ResponseEntity<Void> deleteSalesProperty(@PathVariable Long id) {
        log.debug("REST request to delete SalesProperty : {}", id);
        salesPropertyRepository.deleteById(id);
        salesPropertySearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sales-properties?query=:query} : search for the salesProperty corresponding
     * to the query.
     *
     * @param query the query of the salesProperty search.
     * @return the result of the search.
     */
    @GetMapping("/_search/sales-properties")
    public List<SalesProperty> searchSalesProperties(@RequestParam String query) {
        log.debug("REST request to search SalesProperties for query {}", query);
        return StreamSupport
            .stream(salesPropertySearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
