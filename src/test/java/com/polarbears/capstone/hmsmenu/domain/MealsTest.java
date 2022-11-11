package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meals.class);
        Meals meals1 = new Meals();
        meals1.setId(1L);
        Meals meals2 = new Meals();
        meals2.setId(meals1.getId());
        assertThat(meals1).isEqualTo(meals2);
        meals2.setId(2L);
        assertThat(meals1).isNotEqualTo(meals2);
        meals1.setId(null);
        assertThat(meals1).isNotEqualTo(meals2);
    }
}
