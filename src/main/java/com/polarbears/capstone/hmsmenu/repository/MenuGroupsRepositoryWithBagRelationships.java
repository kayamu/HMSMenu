package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MenuGroupsRepositoryWithBagRelationships {
    Optional<MenuGroups> fetchBagRelationships(Optional<MenuGroups> menuGroups);

    List<MenuGroups> fetchBagRelationships(List<MenuGroups> menuGroups);

    Page<MenuGroups> fetchBagRelationships(Page<MenuGroups> menuGroups);
}
