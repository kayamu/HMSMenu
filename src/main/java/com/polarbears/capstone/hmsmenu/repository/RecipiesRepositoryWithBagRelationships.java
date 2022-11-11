package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Recipies;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RecipiesRepositoryWithBagRelationships {
    Optional<Recipies> fetchBagRelationships(Optional<Recipies> recipies);

    List<Recipies> fetchBagRelationships(List<Recipies> recipies);

    Page<Recipies> fetchBagRelationships(Page<Recipies> recipies);
}
