package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Meals;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Meals entity.
 *
 * When extending this class, extend MealsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MealsRepository extends MealsRepositoryWithBagRelationships, JpaRepository<Meals, Long>, JpaSpecificationExecutor<Meals> {
    default Optional<Meals> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Meals> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Meals> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct meals from Meals meals left join fetch meals.nutriens left join fetch meals.recipies",
        countQuery = "select count(distinct meals) from Meals meals"
    )
    Page<Meals> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct meals from Meals meals left join fetch meals.nutriens left join fetch meals.recipies")
    List<Meals> findAllWithToOneRelationships();

    @Query("select meals from Meals meals left join fetch meals.nutriens left join fetch meals.recipies where meals.id =:id")
    Optional<Meals> findOneWithToOneRelationships(@Param("id") Long id);
}
