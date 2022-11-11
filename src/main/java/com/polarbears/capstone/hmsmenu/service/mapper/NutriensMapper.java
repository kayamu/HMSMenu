package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nutriens} and its DTO {@link NutriensDTO}.
 */
@Mapper(componentModel = "spring")
public interface NutriensMapper extends EntityMapper<NutriensDTO, Nutriens> {}
