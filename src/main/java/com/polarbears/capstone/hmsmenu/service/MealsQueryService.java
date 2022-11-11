package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.repository.MealsRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.MealsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealsMapper;
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
 * Service for executing complex queries for {@link Meals} entities in the database.
 * The main input is a {@link MealsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MealsDTO} or a {@link Page} of {@link MealsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MealsQueryService extends QueryService<Meals> {

    private final Logger log = LoggerFactory.getLogger(MealsQueryService.class);

    private final MealsRepository mealsRepository;

    private final MealsMapper mealsMapper;

    public MealsQueryService(MealsRepository mealsRepository, MealsMapper mealsMapper) {
        this.mealsRepository = mealsRepository;
        this.mealsMapper = mealsMapper;
    }

    /**
     * Return a {@link List} of {@link MealsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MealsDTO> findByCriteria(MealsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Meals> specification = createSpecification(criteria);
        return mealsMapper.toDto(mealsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MealsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MealsDTO> findByCriteria(MealsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Meals> specification = createSpecification(criteria);
        return mealsRepository.findAll(specification, page).map(mealsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MealsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Meals> specification = createSpecification(criteria);
        return mealsRepository.count(specification);
    }

    /**
     * Function to convert {@link MealsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Meals> createSpecification(MealsCriteria criteria) {
        Specification<Meals> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Meals_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Meals_.name));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Meals_.createdDate));
            }
            if (criteria.getImagesUrlsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getImagesUrlsId(),
                            root -> root.join(Meals_.imagesUrls, JoinType.LEFT).get(ImagesUrl_.id)
                        )
                    );
            }
            if (criteria.getMealIngredientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMealIngredientsId(),
                            root -> root.join(Meals_.mealIngredients, JoinType.LEFT).get(MealIngredients_.id)
                        )
                    );
            }
            if (criteria.getNutriensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNutriensId(), root -> root.join(Meals_.nutriens, JoinType.LEFT).get(Nutriens_.id))
                    );
            }
            if (criteria.getRecipiesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRecipiesId(), root -> root.join(Meals_.recipies, JoinType.LEFT).get(Recipies_.id))
                    );
            }
            if (criteria.getMenusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenusId(), root -> root.join(Meals_.menus, JoinType.LEFT).get(Menus_.id))
                    );
            }
        }
        return specification;
    }
}
