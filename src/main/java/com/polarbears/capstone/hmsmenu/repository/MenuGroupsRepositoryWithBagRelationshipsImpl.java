package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MenuGroupsRepositoryWithBagRelationshipsImpl implements MenuGroupsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MenuGroups> fetchBagRelationships(Optional<MenuGroups> menuGroups) {
        return menuGroups.map(this::fetchIngradients).map(this::fetchMenus).map(this::fetchImagesUrls);
    }

    @Override
    public Page<MenuGroups> fetchBagRelationships(Page<MenuGroups> menuGroups) {
        return new PageImpl<>(fetchBagRelationships(menuGroups.getContent()), menuGroups.getPageable(), menuGroups.getTotalElements());
    }

    @Override
    public List<MenuGroups> fetchBagRelationships(List<MenuGroups> menuGroups) {
        return Optional
            .of(menuGroups)
            .map(this::fetchIngradients)
            .map(this::fetchMenus)
            .map(this::fetchImagesUrls)
            .orElse(Collections.emptyList());
    }

    MenuGroups fetchIngradients(MenuGroups result) {
        return entityManager
            .createQuery(
                "select menuGroups from MenuGroups menuGroups left join fetch menuGroups.ingradients where menuGroups is :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<MenuGroups> fetchIngradients(List<MenuGroups> menuGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menuGroups.size()).forEach(index -> order.put(menuGroups.get(index).getId(), index));
        List<MenuGroups> result = entityManager
            .createQuery(
                "select distinct menuGroups from MenuGroups menuGroups left join fetch menuGroups.ingradients where menuGroups in :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", menuGroups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    MenuGroups fetchMenus(MenuGroups result) {
        return entityManager
            .createQuery(
                "select menuGroups from MenuGroups menuGroups left join fetch menuGroups.menus where menuGroups is :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<MenuGroups> fetchMenus(List<MenuGroups> menuGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menuGroups.size()).forEach(index -> order.put(menuGroups.get(index).getId(), index));
        List<MenuGroups> result = entityManager
            .createQuery(
                "select distinct menuGroups from MenuGroups menuGroups left join fetch menuGroups.menus where menuGroups in :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", menuGroups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    MenuGroups fetchImagesUrls(MenuGroups result) {
        return entityManager
            .createQuery(
                "select menuGroups from MenuGroups menuGroups left join fetch menuGroups.imagesUrls where menuGroups is :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<MenuGroups> fetchImagesUrls(List<MenuGroups> menuGroups) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menuGroups.size()).forEach(index -> order.put(menuGroups.get(index).getId(), index));
        List<MenuGroups> result = entityManager
            .createQuery(
                "select distinct menuGroups from MenuGroups menuGroups left join fetch menuGroups.imagesUrls where menuGroups in :menuGroups",
                MenuGroups.class
            )
            .setParameter("menuGroups", menuGroups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
