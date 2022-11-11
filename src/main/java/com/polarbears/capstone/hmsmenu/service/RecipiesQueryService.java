package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.repository.RecipiesRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.RecipiesCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.RecipiesMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Recipies} entities in the database.
 * The main input is a {@link RecipiesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RecipiesDTO} or a {@link Page} of {@link RecipiesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipiesQueryService extends QueryService<Recipies> {

    private final Logger log = LoggerFactory.getLogger(RecipiesQueryService.class);

    private final RecipiesRepository recipiesRepository;

    private final RecipiesMapper recipiesMapper;

    public RecipiesQueryService(RecipiesRepository recipiesRepository, RecipiesMapper recipiesMapper) {
        this.recipiesRepository = recipiesRepository;
        this.recipiesMapper = recipiesMapper;
    }

    /**
     * Return a {@link List} of {@link RecipiesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RecipiesDTO> findByCriteria(RecipiesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Recipies> specification = createSpecification(criteria);
        return recipiesMapper.toDto(recipiesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RecipiesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecipiesDTO> findByCriteria(RecipiesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recipies> specification = createSpecification(criteria);
        return recipiesRepository.findAll(specification, page).map(recipiesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecipiesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Recipies> specification = createSpecification(criteria);
        return recipiesRepository.count(specification);
    }

    /**
     * Function to convert {@link RecipiesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Recipies> createSpecification(RecipiesCriteria criteria) {
        Specification<Recipies> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Recipies_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Recipies_.name));
            }
            if (criteria.getRecipe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecipe(), Recipies_.recipe));
            }
            if (criteria.getExplanation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplanation(), Recipies_.explanation));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Recipies_.createdDate));
            }
            if (criteria.getMealId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMealId(), root -> root.join(Recipies_.meals, JoinType.LEFT).get(Meals_.id))
                    );
            }
            if (criteria.getImagesUrlsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getImagesUrlsId(),
                            root -> root.join(Recipies_.imagesUrls, JoinType.LEFT).get(ImagesUrl_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
