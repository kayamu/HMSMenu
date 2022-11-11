package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NutriensDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NutriensDTO.class);
        NutriensDTO nutriensDTO1 = new NutriensDTO();
        nutriensDTO1.setId(1L);
        NutriensDTO nutriensDTO2 = new NutriensDTO();
        assertThat(nutriensDTO1).isNotEqualTo(nutriensDTO2);
        nutriensDTO2.setId(nutriensDTO1.getId());
        assertThat(nutriensDTO1).isEqualTo(nutriensDTO2);
        nutriensDTO2.setId(2L);
        assertThat(nutriensDTO1).isNotEqualTo(nutriensDTO2);
        nutriensDTO1.setId(null);
        assertThat(nutriensDTO1).isNotEqualTo(nutriensDTO2);
    }
}
