package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IngredientsRepositoryWithBagRelationships {
    Optional<Ingredients> fetchBagRelationships(Optional<Ingredients> ingredients);

    List<Ingredients> fetchBagRelationships(List<Ingredients> ingredients);

    Page<Ingredients> fetchBagRelationships(Page<Ingredients> ingredients);
}
