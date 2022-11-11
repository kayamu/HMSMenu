package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenusDTO.class);
        MenusDTO menusDTO1 = new MenusDTO();
        menusDTO1.setId(1L);
        MenusDTO menusDTO2 = new MenusDTO();
        assertThat(menusDTO1).isNotEqualTo(menusDTO2);
        menusDTO2.setId(menusDTO1.getId());
        assertThat(menusDTO1).isEqualTo(menusDTO2);
        menusDTO2.setId(2L);
        assertThat(menusDTO1).isNotEqualTo(menusDTO2);
        menusDTO1.setId(null);
        assertThat(menusDTO1).isNotEqualTo(menusDTO2);
    }
}
