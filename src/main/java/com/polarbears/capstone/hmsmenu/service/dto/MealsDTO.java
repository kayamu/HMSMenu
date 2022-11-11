package com.polarbears.capstone.hmsmenu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.Meals} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealsDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createdDate;

    private Set<ImagesUrlDTO> imagesUrls = new HashSet<>();

    private Set<MealIngredientsDTO> mealIngredients = new HashSet<>();

    private NutriensDTO nutriens;

    private RecipiesDTO recipies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ImagesUrlDTO> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrlDTO> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Set<MealIngredientsDTO> getMealIngredients() {
        return mealIngredients;
    }

    public void setMealIngredients(Set<MealIngredientsDTO> mealIngredients) {
        this.mealIngredients = mealIngredients;
    }

    public NutriensDTO getNutriens() {
        return nutriens;
    }

    public void setNutriens(NutriensDTO nutriens) {
        this.nutriens = nutriens;
    }

    public RecipiesDTO getRecipies() {
        return recipies;
    }

    public void setRecipies(RecipiesDTO recipies) {
        this.recipies = recipies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealsDTO)) {
            return false;
        }

        MealsDTO mealsDTO = (MealsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mealsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", imagesUrls=" + getImagesUrls() +
            ", mealIngredients=" + getMealIngredients() +
            ", nutriens=" + getNutriens() +
            ", recipies=" + getRecipies() +
            "}";
    }
}
