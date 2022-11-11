package com.polarbears.capstone.hmsmenu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmsmenu.domain.enumeration.BODYFATS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.GOALS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.UNITS;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MenuGroups.
 */
@Entity
@Table(name = "menu_groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal")
    private GOALS goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type")
    private BODYFATS bodyType;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "activity_level_min")
    private Integer activityLevelMin;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "activity_level_max")
    private Integer activityLevelMax;

    @Column(name = "weight_min")
    private Double weightMin;

    @Column(name = "weight_max")
    private Double weightMax;

    @Column(name = "daily_kcal_min")
    private Double dailyKcalMin;

    @Column(name = "daily_kcal_max")
    private Double dailyKcalMax;

    @Column(name = "target_weight_min")
    private Double targetWeightMin;

    @Column(name = "target_weight_max")
    private Double targetWeightMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit")
    private UNITS unit;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_menu_groups__ingradients",
        joinColumns = @JoinColumn(name = "menu_groups_id"),
        inverseJoinColumns = @JoinColumn(name = "ingradients_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mealIngredients", "imagesUrls", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Ingredients> ingradients = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_menu_groups__menus",
        joinColumns = @JoinColumn(name = "menu_groups_id"),
        inverseJoinColumns = @JoinColumn(name = "menus_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imagesUrls", "meals", "nutriens", "menuGroups" }, allowSetters = true)
    private Set<Menus> menus = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_menu_groups__images_urls",
        joinColumns = @JoinColumn(name = "menu_groups_id"),
        inverseJoinColumns = @JoinColumn(name = "images_urls_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "meals", "ingredients", "recipes" }, allowSetters = true)
    private Set<ImagesUrl> imagesUrls = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "menuGroups", "menus", "mealIngredients", "meals", "ingredients" }, allowSetters = true)
    private Nutriens nutriens;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MenuGroups id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return this.contactId;
    }

    public MenuGroups contactId(Long contactId) {
        this.setContactId(contactId);
        return this;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return this.name;
    }

    public MenuGroups name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return this.cost;
    }

    public MenuGroups cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSalesPrice() {
        return this.salesPrice;
    }

    public MenuGroups salesPrice(Double salesPrice) {
        this.setSalesPrice(salesPrice);
        return this;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public MenuGroups explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public GOALS getGoal() {
        return this.goal;
    }

    public MenuGroups goal(GOALS goal) {
        this.setGoal(goal);
        return this;
    }

    public void setGoal(GOALS goal) {
        this.goal = goal;
    }

    public BODYFATS getBodyType() {
        return this.bodyType;
    }

    public MenuGroups bodyType(BODYFATS bodyType) {
        this.setBodyType(bodyType);
        return this;
    }

    public void setBodyType(BODYFATS bodyType) {
        this.bodyType = bodyType;
    }

    public Integer getActivityLevelMin() {
        return this.activityLevelMin;
    }

    public MenuGroups activityLevelMin(Integer activityLevelMin) {
        this.setActivityLevelMin(activityLevelMin);
        return this;
    }

    public void setActivityLevelMin(Integer activityLevelMin) {
        this.activityLevelMin = activityLevelMin;
    }

    public Integer getActivityLevelMax() {
        return this.activityLevelMax;
    }

    public MenuGroups activityLevelMax(Integer activityLevelMax) {
        this.setActivityLevelMax(activityLevelMax);
        return this;
    }

    public void setActivityLevelMax(Integer activityLevelMax) {
        this.activityLevelMax = activityLevelMax;
    }

    public Double getWeightMin() {
        return this.weightMin;
    }

    public MenuGroups weightMin(Double weightMin) {
        this.setWeightMin(weightMin);
        return this;
    }

    public void setWeightMin(Double weightMin) {
        this.weightMin = weightMin;
    }

    public Double getWeightMax() {
        return this.weightMax;
    }

    public MenuGroups weightMax(Double weightMax) {
        this.setWeightMax(weightMax);
        return this;
    }

    public void setWeightMax(Double weightMax) {
        this.weightMax = weightMax;
    }

    public Double getDailyKcalMin() {
        return this.dailyKcalMin;
    }

    public MenuGroups dailyKcalMin(Double dailyKcalMin) {
        this.setDailyKcalMin(dailyKcalMin);
        return this;
    }

    public void setDailyKcalMin(Double dailyKcalMin) {
        this.dailyKcalMin = dailyKcalMin;
    }

    public Double getDailyKcalMax() {
        return this.dailyKcalMax;
    }

    public MenuGroups dailyKcalMax(Double dailyKcalMax) {
        this.setDailyKcalMax(dailyKcalMax);
        return this;
    }

    public void setDailyKcalMax(Double dailyKcalMax) {
        this.dailyKcalMax = dailyKcalMax;
    }

    public Double getTargetWeightMin() {
        return this.targetWeightMin;
    }

    public MenuGroups targetWeightMin(Double targetWeightMin) {
        this.setTargetWeightMin(targetWeightMin);
        return this;
    }

    public void setTargetWeightMin(Double targetWeightMin) {
        this.targetWeightMin = targetWeightMin;
    }

    public Double getTargetWeightMax() {
        return this.targetWeightMax;
    }

    public MenuGroups targetWeightMax(Double targetWeightMax) {
        this.setTargetWeightMax(targetWeightMax);
        return this;
    }

    public void setTargetWeightMax(Double targetWeightMax) {
        this.targetWeightMax = targetWeightMax;
    }

    public UNITS getUnit() {
        return this.unit;
    }

    public MenuGroups unit(UNITS unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(UNITS unit) {
        this.unit = unit;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public MenuGroups createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Ingredients> getIngradients() {
        return this.ingradients;
    }

    public void setIngradients(Set<Ingredients> ingredients) {
        this.ingradients = ingredients;
    }

    public MenuGroups ingradients(Set<Ingredients> ingredients) {
        this.setIngradients(ingredients);
        return this;
    }

    public MenuGroups addIngradients(Ingredients ingredients) {
        this.ingradients.add(ingredients);
        ingredients.getMenuGroups().add(this);
        return this;
    }

    public MenuGroups removeIngradients(Ingredients ingredients) {
        this.ingradients.remove(ingredients);
        ingredients.getMenuGroups().remove(this);
        return this;
    }

    public Set<Menus> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menus> menus) {
        this.menus = menus;
    }

    public MenuGroups menus(Set<Menus> menus) {
        this.setMenus(menus);
        return this;
    }

    public MenuGroups addMenus(Menus menus) {
        this.menus.add(menus);
        menus.getMenuGroups().add(this);
        return this;
    }

    public MenuGroups removeMenus(Menus menus) {
        this.menus.remove(menus);
        menus.getMenuGroups().remove(this);
        return this;
    }

    public Set<ImagesUrl> getImagesUrls() {
        return this.imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrl> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public MenuGroups imagesUrls(Set<ImagesUrl> imagesUrls) {
        this.setImagesUrls(imagesUrls);
        return this;
    }

    public MenuGroups addImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.add(imagesUrl);
        imagesUrl.getMenuGroups().add(this);
        return this;
    }

    public MenuGroups removeImagesUrls(ImagesUrl imagesUrl) {
        this.imagesUrls.remove(imagesUrl);
        imagesUrl.getMenuGroups().remove(this);
        return this;
    }

    public Nutriens getNutriens() {
        return this.nutriens;
    }

    public void setNutriens(Nutriens nutriens) {
        this.nutriens = nutriens;
    }

    public MenuGroups nutriens(Nutriens nutriens) {
        this.setNutriens(nutriens);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuGroups)) {
            return false;
        }
        return id != null && id.equals(((MenuGroups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuGroups{" +
            "id=" + getId() +
            ", contactId=" + getContactId() +
            ", name='" + getName() + "'" +
            ", cost=" + getCost() +
            ", salesPrice=" + getSalesPrice() +
            ", explanation='" + getExplanation() + "'" +
            ", goal='" + getGoal() + "'" +
            ", bodyType='" + getBodyType() + "'" +
            ", activityLevelMin=" + getActivityLevelMin() +
            ", activityLevelMax=" + getActivityLevelMax() +
            ", weightMin=" + getWeightMin() +
            ", weightMax=" + getWeightMax() +
            ", dailyKcalMin=" + getDailyKcalMin() +
            ", dailyKcalMax=" + getDailyKcalMax() +
            ", targetWeightMin=" + getTargetWeightMin() +
            ", targetWeightMax=" + getTargetWeightMax() +
            ", unit='" + getUnit() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
