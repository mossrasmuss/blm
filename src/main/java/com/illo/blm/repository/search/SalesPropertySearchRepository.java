package com.illo.blm.repository.search;

import com.illo.blm.domain.SalesProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link SalesProperty} entity.
 */
public interface SalesPropertySearchRepository extends ElasticsearchRepository<SalesProperty, Long> {
}
