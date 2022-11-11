package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.repository.ImagesUrlRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.ImagesUrlCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.ImagesUrlMapper;
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
 * Service for executing complex queries for {@link ImagesUrl} entities in the database.
 * The main input is a {@link ImagesUrlCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImagesUrlDTO} or a {@link Page} of {@link ImagesUrlDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagesUrlQueryService extends QueryService<ImagesUrl> {

    private final Logger log = LoggerFactory.getLogger(ImagesUrlQueryService.class);

    private final ImagesUrlRepository imagesUrlRepository;

    private final ImagesUrlMapper imagesUrlMapper;

    public ImagesUrlQueryService(ImagesUrlRepository imagesUrlRepository, ImagesUrlMapper imagesUrlMapper) {
        this.imagesUrlRepository = imagesUrlRepository;
        this.imagesUrlMapper = imagesUrlMapper;
    }

    /**
     * Return a {@link List} of {@link ImagesUrlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImagesUrlDTO> findByCriteria(ImagesUrlCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ImagesUrl> specification = createSpecification(criteria);
        return imagesUrlMapper.toDto(imagesUrlRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ImagesUrlDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagesUrlDTO> findByCriteria(ImagesUrlCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ImagesUrl> specification = createSpecification(criteria);
        return imagesUrlRepository.findAll(specification, page).map(imagesUrlMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagesUrlCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ImagesUrl> specification = createSpecification(criteria);
        return imagesUrlRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagesUrlCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ImagesUrl> createSpecification(ImagesUrlCriteria criteria) {
        Specification<ImagesUrl> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ImagesUrl_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ImagesUrl_.name));
            }
            if (criteria.getUrlAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlAddress(), ImagesUrl_.urlAddress));
            }
            if (criteria.getExplanation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplanation(), ImagesUrl_.explanation));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), ImagesUrl_.type));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ImagesUrl_.createdDate));
            }
            if (criteria.getMenuGroupsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuGroupsId(),
                            root -> root.join(ImagesUrl_.menuGroups, JoinType.LEFT).get(MenuGroups_.id)
                        )
                    );
            }
            if (criteria.getMenusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenusId(), root -> root.join(ImagesUrl_.menus, JoinType.LEFT).get(Menus_.id))
                    );
            }
            if (criteria.getMealsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMealsId(), root -> root.join(ImagesUrl_.meals, JoinType.LEFT).get(Meals_.id))
                    );
            }
            if (criteria.getIngredientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngredientsId(),
                            root -> root.join(ImagesUrl_.ingredients, JoinType.LEFT).get(Ingredients_.id)
                        )
                    );
            }
            if (criteria.getRecipeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRecipeId(), root -> root.join(ImagesUrl_.recipes, JoinType.LEFT).get(Recipies_.id))
                    );
            }
        }
        return specification;
    }
}
