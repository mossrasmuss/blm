package com.illo.blm.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PropertyPricingSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PropertyPricingSearchRepositoryMockConfiguration {

    @MockBean
    private PropertyPricingSearchRepository mockPropertyPricingSearchRepository;

}
