package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Meals;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MealsRepositoryWithBagRelationships {
    Optional<Meals> fetchBagRelationships(Optional<Meals> meals);

    List<Meals> fetchBagRelationships(List<Meals> meals);

    Page<Meals> fetchBagRelationships(Page<Meals> meals);
}
