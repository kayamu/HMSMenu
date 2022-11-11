package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.dto.IngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MenuGroupsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuGroups} and its DTO {@link MenuGroupsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuGroupsMapper extends EntityMapper<MenuGroupsDTO, MenuGroups> {
    @Mapping(target = "ingradients", source = "ingradients", qualifiedByName = "ingredientsNameSet")
    @Mapping(target = "menus", source = "menus", qualifiedByName = "menusNameSet")
    @Mapping(target = "imagesUrls", source = "imagesUrls", qualifiedByName = "imagesUrlNameSet")
    @Mapping(target = "nutriens", source = "nutriens", qualifiedByName = "nutriensName")
    MenuGroupsDTO toDto(MenuGroups s);

    @Mapping(target = "removeIngradients", ignore = true)
    @Mapping(target = "removeMenus", ignore = true)
    @Mapping(target = "removeImagesUrls", ignore = true)
    MenuGroups toEntity(MenuGroupsDTO menuGroupsDTO);

    @Named("ingredientsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    IngredientsDTO toDtoIngredientsName(Ingredients ingredients);

    @Named("ingredientsNameSet")
    default Set<IngredientsDTO> toDtoIngredientsNameSet(Set<Ingredients> ingredients) {
        return ingredients.stream().map(this::toDtoIngredientsName).collect(Collectors.toSet());
    }

    @Named("menusName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MenusDTO toDtoMenusName(Menus menus);

    @Named("menusNameSet")
    default Set<MenusDTO> toDtoMenusNameSet(Set<Menus> menus) {
        return menus.stream().map(this::toDtoMenusName).collect(Collectors.toSet());
    }

    @Named("imagesUrlName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ImagesUrlDTO toDtoImagesUrlName(ImagesUrl imagesUrl);

    @Named("imagesUrlNameSet")
    default Set<ImagesUrlDTO> toDtoImagesUrlNameSet(Set<ImagesUrl> imagesUrl) {
        return imagesUrl.stream().map(this::toDtoImagesUrlName).collect(Collectors.toSet());
    }

    @Named("nutriensName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    NutriensDTO toDtoNutriensName(Nutriens nutriens);
}
