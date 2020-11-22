package com.illo.blm.repository.search;

import com.illo.blm.domain.Attribute;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Attribute} entity.
 */
public interface AttributeSearchRepository extends ElasticsearchRepository<Attribute, Long> {
}
