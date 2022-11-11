package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecipiesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecipiesDTO.class);
        RecipiesDTO recipiesDTO1 = new RecipiesDTO();
        recipiesDTO1.setId(1L);
        RecipiesDTO recipiesDTO2 = new RecipiesDTO();
        assertThat(recipiesDTO1).isNotEqualTo(recipiesDTO2);
        recipiesDTO2.setId(recipiesDTO1.getId());
        assertThat(recipiesDTO1).isEqualTo(recipiesDTO2);
        recipiesDTO2.setId(2L);
        assertThat(recipiesDTO1).isNotEqualTo(recipiesDTO2);
        recipiesDTO1.setId(null);
        assertThat(recipiesDTO1).isNotEqualTo(recipiesDTO2);
    }
}
