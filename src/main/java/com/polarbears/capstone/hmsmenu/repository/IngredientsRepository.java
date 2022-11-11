package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ingredients entity.
 *
 * When extending this class, extend IngredientsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface IngredientsRepository
    extends IngredientsRepositoryWithBagRelationships, JpaRepository<Ingredients, Long>, JpaSpecificationExecutor<Ingredients> {
    default Optional<Ingredients> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Ingredients> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Ingredients> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct ingredients from Ingredients ingredients left join fetch ingredients.nutriens",
        countQuery = "select count(distinct ingredients) from Ingredients ingredients"
    )
    Page<Ingredients> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct ingredients from Ingredients ingredients left join fetch ingredients.nutriens")
    List<Ingredients> findAllWithToOneRelationships();

    @Query("select ingredients from Ingredients ingredients left join fetch ingredients.nutriens where ingredients.id =:id")
    Optional<Ingredients> findOneWithToOneRelationships(@Param("id") Long id);
}
