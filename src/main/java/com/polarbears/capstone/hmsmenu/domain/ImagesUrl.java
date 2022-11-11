package com.polarbears.capstone.hmsmenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmsmenu.domain.enumeration.IMAGETYPES;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImagesUrl.
 */
@Entity
@Table(name = "images_url")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url_address")
    private String urlAddress;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IMAGETYPES type;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany(mappedBy = "imagesUrls")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingradients", "menus", "imagesUrls", "nutriens" }, allowSetters = true)
    private Set<MenuGroups> menuGroups = new HashSet<>();

    @ManyToMany(mappedBy = "imagesUrls")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "meals", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Menus> menus = new HashSet<>();

    @ManyToMany(mappedBy = "imagesUrls")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "mealIngredients", "nutriens", "recipies", "menus" }, allowSetters = true)
    private Set<Meals> meals = new HashSet<>();

    @ManyToMany(mappedBy = "imagesUrls")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mealIngredients", "imagesUrls", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Ingredients> ingredients = new HashSet<>();

    @ManyToMany(mappedBy = "imagesUrls")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "meals", "imagesUrls" }, allowSetters = true)
    private Set<Recipies> recipes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImagesUrl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ImagesUrl name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAddress() {
        return this.urlAddress;
    }

    public ImagesUrl urlAddress(String urlAddress) {
        this.setUrlAddress(urlAddress);
        return this;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public ImagesUrl explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public IMAGETYPES getType() {
        return this.type;
    }

    public ImagesUrl type(IMAGETYPES type) {
        this.setType(type);
        return this;
    }

    public void setType(IMAGETYPES type) {
        this.type = type;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public ImagesUrl createdDate(LocalDate createdDate) {
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
            this.menuGroups.forEach(i -> i.removeImagesUrls(this));
        }
        if (menuGroups != null) {
            menuGroups.forEach(i -> i.addImagesUrls(this));
        }
        this.menuGroups = menuGroups;
    }

    public ImagesUrl menuGroups(Set<MenuGroups> menuGroups) {
        this.setMenuGroups(menuGroups);
        return this;
    }

    public ImagesUrl addMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.add(menuGroups);
        menuGroups.getImagesUrls().add(this);
        return this;
    }

    public ImagesUrl removeMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.remove(menuGroups);
        menuGroups.getImagesUrls().remove(this);
        return this;
    }

    public Set<Menus> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menus> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.removeImagesUrls(this));
        }
        if (menus != null) {
            menus.forEach(i -> i.addImagesUrls(this));
        }
        this.menus = menus;
    }

    public ImagesUrl menus(Set<Menus> menus) {
        this.setMenus(menus);
        return this;
    }

    public ImagesUrl addMenus(Menus menus) {
        this.menus.add(menus);
        menus.getImagesUrls().add(this);
        return this;
    }

    public ImagesUrl removeMenus(Menus menus) {
        this.menus.remove(menus);
        menus.getImagesUrls().remove(this);
        return this;
    }

    public Set<Meals> getMeals() {
        return this.meals;
    }

    public void setMeals(Set<Meals> meals) {
        if (this.meals != null) {
            this.meals.forEach(i -> i.removeImagesUrls(this));
        }
        if (meals != null) {
            meals.forEach(i -> i.addImagesUrls(this));
        }
        this.meals = meals;
    }

    public ImagesUrl meals(Set<Meals> meals) {
        this.setMeals(meals);
        return this;
    }

    public ImagesUrl addMeals(Meals meals) {
        this.meals.add(meals);
        meals.getImagesUrls().add(this);
        return this;
    }

    public ImagesUrl removeMeals(Meals meals) {
        this.meals.remove(meals);
        meals.getImagesUrls().remove(this);
        return this;
    }

    public Set<Ingredients> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Set<Ingredients> ingredients) {
        if (this.ingredients != null) {
            this.ingredients.forEach(i -> i.removeImagesUrls(this));
        }
        if (ingredients != null) {
            ingredients.forEach(i -> i.addImagesUrls(this));
        }
        this.ingredients = ingredients;
    }

    public ImagesUrl ingredients(Set<Ingredients> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public ImagesUrl addIngredients(Ingredients ingredients) {
        this.ingredients.add(ingredients);
        ingredients.getImagesUrls().add(this);
        return this;
    }

    public ImagesUrl removeIngredients(Ingredients ingredients) {
        this.ingredients.remove(ingredients);
        ingredients.getImagesUrls().remove(this);
        return this;
    }

    public Set<Recipies> getRecipes() {
        return this.recipes;
    }

    public void setRecipes(Set<Recipies> recipies) {
        if (this.recipes != null) {
            this.recipes.forEach(i -> i.removeImagesUrls(this));
        }
        if (recipies != null) {
            recipies.forEach(i -> i.addImagesUrls(this));
        }
        this.recipes = recipies;
    }

    public ImagesUrl recipes(Set<Recipies> recipies) {
        this.setRecipes(recipies);
        return this;
    }

    public ImagesUrl addRecipe(Recipies recipies) {
        this.recipes.add(recipies);
        recipies.getImagesUrls().add(this);
        return this;
    }

    public ImagesUrl removeRecipe(Recipies recipies) {
        this.recipes.remove(recipies);
        recipies.getImagesUrls().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagesUrl)) {
            return false;
        }
        return id != null && id.equals(((ImagesUrl) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesUrl{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", urlAddress='" + getUrlAddress() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", type='" + getType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
