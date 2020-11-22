package com.illo.blm.web.rest;

import com.illo.blm.domain.PropertyPricing;
import com.illo.blm.repository.PropertyPricingRepository;
import com.illo.blm.repository.search.PropertyPricingSearchRepository;
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
 * REST controller for managing {@link com.illo.blm.domain.PropertyPricing}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PropertyPricingResource {

    private final Logger log = LoggerFactory.getLogger(PropertyPricingResource.class);

    private static final String ENTITY_NAME = "propertyPricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyPricingRepository propertyPricingRepository;

    private final PropertyPricingSearchRepository propertyPricingSearchRepository;

    public PropertyPricingResource(PropertyPricingRepository propertyPricingRepository, PropertyPricingSearchRepository propertyPricingSearchRepository) {
        this.propertyPricingRepository = propertyPricingRepository;
        this.propertyPricingSearchRepository = propertyPricingSearchRepository;
    }

    /**
     * {@code POST  /property-pricings} : Create a new propertyPricing.
     *
     * @param propertyPricing the propertyPricing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyPricing, or with status {@code 400 (Bad Request)} if the propertyPricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/property-pricings")
    public ResponseEntity<PropertyPricing> createPropertyPricing(@RequestBody PropertyPricing propertyPricing) throws URISyntaxException {
        log.debug("REST request to save PropertyPricing : {}", propertyPricing);
        if (propertyPricing.getId() != null) {
            throw new BadRequestAlertException("A new propertyPricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyPricing result = propertyPricingRepository.save(propertyPricing);
        propertyPricingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/property-pricings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /property-pricings} : Updates an existing propertyPricing.
     *
     * @param propertyPricing the propertyPricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyPricing,
     * or with status {@code 400 (Bad Request)} if the propertyPricing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyPricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/property-pricings")
    public ResponseEntity<PropertyPricing> updatePropertyPricing(@RequestBody PropertyPricing propertyPricing) throws URISyntaxException {
        log.debug("REST request to update PropertyPricing : {}", propertyPricing);
        if (propertyPricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PropertyPricing result = propertyPricingRepository.save(propertyPricing);
        propertyPricingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyPricing.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /property-pricings} : get all the propertyPricings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyPricings in body.
     */
    @GetMapping("/property-pricings")
    public List<PropertyPricing> getAllPropertyPricings() {
        log.debug("REST request to get all PropertyPricings");
        return propertyPricingRepository.findAll();
    }

    /**
     * {@code GET  /property-pricings/:id} : get the "id" propertyPricing.
     *
     * @param id the id of the propertyPricing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyPricing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/property-pricings/{id}")
    public ResponseEntity<PropertyPricing> getPropertyPricing(@PathVariable Long id) {
        log.debug("REST request to get PropertyPricing : {}", id);
        Optional<PropertyPricing> propertyPricing = propertyPricingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(propertyPricing);
    }

    /**
     * {@code DELETE  /property-pricings/:id} : delete the "id" propertyPricing.
     *
     * @param id the id of the propertyPricing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/property-pricings/{id}")
    public ResponseEntity<Void> deletePropertyPricing(@PathVariable Long id) {
        log.debug("REST request to delete PropertyPricing : {}", id);
        propertyPricingRepository.deleteById(id);
        propertyPricingSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/property-pricings?query=:query} : search for the propertyPricing corresponding
     * to the query.
     *
     * @param query the query of the propertyPricing search.
     * @return the result of the search.
     */
    @GetMapping("/_search/property-pricings")
    public List<PropertyPricing> searchPropertyPricings(@RequestParam String query) {
        log.debug("REST request to search PropertyPricings for query {}", query);
        return StreamSupport
            .stream(propertyPricingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
