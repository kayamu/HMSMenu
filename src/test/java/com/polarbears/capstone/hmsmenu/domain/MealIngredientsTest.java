package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MealIngredientsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MealIngredients.class);
        MealIngredients mealIngredients1 = new MealIngredients();
        mealIngredients1.setId(1L);
        MealIngredients mealIngredients2 = new MealIngredients();
        mealIngredients2.setId(mealIngredients1.getId());
        assertThat(mealIngredients1).isEqualTo(mealIngredients2);
        mealIngredients2.setId(2L);
        assertThat(mealIngredients1).isNotEqualTo(mealIngredients2);
        mealIngredients1.setId(null);
        assertThat(mealIngredients1).isNotEqualTo(mealIngredients2);
    }
}
