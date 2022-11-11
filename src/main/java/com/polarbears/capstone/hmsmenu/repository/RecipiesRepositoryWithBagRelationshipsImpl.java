package com.polarbears.capstone.hmsmenu.repository;

import com.polarbears.capstone.hmsmenu.domain.Recipies;
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
public class RecipiesRepositoryWithBagRelationshipsImpl implements RecipiesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Recipies> fetchBagRelationships(Optional<Recipies> recipies) {
        return recipies.map(this::fetchImagesUrls);
    }

    @Override
    public Page<Recipies> fetchBagRelationships(Page<Recipies> recipies) {
        return new PageImpl<>(fetchBagRelationships(recipies.getContent()), recipies.getPageable(), recipies.getTotalElements());
    }

    @Override
    public List<Recipies> fetchBagRelationships(List<Recipies> recipies) {
        return Optional.of(recipies).map(this::fetchImagesUrls).orElse(Collections.emptyList());
    }

    Recipies fetchImagesUrls(Recipies result) {
        return entityManager
            .createQuery(
                "select recipies from Recipies recipies left join fetch recipies.imagesUrls where recipies is :recipies",
                Recipies.class
            )
            .setParameter("recipies", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Recipies> fetchImagesUrls(List<Recipies> recipies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, recipies.size()).forEach(index -> order.put(recipies.get(index).getId(), index));
        List<Recipies> result = entityManager
            .createQuery(
                "select distinct recipies from Recipies recipies left join fetch recipies.imagesUrls where recipies in :recipies",
                Recipies.class
            )
            .setParameter("recipies", recipies)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
