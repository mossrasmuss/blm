package com.illo.blm.repository.search;

import com.illo.blm.domain.Media;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Media} entity.
 */
public interface MediaSearchRepository extends ElasticsearchRepository<Media, Long> {
}
