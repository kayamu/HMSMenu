package com.polarbears.capstone.hmsmenu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsmenu.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menus.class);
        Menus menus1 = new Menus();
        menus1.setId(1L);
        Menus menus2 = new Menus();
        menus2.setId(menus1.getId());
        assertThat(menus1).isEqualTo(menus2);
        menus2.setId(2L);
        assertThat(menus1).isNotEqualTo(menus2);
        menus1.setId(null);
        assertThat(menus1).isNotEqualTo(menus2);
    }
}
