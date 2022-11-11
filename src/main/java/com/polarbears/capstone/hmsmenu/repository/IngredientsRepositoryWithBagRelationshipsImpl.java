package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Ingredients;
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
public class IngredientsRepositoryWithBagRelationshipsImpl implements IngredientsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ingredients> fetchBagRelationships(Optional<Ingredients> ingredients) {
        return ingredients.map(this::fetchImagesUrls);
    }

    @Override
    public Page<Ingredients> fetchBagRelationships(Page<Ingredients> ingredients) {
        return new PageImpl<>(fetchBagRelationships(ingredients.getContent()), ingredients.getPageable(), ingredients.getTotalElements());
    }

    @Override
    public List<Ingredients> fetchBagRelationships(List<Ingredients> ingredients) {
        return Optional.of(ingredients).map(this::fetchImagesUrls).orElse(Collections.emptyList());
    }

    Ingredients fetchImagesUrls(Ingredients result) {
        return entityManager
            .createQuery(
                "select ingredients from Ingredients ingredients left join fetch ingredients.imagesUrls where ingredients is :ingredients",
                Ingredients.class
            )
            .setParameter("ingredients", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Ingredients> fetchImagesUrls(List<Ingredients> ingredients) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ingredients.size()).forEach(index -> order.put(ingredients.get(index).getId(), index));
        List<Ingredients> result = entityManager
            .createQuery(
                "select distinct ingredients from Ingredients ingredients left join fetch ingredients.imagesUrls where ingredients in :ingredients",
                Ingredients.class
            )
            .setParameter("ingredients", ingredients)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
