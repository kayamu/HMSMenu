package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.repository.MenuGroupsRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.MenuGroupsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenuGroupsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenuGroupsMapper;
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
 * Service for executing complex queries for {@link MenuGroups} entities in the database.
 * The main input is a {@link MenuGroupsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuGroupsDTO} or a {@link Page} of {@link MenuGroupsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuGroupsQueryService extends QueryService<MenuGroups> {

    private final Logger log = LoggerFactory.getLogger(MenuGroupsQueryService.class);

    private final MenuGroupsRepository menuGroupsRepository;

    private final MenuGroupsMapper menuGroupsMapper;

    public MenuGroupsQueryService(MenuGroupsRepository menuGroupsRepository, MenuGroupsMapper menuGroupsMapper) {
        this.menuGroupsRepository = menuGroupsRepository;
        this.menuGroupsMapper = menuGroupsMapper;
    }

    /**
     * Return a {@link List} of {@link MenuGroupsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuGroupsDTO> findByCriteria(MenuGroupsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuGroups> specification = createSpecification(criteria);
        return menuGroupsMapper.toDto(menuGroupsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenuGroupsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuGroupsDTO> findByCriteria(MenuGroupsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuGroups> specification = createSpecification(criteria);
        return menuGroupsRepository.findAll(specification, page).map(menuGroupsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuGroupsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuGroups> specification = createSpecification(criteria);
        return menuGroupsRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuGroupsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MenuGroups> createSpecification(MenuGroupsCriteria criteria) {
        Specification<MenuGroups> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MenuGroups_.id));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContactId(), MenuGroups_.contactId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MenuGroups_.name));
            }
            if (criteria.getCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCost(), MenuGroups_.cost));
            }
            if (criteria.getSalesPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalesPrice(), MenuGroups_.salesPrice));
            }
            if (criteria.getExplanation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplanation(), MenuGroups_.explanation));
            }
            if (criteria.getGoal() != null) {
                specification = specification.and(buildSpecification(criteria.getGoal(), MenuGroups_.goal));
            }
            if (criteria.getBodyType() != null) {
                specification = specification.and(buildSpecification(criteria.getBodyType(), MenuGroups_.bodyType));
            }
            if (criteria.getActivityLevelMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivityLevelMin(), MenuGroups_.activityLevelMin));
            }
            if (criteria.getActivityLevelMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivityLevelMax(), MenuGroups_.activityLevelMax));
            }
            if (criteria.getWeightMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightMin(), MenuGroups_.weightMin));
            }
            if (criteria.getWeightMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightMax(), MenuGroups_.weightMax));
            }
            if (criteria.getDailyKcalMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDailyKcalMin(), MenuGroups_.dailyKcalMin));
            }
            if (criteria.getDailyKcalMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDailyKcalMax(), MenuGroups_.dailyKcalMax));
            }
            if (criteria.getTargetWeightMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetWeightMin(), MenuGroups_.targetWeightMin));
            }
            if (criteria.getTargetWeightMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetWeightMax(), MenuGroups_.targetWeightMax));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildSpecification(criteria.getUnit(), MenuGroups_.unit));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MenuGroups_.createdDate));
            }
            if (criteria.getIngradientsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIngradientsId(),
                            root -> root.join(MenuGroups_.ingradients, JoinType.LEFT).get(Ingredients_.id)
                        )
                    );
            }
            if (criteria.getMenusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenusId(), root -> root.join(MenuGroups_.menus, JoinType.LEFT).get(Menus_.id))
                    );
            }
            if (criteria.getImagesUrlsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getImagesUrlsId(),
                            root -> root.join(MenuGroups_.imagesUrls, JoinType.LEFT).get(ImagesUrl_.id)
                        )
                    );
            }
            if (criteria.getNutriensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNutriensId(),
                            root -> root.join(MenuGroups_.nutriens, JoinType.LEFT).get(Nutriens_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
