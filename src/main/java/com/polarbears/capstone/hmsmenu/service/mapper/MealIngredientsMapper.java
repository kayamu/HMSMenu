package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.service.dto.IngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MealIngredients} and its DTO {@link MealIngredientsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MealIngredientsMapper extends EntityMapper<MealIngredientsDTO, MealIngredients> {
    @Mapping(target = "nutriens", source = "nutriens", qualifiedByName = "nutriensName")
    @Mapping(target = "ingradients", source = "ingradients", qualifiedByName = "ingredientsName")
    MealIngredientsDTO toDto(MealIngredients s);

    @Named("nutriensName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NutriensDTO toDtoNutriensName(Nutriens nutriens);

    @Named("ingredientsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IngredientsDTO toDtoIngredientsName(Ingredients ingredients);
}
