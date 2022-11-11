package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recipies} and its DTO {@link RecipiesDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipiesMapper extends EntityMapper<RecipiesDTO, Recipies> {
    @Mapping(target = "imagesUrls", source = "imagesUrls", qualifiedByName = "imagesUrlNameSet")
    RecipiesDTO toDto(Recipies s);

    @Mapping(target = "removeImagesUrls", ignore = true)
    Recipies toEntity(RecipiesDTO recipiesDTO);

    @Named("imagesUrlName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ImagesUrlDTO toDtoImagesUrlName(ImagesUrl imagesUrl);

    @Named("imagesUrlNameSet")
    default Set<ImagesUrlDTO> toDtoImagesUrlNameSet(Set<ImagesUrl> imagesUrl) {
        return imagesUrl.stream().map(this::toDtoImagesUrlName).collect(Collectors.toSet());
    }
}
