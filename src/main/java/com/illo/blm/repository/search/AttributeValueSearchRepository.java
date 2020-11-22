package com.illo.blm.repository.search;

import com.illo.blm.domain.AttributeValue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AttributeValue} entity.
 */
public interface AttributeValueSearchRepository extends ElasticsearchRepository<AttributeValue, Long> {
}
