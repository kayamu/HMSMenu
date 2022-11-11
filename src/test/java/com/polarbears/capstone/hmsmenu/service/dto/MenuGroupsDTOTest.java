package com.polarbears.capstone.hmsmenu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuGroupsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuGroupsDTO.class);
        MenuGroupsDTO menuGroupsDTO1 = new MenuGroupsDTO();
        menuGroupsDTO1.setId(1L);
        MenuGroupsDTO menuGroupsDTO2 = new MenuGroupsDTO();
        assertThat(menuGroupsDTO1).isNotEqualTo(menuGroupsDTO2);
        menuGroupsDTO2.setId(menuGroupsDTO1.getId());
        assertThat(menuGroupsDTO1).isEqualTo(menuGroupsDTO2);
        menuGroupsDTO2.setId(2L);
        assertThat(menuGroupsDTO1).isNotEqualTo(menuGroupsDTO2);
        menuGroupsDTO1.setId(null);
        assertThat(menuGroupsDTO1).isNotEqualTo(menuGroupsDTO2);
    }
}
