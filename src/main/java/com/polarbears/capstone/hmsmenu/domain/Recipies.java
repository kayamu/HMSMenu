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
 * A Recipies.
 */
@Entity
@Table(name = "recipies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "recipe")
    private String recipe;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "recipies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "mealIngredients", "nutriens", "recipies", "menus" }, allowSetters = true)
    private Set<Meals> meals = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_recipies__images_urls",
        joinColumns = @JoinColumn(name = "recipies_id"),
        inverseJoinColumns = @JoinColumn(name = "images_urls_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "meals", "ingredients", "recipes" }, allowSetters = true)
    private Set<ImagesUrl> imagesUrls = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipies id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Recipies name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public Recipies recipe(String recipe) {
        this.setRecipe(recipe);
        return this;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public Recipies explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Recipies createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Meals> getMeals() {
        return this.meals;
    }

    public void setMeals(Set<Meals> meals) {
        if (this.meals != null) {
            this.meals.forEach(i -> i.setRecipies(null));
        }
        if (meals != null) {
            meals.forEach(i -> i.setRecipies(this));
        }
        this.meals = meals;
    }

    public Recipies meals(Set<Meals> meals) {
        this.setMeals(meals);
        return this;
    }

    public Recipies addMeal(Meals meals) {
        this.meals.add(meals);
        meals.setRecipies(this);
        return this;
    }

    public Recipies removeMeal(Meals meals) {
        this.meals.remove(meals);
        meals.setRecipies(null);
        return this;
    }

    public Set<ImagesUrl> getImagesUrls() {
        return this.imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrl> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Recipies imagesUrls(Set<ImagesUrl> imagesUrls) {
        this.setImagesUrls(imagesUrls);
        return this;
    }

    public Recipies addImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.add(imagesUrl);
        imagesUrl.getRecipes().add(this);
        return this;
    }

    public Recipies removeImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.remove(imagesUrl);
        imagesUrl.getRecipes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipies)) {
            return false;
        }
        return id != null && id.equals(((Recipies) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipies{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", recipe='" + getRecipe() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
