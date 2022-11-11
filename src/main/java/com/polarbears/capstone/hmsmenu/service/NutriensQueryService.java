package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.repository.NutriensRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.NutriensCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.NutriensMapper;
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
 * Service for executing complex queries for {@link Nutriens} entities in the database.
 * The main input is a {@link NutriensCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NutriensDTO} or a {@link Page} of {@link NutriensDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NutriensQueryService extends QueryService<Nutriens> {

    private final Logger log = LoggerFactory.getLogger(NutriensQueryService.class);

    private final NutriensRepository nutriensRepository;

    private final NutriensMapper nutriensMapper;

    public NutriensQueryService(NutriensRepository nutriensRepository, NutriensMapper nutriensMapper) {
        this.nutriensRepository = nutriensRepository;
        this.nutriensMapper = nutriensMapper;
    }

    /**
     * Return a {@link List} of {@link NutriensDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NutriensDTO> findByCriteria(NutriensCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Nutriens> specification = createSpecification(criteria);
        return nutriensMapper.toDto(nutriensRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NutriensDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NutriensDTO> findByCriteria(NutriensCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Nutriens> specification = createSpecification(criteria);
        return nutriensRepository.findAll(specification, page).map(nutriensMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NutriensCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Nutriens> specification = createSpecification(criteria);
        return nutriensRepository.count(specification);
    }

    /**
     * Function to convert {@link NutriensCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Nutriens> createSpecification(NutriensCriteria criteria) {
        Specification<Nutriens> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Nutriens_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Nutriens_.name));
            }
            if (criteria.getProtein() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProtein(), Nutriens_.protein));
            }
            if (criteria.getCarb() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCarb(), Nutriens_.carb));
            }
            if (criteria.getFat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFat(), Nutriens_.fat));
            }
            if (criteria.getKcal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKcal(), Nutriens_.kcal));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Nutriens_.createdDate));
            }
            if (criteria.getMenuGroupsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuGroupsId(),
                            root -> root.join(Nutriens_.menuGroups, JoinType.LEFT).get(MenuGroups_.id)
                        )
                    );
            }
            if (criteria.getMenusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenusId(), root -> root.join(Nutriens_.menus, JoinType.LEFT).get(Menus_.id))
                    );
            }
            if (criteria.getMealIngredientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMealIngredientsId(),
                            root -> root.join(Nutriens_.mealIngredients, JoinType.LEFT).get(MealIngredients_.id)
                        )
                    );
            }
            if (criteria.getMealsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMealsId(), root -> root.join(Nutriens_.meals, JoinType.LEFT).get(Meals_.id))
                    );
            }
            if (criteria.getIngredientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngredientsId(),
                            root -> root.join(Nutriens_.ingredients, JoinType.LEFT).get(Ingredients_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
