package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Menus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MenusRepositoryWithBagRelationships {
    Optional<Menus> fetchBagRelationships(Optional<Menus> menus);

    List<Menus> fetchBagRelationships(List<Menus> menus);

    Page<Menus> fetchBagRelationships(Page<Menus> menus);
}
