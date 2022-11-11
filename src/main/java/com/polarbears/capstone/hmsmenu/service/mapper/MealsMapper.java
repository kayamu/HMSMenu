package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Meals} and its DTO {@link MealsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MealsMapper extends EntityMapper<MealsDTO, Meals> {
    @Mapping(target = "imagesUrls", source = "imagesUrls", qualifiedByName = "imagesUrlNameSet")
    @Mapping(target = "mealIngredients", source = "mealIngredients", qualifiedByName = "mealIngredientsNameSet")
    @Mapping(target = "nutriens", source = "nutriens", qualifiedByName = "nutriensName")
    @Mapping(target = "recipies", source = "recipies", qualifiedByName = "recipiesName")
    MealsDTO toDto(Meals s);

    @Mapping(target = "removeImagesUrls", ignore = true)
    @Mapping(target = "removeMealIngredients", ignore = true)
    Meals toEntity(MealsDTO mealsDTO);

    @Named("imagesUrlName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ImagesUrlDTO toDtoImagesUrlName(ImagesUrl imagesUrl);

    @Named("imagesUrlNameSet")
    default Set<ImagesUrlDTO> toDtoImagesUrlNameSet(Set<ImagesUrl> imagesUrl) {
        return imagesUrl.stream().map(this::toDtoImagesUrlName).collect(Collectors.toSet());
    }

    @Named("mealIngredientsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MealIngredientsDTO toDtoMealIngredientsName(MealIngredients mealIngredients);

    @Named("mealIngredientsNameSet")
    default Set<MealIngredientsDTO> toDtoMealIngredientsNameSet(Set<MealIngredients> mealIngredients) {
        return mealIngredients.stream().map(this::toDtoMealIngredientsName).collect(Collectors.toSet());
    }

    @Named("nutriensName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NutriensDTO toDtoNutriensName(Nutriens nutriens);

    @Named("recipiesName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RecipiesDTO toDtoRecipiesName(Recipies recipies);
}
