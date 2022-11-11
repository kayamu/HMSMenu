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
 * A Nutriens.
 */
@Entity
@Table(name = "nutriens")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nutriens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "carb")
    private Double carb;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "kcal")
    private Double kcal;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "nutriens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingradients", "menus", "imagesUrls", "nutriens" }, allowSetters = true)
    private Set<MenuGroups> menuGroups = new HashSet<>();

    @OneToMany(mappedBy = "nutriens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "meals", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Menus> menus = new HashSet<>();

    @OneToMany(mappedBy = "nutriens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nutriens", "ingradients", "meals" }, allowSetters = true)
    private Set<MealIngredients> mealIngredients = new HashSet<>();

    @OneToMany(mappedBy = "nutriens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "mealIngredients", "nutriens", "recipies", "menus" }, allowSetters = true)
    private Set<Meals> meals = new HashSet<>();

    @OneToMany(mappedBy = "nutriens")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mealIngredients", "imagesUrls", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Ingredients> ingredients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nutriens id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Nutriens name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProtein() {
        return this.protein;
    }

    public Nutriens protein(Double protein) {
        this.setProtein(protein);
        return this;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarb() {
        return this.carb;
    }

    public Nutriens carb(Double carb) {
        this.setCarb(carb);
        return this;
    }

    public void setCarb(Double carb) {
        this.carb = carb;
    }

    public Double getFat() {
        return this.fat;
    }

    public Nutriens fat(Double fat) {
        this.setFat(fat);
        return this;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getKcal() {
        return this.kcal;
    }

    public Nutriens kcal(Double kcal) {
        this.setKcal(kcal);
        return this;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Nutriens createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<MenuGroups> getMenuGroups() {
        return this.menuGroups;
    }

    public void setMenuGroups(Set<MenuGroups> menuGroups) {
        if (this.menuGroups != null) {
            this.menuGroups.forEach(i -> i.setNutriens(null));
        }
        if (menuGroups != null) {
            menuGroups.forEach(i -> i.setNutriens(this));
        }
        this.menuGroups = menuGroups;
    }

    public Nutriens menuGroups(Set<MenuGroups> menuGroups) {
        this.setMenuGroups(menuGroups);
        return this;
    }

    public Nutriens addMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.add(menuGroups);
        menuGroups.setNutriens(this);
        return this;
    }

    public Nutriens removeMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.remove(menuGroups);
        menuGroups.setNutriens(null);
        return this;
    }

    public Set<Menus> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menus> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.setNutriens(null));
        }
        if (menus != null) {
            menus.forEach(i -> i.setNutriens(this));
        }
        this.menus = menus;
    }

    public Nutriens menus(Set<Menus> menus) {
        this.setMenus(menus);
        return this;
    }

    public Nutriens addMenus(Menus menus) {
        this.menus.add(menus);
        menus.setNutriens(this);
        return this;
    }

    public Nutriens removeMenus(Menus menus) {
        this.menus.remove(menus);
        menus.setNutriens(null);
        return this;
    }

    public Set<MealIngredients> getMealIngredients() {
        return this.mealIngredients;
    }

    public void setMealIngredients(Set<MealIngredients> mealIngredients) {
        if (this.mealIngredients != null) {
            this.mealIngredients.forEach(i -> i.setNutriens(null));
        }
        if (mealIngredients != null) {
            mealIngredients.forEach(i -> i.setNutriens(this));
        }
        this.mealIngredients = mealIngredients;
    }

    public Nutriens mealIngredients(Set<MealIngredients> mealIngredients) {
        this.setMealIngredients(mealIngredients);
        return this;
    }

    public Nutriens addMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.add(mealIngredients);
        mealIngredients.setNutriens(this);
        return this;
    }

    public Nutriens removeMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.remove(mealIngredients);
        mealIngredients.setNutriens(null);
        return this;
    }

    public Set<Meals> getMeals() {
        return this.meals;
    }

    public void setMeals(Set<Meals> meals) {
        if (this.meals != null) {
            this.meals.forEach(i -> i.setNutriens(null));
        }
        if (meals != null) {
            meals.forEach(i -> i.setNutriens(this));
        }
        this.meals = meals;
    }

    public Nutriens meals(Set<Meals> meals) {
        this.setMeals(meals);
        return this;
    }

    public Nutriens addMeals(Meals meals) {
        this.meals.add(meals);
        meals.setNutriens(this);
        return this;
    }

    public Nutriens removeMeals(Meals meals) {
        this.meals.remove(meals);
        meals.setNutriens(null);
        return this;
    }

    public Set<Ingredients> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Set<Ingredients> ingredients) {
        if (this.ingredients != null) {
            this.ingredients.forEach(i -> i.setNutriens(null));
        }
        if (ingredients != null) {
            ingredients.forEach(i -> i.setNutriens(this));
        }
        this.ingredients = ingredients;
    }

    public Nutriens ingredients(Set<Ingredients> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public Nutriens addIngredients(Ingredients ingredients) {
        this.ingredients.add(ingredients);
        ingredients.setNutriens(this);
        return this;
    }

    public Nutriens removeIngredients(Ingredients ingredients) {
        this.ingredients.remove(ingredients);
        ingredients.setNutriens(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nutriens)) {
            return false;
        }
        return id != null && id.equals(((Nutriens) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nutriens{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", protein=" + getProtein() +
            ", carb=" + getCarb() +
            ", fat=" + getFat() +
            ", kcal=" + getKcal() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
