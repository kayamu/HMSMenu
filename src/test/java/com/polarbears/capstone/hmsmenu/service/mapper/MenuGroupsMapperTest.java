package com.polarbears.capstone.hmsmenu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuGroupsMapperTest {

    private MenuGroupsMapper menuGroupsMapper;

    @BeforeEach
    public void setUp() {
        menuGroupsMapper = new MenuGroupsMapperImpl();
    }
}
