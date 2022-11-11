package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Menus;
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
public class MenusRepositoryWithBagRelationshipsImpl implements MenusRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Menus> fetchBagRelationships(Optional<Menus> menus) {
        return menus.map(this::fetchImagesUrls).map(this::fetchMeals);
    }

    @Override
    public Page<Menus> fetchBagRelationships(Page<Menus> menus) {
        return new PageImpl<>(fetchBagRelationships(menus.getContent()), menus.getPageable(), menus.getTotalElements());
    }

    @Override
    public List<Menus> fetchBagRelationships(List<Menus> menus) {
        return Optional.of(menus).map(this::fetchImagesUrls).map(this::fetchMeals).orElse(Collections.emptyList());
    }

    Menus fetchImagesUrls(Menus result) {
        return entityManager
            .createQuery("select menus from Menus menus left join fetch menus.imagesUrls where menus is :menus", Menus.class)
            .setParameter("menus", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Menus> fetchImagesUrls(List<Menus> menus) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menus.size()).forEach(index -> order.put(menus.get(index).getId(), index));
        List<Menus> result = entityManager
            .createQuery("select distinct menus from Menus menus left join fetch menus.imagesUrls where menus in :menus", Menus.class)
            .setParameter("menus", menus)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Menus fetchMeals(Menus result) {
        return entityManager
            .createQuery("select menus from Menus menus left join fetch menus.meals where menus is :menus", Menus.class)
            .setParameter("menus", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Menus> fetchMeals(List<Menus> menus) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menus.size()).forEach(index -> order.put(menus.get(index).getId(), index));
        List<Menus> result = entityManager
            .createQuery("select distinct menus from Menus menus left join fetch menus.meals where menus in :menus", Menus.class)
            .setParameter("menus", menus)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
