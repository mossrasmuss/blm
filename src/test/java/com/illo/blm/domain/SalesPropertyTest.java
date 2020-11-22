package com.illo.blm.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.illo.blm.web.rest.TestUtil;

public class SalesPropertyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesProperty.class);
        SalesProperty salesProperty1 = new SalesProperty();
        salesProperty1.setId(1L);
        SalesProperty salesProperty2 = new SalesProperty();
        salesProperty2.setId(salesProperty1.getId());
        assertThat(salesProperty1).isEqualTo(salesProperty2);
        salesProperty2.setId(2L);
        assertThat(salesProperty1).isNotEqualTo(salesProperty2);
        salesProperty1.setId(null);
        assertThat(salesProperty1).isNotEqualTo(salesProperty2);
    }
}
