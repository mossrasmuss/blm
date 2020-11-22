package com.illo.blm.repository.search;

import com.illo.blm.domain.PropertyGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PropertyGroup} entity.
 */
public interface PropertyGroupSearchRepository extends ElasticsearchRepository<PropertyGroup, Long> {
}
