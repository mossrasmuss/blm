package com.illo.blm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.illo.blm.web.rest.TestUtil;

public class AttributeValueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValue.class);
        AttributeValue attributeValue1 = new AttributeValue();
        attributeValue1.setId(1L);
        AttributeValue attributeValue2 = new AttributeValue();
        attributeValue2.setId(attributeValue1.getId());
        assertThat(attributeValue1).isEqualTo(attributeValue2);
        attributeValue2.setId(2L);
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
        attributeValue1.setId(null);
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
    }
}
