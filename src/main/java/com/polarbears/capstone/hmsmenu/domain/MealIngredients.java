package com.polarbears.capstone.hmsmenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MealIngredients.
 */
@Entity
@Table(name = "meal_ingredients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealIngredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private String amount;

    @Column(name = "unit")
    private String unit;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "mealIngredients", "meals", "ingredients" }, allowSetters = true)
    private Nutriens nutriens;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mealIngredients", "imagesUrls", "nutriens", "menuGroups" }, allowSetters = true)
    private Ingredients ingradients;

    @ManyToMany(mappedBy = "mealIngredients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "mealIngredients", "nutriens", "recipies", "menus" }, allowSetters = true)
    private Set<Meals> meals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MealIngredients id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MealIngredients name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return this.amount;
    }

    public MealIngredients amount(String amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return this.unit;
    }

    public MealIngredients unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public MealIngredients createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Nutriens getNutriens() {
        return this.nutriens;
    }

    public void setNutriens(Nutriens nutriens) {
        this.nutriens = nutriens;
    }

    public MealIngredients nutriens(Nutriens nutriens) {
        this.setNutriens(nutriens);
        return this;
    }

    public Ingredients getIngradients() {
        return this.ingradients;
    }

    public void setIngradients(Ingredients ingredients) {
        this.ingradients = ingredients;
    }

    public MealIngredients ingradients(Ingredients ingredients) {
        this.setIngradients(ingredients);
        return this;
    }

    public Set<Meals> getMeals() {
        return this.meals;
    }

    public void setMeals(Set<Meals> meals) {
        if (this.meals != null) {
            this.meals.forEach(i -> i.removeMealIngredients(this));
        }
        if (meals != null) {
            meals.forEach(i -> i.addMealIngredients(this));
        }
        this.meals = meals;
    }

    public MealIngredients meals(Set<Meals> meals) {
        this.setMeals(meals);
        return this;
    }

    public MealIngredients addMeals(Meals meals) {
        this.meals.add(meals);
        meals.getMealIngredients().add(this);
        return this;
    }

    public MealIngredients removeMeals(Meals meals) {
        this.meals.remove(meals);
        meals.getMealIngredients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MealIngredients)) {
            return false;
        }
        return id != null && id.equals(((MealIngredients) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealIngredients{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount='" + getAmount() + "'" +
            ", unit='" + getUnit() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
