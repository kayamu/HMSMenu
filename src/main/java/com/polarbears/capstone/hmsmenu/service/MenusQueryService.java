package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.*; // for static metamodels
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.repository.MenusRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.MenusCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenusMapper;
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
 * Service for executing complex queries for {@link Menus} entities in the database.
 * The main input is a {@link MenusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenusDTO} or a {@link Page} of {@link MenusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenusQueryService extends QueryService<Menus> {

    private final Logger log = LoggerFactory.getLogger(MenusQueryService.class);

    private final MenusRepository menusRepository;

    private final MenusMapper menusMapper;

    public MenusQueryService(MenusRepository menusRepository, MenusMapper menusMapper) {
        this.menusRepository = menusRepository;
        this.menusMapper = menusMapper;
    }

    /**
     * Return a {@link List} of {@link MenusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenusDTO> findByCriteria(MenusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Menus> specification = createSpecification(criteria);
        return menusMapper.toDto(menusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenusDTO> findByCriteria(MenusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Menus> specification = createSpecification(criteria);
        return menusRepository.findAll(specification, page).map(menusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Menus> specification = createSpecification(criteria);
        return menusRepository.count(specification);
    }

    /**
     * Function to convert {@link MenusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Menus> createSpecification(MenusCriteria criteria) {
        Specification<Menus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Menus_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Menus_.name));
            }
            if (criteria.getMenuDay() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuDay(), Menus_.menuDay));
            }
            if (criteria.getMenuTime() != null) {
                specification = specification.and(buildSpecification(criteria.getMenuTime(), Menus_.menuTime));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContactId(), Menus_.contactId));
            }
            if (criteria.getCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCost(), Menus_.cost));
            }
            if (criteria.getSalesPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalesPrice(), Menus_.salesPrice));
            }
            if (criteria.getExplanation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExplanation(), Menus_.explanation));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Menus_.createdDate));
            }
            if (criteria.getImagesUrlsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getImagesUrlsId(),
                            root -> root.join(Menus_.imagesUrls, JoinType.LEFT).get(ImagesUrl_.id)
                        )
                    );
            }
            if (criteria.getMealsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMealsId(), root -> root.join(Menus_.meals, JoinType.LEFT).get(Meals_.id))
                    );
            }
            if (criteria.getNutriensId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNutriensId(), root -> root.join(Menus_.nutriens, JoinType.LEFT).get(Nutriens_.id))
                    );
            }
            if (criteria.getMenuGroupsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuGroupsId(),
                            root -> root.join(Menus_.menuGroups, JoinType.LEFT).get(MenuGroups_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
