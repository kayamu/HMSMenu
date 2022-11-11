package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuGroups entity.
 *
 * When extending this class, extend MenuGroupsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MenuGroupsRepository
    extends MenuGroupsRepositoryWithBagRelationships, JpaRepository<MenuGroups, Long>, JpaSpecificationExecutor<MenuGroups> {
    default Optional<MenuGroups> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<MenuGroups> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<MenuGroups> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct menuGroups from MenuGroups menuGroups left join fetch menuGroups.nutriens",
        countQuery = "select count(distinct menuGroups) from MenuGroups menuGroups"
    )
    Page<MenuGroups> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct menuGroups from MenuGroups menuGroups left join fetch menuGroups.nutriens")
    List<MenuGroups> findAllWithToOneRelationships();

    @Query("select menuGroups from MenuGroups menuGroups left join fetch menuGroups.nutriens where menuGroups.id =:id")
    Optional<MenuGroups> findOneWithToOneRelationships(@Param("id") Long id);
}
