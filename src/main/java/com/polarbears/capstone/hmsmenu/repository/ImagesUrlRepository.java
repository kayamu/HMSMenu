package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImagesUrl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImagesUrlRepository extends JpaRepository<ImagesUrl, Long>, JpaSpecificationExecutor<ImagesUrl> {}
