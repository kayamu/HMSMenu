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
 * A Meals.
 */
@Entity
@Table(name = "meals")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Meals implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_meals__images_urls",
        joinColumns = @JoinColumn(name = "meals_id"),
        inverseJoinColumns = @JoinColumn(name = "images_urls_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "meals", "ingredients", "recipes" }, allowSetters = true)
    private Set<ImagesUrl> imagesUrls = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_meals__meal_ingredients",
        joinColumns = @JoinColumn(name = "meals_id"),
        inverseJoinColumns = @JoinColumn(name = "meal_ingredients_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nutriens", "ingradients", "meals" }, allowSetters = true)
    private Set<MealIngredients> mealIngredients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "mealIngredients", "meals", "ingredients" }, allowSetters = true)
    private Nutriens nutriens;

    @ManyToOne
    @JsonIgnoreProperties(value = { "meals", "imagesUrls" }, allowSetters = true)
    private Recipies recipies;

    @ManyToMany(mappedBy = "meals")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "meals", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Menus> menus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Meals id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Meals name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Meals createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ImagesUrl> getImagesUrls() {
        return this.imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrl> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Meals imagesUrls(Set<ImagesUrl> imagesUrls) {
        this.setImagesUrls(imagesUrls);
        return this;
    }

    public Meals addImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.add(imagesUrl);
        imagesUrl.getMeals().add(this);
        return this;
    }

    public Meals removeImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.remove(imagesUrl);
        imagesUrl.getMeals().remove(this);
        return this;
    }

    public Set<MealIngredients> getMealIngredients() {
        return this.mealIngredients;
    }

    public void setMealIngredients(Set<MealIngredients> mealIngredients) {
        this.mealIngredients = mealIngredients;
    }

    public Meals mealIngredients(Set<MealIngredients> mealIngredients) {
        this.setMealIngredients(mealIngredients);
        return this;
    }

    public Meals addMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.add(mealIngredients);
        mealIngredients.getMeals().add(this);
        return this;
    }

    public Meals removeMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.remove(mealIngredients);
        mealIngredients.getMeals().remove(this);
        return this;
    }

    public Nutriens getNutriens() {
        return this.nutriens;
    }

    public void setNutriens(Nutriens nutriens) {
        this.nutriens = nutriens;
    }

    public Meals nutriens(Nutriens nutriens) {
        this.setNutriens(nutriens);
        return this;
    }

    public Recipies getRecipies() {
        return this.recipies;
    }

    public void setRecipies(Recipies recipies) {
        this.recipies = recipies;
    }

    public Meals recipies(Recipies recipies) {
        this.setRecipies(recipies);
        return this;
    }

    public Set<Menus> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menus> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.removeMeals(this));
        }
        if (menus != null) {
            menus.forEach(i -> i.addMeals(this));
        }
        this.menus = menus;
    }

    public Meals menus(Set<Menus> menus) {
        this.setMenus(menus);
        return this;
    }

    public Meals addMenus(Menus menus) {
        this.menus.add(menus);
        menus.getMeals().add(this);
        return this;
    }

    public Meals removeMenus(Menus menus) {
        this.menus.remove(menus);
        menus.getMeals().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Meals)) {
            return false;
        }
        return id != null && id.equals(((Meals) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Meals{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
