package com.illo.blm.repository.search;

import com.illo.blm.domain.PropertyPricing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PropertyPricing} entity.
 */
public interface PropertyPricingSearchRepository extends ElasticsearchRepository<PropertyPricing, Long> {
}
