package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuGroupsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuGroups.class);
        MenuGroups menuGroups1 = new MenuGroups();
        menuGroups1.setId(1L);
        MenuGroups menuGroups2 = new MenuGroups();
        menuGroups2.setId(menuGroups1.getId());
        assertThat(menuGroups1).isEqualTo(menuGroups2);
        menuGroups2.setId(2L);
        assertThat(menuGroups1).isNotEqualTo(menuGroups2);
        menuGroups1.setId(null);
        assertThat(menuGroups1).isNotEqualTo(menuGroups2);
    }
}
