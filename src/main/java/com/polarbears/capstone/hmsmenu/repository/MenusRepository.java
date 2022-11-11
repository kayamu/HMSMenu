package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Menus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Menus entity.
 *
 * When extending this class, extend MenusRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MenusRepository extends MenusRepositoryWithBagRelationships, JpaRepository<Menus, Long>, JpaSpecificationExecutor<Menus> {
    default Optional<Menus> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Menus> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Menus> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct menus from Menus menus left join fetch menus.nutriens",
        countQuery = "select count(distinct menus) from Menus menus"
    )
    Page<Menus> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct menus from Menus menus left join fetch menus.nutriens")
    List<Menus> findAllWithToOneRelationships();

    @Query("select menus from Menus menus left join fetch menus.nutriens where menus.id =:id")
    Optional<Menus> findOneWithToOneRelationships(@Param("id") Long id);
}
