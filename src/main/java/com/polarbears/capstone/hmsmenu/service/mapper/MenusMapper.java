package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Menus} and its DTO {@link MenusDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenusMapper extends EntityMapper<MenusDTO, Menus> {
    @Mapping(target = "imagesUrls", source = "imagesUrls", qualifiedByName = "imagesUrlNameSet")
    @Mapping(target = "meals", source = "meals", qualifiedByName = "mealsNameSet")
    @Mapping(target = "nutriens", source = "nutriens", qualifiedByName = "nutriensName")
    MenusDTO toDto(Menus s);

    @Mapping(target = "removeImagesUrls", ignore = true)
    @Mapping(target = "removeMeals", ignore = true)
    Menus toEntity(MenusDTO menusDTO);

    @Named("imagesUrlName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ImagesUrlDTO toDtoImagesUrlName(ImagesUrl imagesUrl);

    @Named("imagesUrlNameSet")
    default Set<ImagesUrlDTO> toDtoImagesUrlNameSet(Set<ImagesUrl> imagesUrl) {
        return imagesUrl.stream().map(this::toDtoImagesUrlName).collect(Collectors.toSet());
    }

    @Named("mealsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MealsDTO toDtoMealsName(Meals meals);

    @Named("mealsNameSet")
    default Set<MealsDTO> toDtoMealsNameSet(Set<Meals> meals) {
        return meals.stream().map(this::toDtoMealsName).collect(Collectors.toSet());
    }

    @Named("nutriensName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NutriensDTO toDtoNutriensName(Nutriens nutriens);
}
