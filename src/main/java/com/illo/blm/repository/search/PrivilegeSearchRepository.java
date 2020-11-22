package com.illo.blm.repository.search;

import com.illo.blm.domain.Privilege;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Privilege} entity.
 */
public interface PrivilegeSearchRepository extends ElasticsearchRepository<Privilege, Long> {
}
