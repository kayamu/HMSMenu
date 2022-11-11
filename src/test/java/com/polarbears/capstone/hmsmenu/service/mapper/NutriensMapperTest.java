package com.polarbears.capstone.hmsmenu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NutriensMapperTest {

    private NutriensMapper nutriensMapper;

    @BeforeEach
    public void setUp() {
        nutriensMapper = new NutriensMapperImpl();
    }
}
