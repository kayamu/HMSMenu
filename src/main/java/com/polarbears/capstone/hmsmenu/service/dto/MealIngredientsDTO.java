package com.polarbears.capstone.hmsmenu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.MealIngredients} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealIngredientsDTO implements Serializable {

    private Long id;

    private String name;

    private String amount;

    private String unit;

    private LocalDate createdDate;

    private NutriensDTO nutriens;

    private IngredientsDTO ingradients;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public NutriensDTO getNutriens() {
        return nutriens;
    }

    public void setNutriens(NutriensDTO nutriens) {
        this.nutriens = nutriens;
    }

    public IngredientsDTO getIngradients() {
        return ingradients;
    }

    public void setIngradients(IngredientsDTO ingradients) {
        this.ingradients = ingradients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealIngredientsDTO)) {
            return false;
        }

        MealIngredientsDTO mealIngredientsDTO = (MealIngredientsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mealIngredientsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealIngredientsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount='" + getAmount() + "'" +
            ", unit='" + getUnit() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", nutriens=" + getNutriens() +
            ", ingradients=" + getIngradients() +
            "}";
    }
}
