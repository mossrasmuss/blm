package com.illo.blm.repository.search;

import com.illo.blm.domain.Business;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Business} entity.
 */
public interface BusinessSearchRepository extends ElasticsearchRepository<Business, Long> {
}
