package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.repository.MealIngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.MealIngredientsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealIngredientsMapper;
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
 * Service for executing complex queries for {@link MealIngredients} entities in the database.
 * The main input is a {@link MealIngredientsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MealIngredientsDTO} or a {@link Page} of {@link MealIngredientsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MealIngredientsQueryService extends QueryService<MealIngredients> {

    private final Logger log = LoggerFactory.getLogger(MealIngredientsQueryService.class);

    private final MealIngredientsRepository mealIngredientsRepository;

    private final MealIngredientsMapper mealIngredientsMapper;

    public MealIngredientsQueryService(MealIngredientsRepository mealIngredientsRepository, MealIngredientsMapper mealIngredientsMapper) {
        this.mealIngredientsRepository = mealIngredientsRepository;
        this.mealIngredientsMapper = mealIngredientsMapper;
    }

    /**
     * Return a {@link List} of {@link MealIngredientsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MealIngredientsDTO> findByCriteria(MealIngredientsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MealIngredients> specification = createSpecification(criteria);
        return mealIngredientsMapper.toDto(mealIngredientsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MealIngredientsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MealIngredientsDTO> findByCriteria(MealIngredientsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MealIngredients> specification = createSpecification(criteria);
        return mealIngredientsRepository.findAll(specification, page).map(mealIngredientsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MealIngredientsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MealIngredients> specification = createSpecification(criteria);
        return mealIngredientsRepository.count(specification);
    }

    /**
     * Function to convert {@link MealIngredientsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MealIngredients> createSpecification(MealIngredientsCriteria criteria) {
        Specification<MealIngredients> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MealIngredients_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MealIngredients_.name));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAmount(), MealIngredients_.amount));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), MealIngredients_.unit));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MealIngredients_.createdDate));
            }
            if (criteria.getNutriensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNutriensId(),
                            root -> root.join(MealIngredients_.nutriens, JoinType.LEFT).get(Nutriens_.id)
                        )
                    );
            }
            if (criteria.getIngradientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngradientsId(),
                            root -> root.join(MealIngredients_.ingradients, JoinType.LEFT).get(Ingredients_.id)
                        )
                    );
            }
            if (criteria.getMealsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMealsId(), root -> root.join(MealIngredients_.meals, JoinType.LEFT).get(Meals_.id))
                    );
            }
        }
        return specification;
    }
}
