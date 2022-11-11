package com.polarbears.capstone.hmsmenu.service.mapper;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImagesUrl} and its DTO {@link ImagesUrlDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagesUrlMapper extends EntityMapper<ImagesUrlDTO, ImagesUrl> {}
