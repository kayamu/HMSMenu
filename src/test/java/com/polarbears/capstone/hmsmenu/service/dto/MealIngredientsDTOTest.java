package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealIngredientsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealIngredientsDTO.class);
        MealIngredientsDTO mealIngredientsDTO1 = new MealIngredientsDTO();
        mealIngredientsDTO1.setId(1L);
        MealIngredientsDTO mealIngredientsDTO2 = new MealIngredientsDTO();
        assertThat(mealIngredientsDTO1).isNotEqualTo(mealIngredientsDTO2);
        mealIngredientsDTO2.setId(mealIngredientsDTO1.getId());
        assertThat(mealIngredientsDTO1).isEqualTo(mealIngredientsDTO2);
        mealIngredientsDTO2.setId(2L);
        assertThat(mealIngredientsDTO1).isNotEqualTo(mealIngredientsDTO2);
        mealIngredientsDTO1.setId(null);
        assertThat(mealIngredientsDTO1).isNotEqualTo(mealIngredientsDTO2);
    }
}
