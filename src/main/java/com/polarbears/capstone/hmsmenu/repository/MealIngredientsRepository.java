package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MealIngredients entity.
 */
@Repository
public interface MealIngredientsRepository extends JpaRepository<MealIngredients, Long>, JpaSpecificationExecutor<MealIngredients> {
    default Optional<MealIngredients> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MealIngredients> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MealIngredients> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct mealIngredients from MealIngredients mealIngredients left join fetch mealIngredients.nutriens left join fetch mealIngredients.ingradients",
        countQuery = "select count(distinct mealIngredients) from MealIngredients mealIngredients"
    )
    Page<MealIngredients> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct mealIngredients from MealIngredients mealIngredients left join fetch mealIngredients.nutriens left join fetch mealIngredients.ingradients"
    )
    List<MealIngredients> findAllWithToOneRelationships();

    @Query(
        "select mealIngredients from MealIngredients mealIngredients left join fetch mealIngredients.nutriens left join fetch mealIngredients.ingradients where mealIngredients.id =:id"
    )
    Optional<MealIngredients> findOneWithToOneRelationships(@Param("id") Long id);
}
