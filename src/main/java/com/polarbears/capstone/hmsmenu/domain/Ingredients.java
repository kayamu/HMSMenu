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
 * A Ingredients.
 */
@Entity
@Table(name = "ingredients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ingredients implements Serializable {

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

    @OneToMany(mappedBy = "ingradients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nutriens", "ingradients", "meals" }, allowSetters = true)
    private Set<MealIngredients> mealIngredients = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_ingredients__images_urls",
        joinColumns = @JoinColumn(name = "ingredients_id"),
        inverseJoinColumns = @JoinColumn(name = "images_urls_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "meals", "ingredients", "recipes" }, allowSetters = true)
    private Set<ImagesUrl> imagesUrls = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "mealIngredients", "meals", "ingredients" }, allowSetters = true)
    private Nutriens nutriens;

    @ManyToMany(mappedBy = "ingradients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingradients", "menus", "imagesUrls", "nutriens" }, allowSetters = true)
    private Set<MenuGroups> menuGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ingredients id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ingredients name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Ingredients createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<MealIngredients> getMealIngredients() {
        return this.mealIngredients;
    }

    public void setMealIngredients(Set<MealIngredients> mealIngredients) {
        if (this.mealIngredients != null) {
            this.mealIngredients.forEach(i -> i.setIngradients(null));
        }
        if (mealIngredients != null) {
            mealIngredients.forEach(i -> i.setIngradients(this));
        }
        this.mealIngredients = mealIngredients;
    }

    public Ingredients mealIngredients(Set<MealIngredients> mealIngredients) {
        this.setMealIngredients(mealIngredients);
        return this;
    }

    public Ingredients addMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.add(mealIngredients);
        mealIngredients.setIngradients(this);
        return this;
    }

    public Ingredients removeMealIngredients(MealIngredients mealIngredients) {
        this.mealIngredients.remove(mealIngredients);
        mealIngredients.setIngradients(null);
        return this;
    }

    public Set<ImagesUrl> getImagesUrls() {
        return this.imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrl> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Ingredients imagesUrls(Set<ImagesUrl> imagesUrls) {
        this.setImagesUrls(imagesUrls);
        return this;
    }

    public Ingredients addImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.add(imagesUrl);
        imagesUrl.getIngredients().add(this);
        return this;
    }

    public Ingredients removeImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.remove(imagesUrl);
        imagesUrl.getIngredients().remove(this);
        return this;
    }

    public Nutriens getNutriens() {
        return this.nutriens;
    }

    public void setNutriens(Nutriens nutriens) {
        this.nutriens = nutriens;
    }

    public Ingredients nutriens(Nutriens nutriens) {
        this.setNutriens(nutriens);
        return this;
    }

    public Set<MenuGroups> getMenuGroups() {
        return this.menuGroups;
    }

    public void setMenuGroups(Set<MenuGroups> menuGroups) {
        if (this.menuGroups != null) {
            this.menuGroups.forEach(i -> i.removeIngradients(this));
        }
        if (menuGroups != null) {
            menuGroups.forEach(i -> i.addIngradients(this));
        }
        this.menuGroups = menuGroups;
    }

    public Ingredients menuGroups(Set<MenuGroups> menuGroups) {
        this.setMenuGroups(menuGroups);
        return this;
    }

    public Ingredients addMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.add(menuGroups);
        menuGroups.getIngradients().add(this);
        return this;
    }

    public Ingredients removeMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.remove(menuGroups);
        menuGroups.getIngradients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingredients)) {
            return false;
        }
        return id != null && id.equals(((Ingredients) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingredients{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
