package com.illo.blm.repository.search;

import com.illo.blm.domain.UserAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link UserAccount} entity.
 */
public interface UserAccountSearchRepository extends ElasticsearchRepository<UserAccount, Long> {
}
