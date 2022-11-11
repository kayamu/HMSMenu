package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NutriensTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nutriens.class);
        Nutriens nutriens1 = new Nutriens();
        nutriens1.setId(1L);
        Nutriens nutriens2 = new Nutriens();
        nutriens2.setId(nutriens1.getId());
        assertThat(nutriens1).isEqualTo(nutriens2);
        nutriens2.setId(2L);
        assertThat(nutriens1).isNotEqualTo(nutriens2);
        nutriens1.setId(null);
        assertThat(nutriens1).isNotEqualTo(nutriens2);
    }
}
