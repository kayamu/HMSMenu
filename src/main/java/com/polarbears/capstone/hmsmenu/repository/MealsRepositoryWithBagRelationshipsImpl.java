package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Meals;
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
public class MealsRepositoryWithBagRelationshipsImpl implements MealsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Meals> fetchBagRelationships(Optional<Meals> meals) {
        return meals.map(this::fetchImagesUrls).map(this::fetchMealIngredients);
    }

    @Override
    public Page<Meals> fetchBagRelationships(Page<Meals> meals) {
        return new PageImpl<>(fetchBagRelationships(meals.getContent()), meals.getPageable(), meals.getTotalElements());
    }

    @Override
    public List<Meals> fetchBagRelationships(List<Meals> meals) {
        return Optional.of(meals).map(this::fetchImagesUrls).map(this::fetchMealIngredients).orElse(Collections.emptyList());
    }

    Meals fetchImagesUrls(Meals result) {
        return entityManager
            .createQuery("select meals from Meals meals left join fetch meals.imagesUrls where meals is :meals", Meals.class)
            .setParameter("meals", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Meals> fetchImagesUrls(List<Meals> meals) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, meals.size()).forEach(index -> order.put(meals.get(index).getId(), index));
        List<Meals> result = entityManager
            .createQuery("select distinct meals from Meals meals left join fetch meals.imagesUrls where meals in :meals", Meals.class)
            .setParameter("meals", meals)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Meals fetchMealIngredients(Meals result) {
        return entityManager
            .createQuery("select meals from Meals meals left join fetch meals.mealIngredients where meals is :meals", Meals.class)
            .setParameter("meals", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Meals> fetchMealIngredients(List<Meals> meals) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, meals.size()).forEach(index -> order.put(meals.get(index).getId(), index));
        List<Meals> result = entityManager
            .createQuery("select distinct meals from Meals meals left join fetch meals.mealIngredients where meals in :meals", Meals.class)
            .setParameter("meals", meals)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
