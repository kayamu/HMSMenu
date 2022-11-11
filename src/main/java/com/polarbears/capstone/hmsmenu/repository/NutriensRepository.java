package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Nutriens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NutriensRepository extends JpaRepository<Nutriens, Long>, JpaSpecificationExecutor<Nutriens> {}
