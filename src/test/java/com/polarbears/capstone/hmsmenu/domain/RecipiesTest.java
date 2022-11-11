package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipies.class);
        Recipies recipies1 = new Recipies();
        recipies1.setId(1L);
        Recipies recipies2 = new Recipies();
        recipies2.setId(recipies1.getId());
        assertThat(recipies1).isEqualTo(recipies2);
        recipies2.setId(2L);
        assertThat(recipies1).isNotEqualTo(recipies2);
        recipies1.setId(null);
        assertThat(recipies1).isNotEqualTo(recipies2);
    }
}
