package com.illo.blm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.illo.blm.web.rest.TestUtil;

public class PropertyPricingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyPricing.class);
        PropertyPricing propertyPricing1 = new PropertyPricing();
        propertyPricing1.setId(1L);
        PropertyPricing propertyPricing2 = new PropertyPricing();
        propertyPricing2.setId(propertyPricing1.getId());
        assertThat(propertyPricing1).isEqualTo(propertyPricing2);
        propertyPricing2.setId(2L);
        assertThat(propertyPricing1).isNotEqualTo(propertyPricing2);
        propertyPricing1.setId(null);
        assertThat(propertyPricing1).isNotEqualTo(propertyPricing2);
    }
}
