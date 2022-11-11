package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.dto.IngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingredients} and its DTO {@link IngredientsDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngredientsMapper extends EntityMapper<IngredientsDTO, Ingredients> {
    @Mapping(target = "imagesUrls", source = "imagesUrls", qualifiedByName = "imagesUrlNameSet")
    @Mapping(target = "nutriens", source = "nutriens", qualifiedByName = "nutriensName")
    IngredientsDTO toDto(Ingredients s);

    @Mapping(target = "removeImagesUrls", ignore = true)
    Ingredients toEntity(IngredientsDTO ingredientsDTO);

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
