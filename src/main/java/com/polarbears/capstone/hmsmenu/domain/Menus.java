package com.polarbears.capstone.hmsmenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmsmenu.domain.enumeration.DAYS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.REPAST;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Menus.
 */
@Entity
@Table(name = "menus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Menus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_day")
    private DAYS menuDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "menu_time")
    private REPAST menuTime;

    @Column(name = "contact_id")
    private Integer contactId;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_menus__images_urls",
        joinColumns = @JoinColumn(name = "menus_id"),
        inverseJoinColumns = @JoinColumn(name = "images_urls_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "meals", "ingredients", "recipes" }, allowSetters = true)
    private Set<ImagesUrl> imagesUrls = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_menus__meals", joinColumns = @JoinColumn(name = "menus_id"), inverseJoinColumns = @JoinColumn(name = "meals_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "mealIngredients", "nutriens", "recipies", "menus" }, allowSetters = true)
    private Set<Meals> meals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "mealIngredients", "meals", "ingredients" }, allowSetters = true)
    private Nutriens nutriens;

    @ManyToMany(mappedBy = "menus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ingradients", "menus", "imagesUrls", "nutriens" }, allowSetters = true)
    private Set<MenuGroups> menuGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Menus name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DAYS getMenuDay() {
        return this.menuDay;
    }

    public Menus menuDay(DAYS menuDay) {
        this.setMenuDay(menuDay);
        return this;
    }

    public void setMenuDay(DAYS menuDay) {
        this.menuDay = menuDay;
    }

    public REPAST getMenuTime() {
        return this.menuTime;
    }

    public Menus menuTime(REPAST menuTime) {
        this.setMenuTime(menuTime);
        return this;
    }

    public void setMenuTime(REPAST menuTime) {
        this.menuTime = menuTime;
    }

    public Integer getContactId() {
        return this.contactId;
    }

    public Menus contactId(Integer contactId) {
        this.setContactId(contactId);
        return this;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Double getCost() {
        return this.cost;
    }

    public Menus cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSalesPrice() {
        return this.salesPrice;
    }

    public Menus salesPrice(Double salesPrice) {
        this.setSalesPrice(salesPrice);
        return this;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public Menus explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Menus createdDate(LocalDate createdDate) {
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

    public Menus imagesUrls(Set<ImagesUrl> imagesUrls) {
        this.setImagesUrls(imagesUrls);
        return this;
    }

    public Menus addImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.add(imagesUrl);
        imagesUrl.getMenus().add(this);
        return this;
    }

    public Menus removeImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.remove(imagesUrl);
        imagesUrl.getMenus().remove(this);
        return this;
    }

    public Set<Meals> getMeals() {
        return this.meals;
    }

    public void setMeals(Set<Meals> meals) {
        this.meals = meals;
    }

    public Menus meals(Set<Meals> meals) {
        this.setMeals(meals);
        return this;
    }

    public Menus addMeals(Meals meals) {
        this.meals.add(meals);
        meals.getMenus().add(this);
        return this;
    }

    public Menus removeMeals(Meals meals) {
        this.meals.remove(meals);
        meals.getMenus().remove(this);
        return this;
    }

    public Nutriens getNutriens() {
        return this.nutriens;
    }

    public void setNutriens(Nutriens nutriens) {
        this.nutriens = nutriens;
    }

    public Menus nutriens(Nutriens nutriens) {
        this.setNutriens(nutriens);
        return this;
    }

    public Set<MenuGroups> getMenuGroups() {
        return this.menuGroups;
    }

    public void setMenuGroups(Set<MenuGroups> menuGroups) {
        if (this.menuGroups != null) {
            this.menuGroups.forEach(i -> i.removeMenus(this));
        }
        if (menuGroups != null) {
            menuGroups.forEach(i -> i.addMenus(this));
        }
        this.menuGroups = menuGroups;
    }

    public Menus menuGroups(Set<MenuGroups> menuGroups) {
        this.setMenuGroups(menuGroups);
        return this;
    }

    public Menus addMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.add(menuGroups);
        menuGroups.getMenus().add(this);
        return this;
    }

    public Menus removeMenuGroups(MenuGroups menuGroups) {
        this.menuGroups.remove(menuGroups);
        menuGroups.getMenus().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menus)) {
            return false;
        }
        return id != null && id.equals(((Menus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", menuDay='" + getMenuDay() + "'" +
            ", menuTime='" + getMenuTime() + "'" +
            ", contactId=" + getContactId() +
            ", cost=" + getCost() +
            ", salesPrice=" + getSalesPrice() +
            ", explanation='" + getExplanation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
