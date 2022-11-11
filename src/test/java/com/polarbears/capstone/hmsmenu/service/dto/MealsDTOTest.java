package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealsDTO.class);
        MealsDTO mealsDTO1 = new MealsDTO();
        mealsDTO1.setId(1L);
        MealsDTO mealsDTO2 = new MealsDTO();
        assertThat(mealsDTO1).isNotEqualTo(mealsDTO2);
        mealsDTO2.setId(mealsDTO1.getId());
        assertThat(mealsDTO1).isEqualTo(mealsDTO2);
        mealsDTO2.setId(2L);
        assertThat(mealsDTO1).isNotEqualTo(mealsDTO2);
        mealsDTO1.setId(null);
        assertThat(mealsDTO1).isNotEqualTo(mealsDTO2);
    }
}
