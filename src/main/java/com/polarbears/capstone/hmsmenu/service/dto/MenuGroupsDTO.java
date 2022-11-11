package com.polarbears.capstone.hmsmenu.service.dto;

import com.polarbears.capstone.hmsmenu.domain.enumeration.BODYFATS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.GOALS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.UNITS;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.MenuGroups} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuGroupsDTO implements Serializable {

    private Long id;

    private Long contactId;

    private String name;

    private Double cost;

    private Double salesPrice;

    private String explanation;

    private GOALS goal;

    private BODYFATS bodyType;

    @Min(value = 1)
    @Max(value = 10)
    private Integer activityLevelMin;

    @Min(value = 1)
    @Max(value = 10)
    private Integer activityLevelMax;

    private Double weightMin;

    private Double weightMax;

    private Double dailyKcalMin;

    private Double dailyKcalMax;

    private Double targetWeightMin;

    private Double targetWeightMax;

    private UNITS unit;

    private LocalDate createdDate;

    private Set<IngredientsDTO> ingradients = new HashSet<>();

    private Set<MenusDTO> menus = new HashSet<>();

    private Set<ImagesUrlDTO> imagesUrls = new HashSet<>();

    private NutriensDTO nutriens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public GOALS getGoal() {
        return goal;
    }

    public void setGoal(GOALS goal) {
        this.goal = goal;
    }

    public BODYFATS getBodyType() {
        return bodyType;
    }

    public void setBodyType(BODYFATS bodyType) {
        this.bodyType = bodyType;
    }

    public Integer getActivityLevelMin() {
        return activityLevelMin;
    }

    public void setActivityLevelMin(Integer activityLevelMin) {
        this.activityLevelMin = activityLevelMin;
    }

    public Integer getActivityLevelMax() {
        return activityLevelMax;
    }

    public void setActivityLevelMax(Integer activityLevelMax) {
        this.activityLevelMax = activityLevelMax;
    }

    public Double getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(Double weightMin) {
        this.weightMin = weightMin;
    }

    public Double getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(Double weightMax) {
        this.weightMax = weightMax;
    }

    public Double getDailyKcalMin() {
        return dailyKcalMin;
    }

    public void setDailyKcalMin(Double dailyKcalMin) {
        this.dailyKcalMin = dailyKcalMin;
    }

    public Double getDailyKcalMax() {
        return dailyKcalMax;
    }

    public void setDailyKcalMax(Double dailyKcalMax) {
        this.dailyKcalMax = dailyKcalMax;
    }

    public Double getTargetWeightMin() {
        return targetWeightMin;
    }

    public void setTargetWeightMin(Double targetWeightMin) {
        this.targetWeightMin = targetWeightMin;
    }

    public Double getTargetWeightMax() {
        return targetWeightMax;
    }

    public void setTargetWeightMax(Double targetWeightMax) {
        this.targetWeightMax = targetWeightMax;
    }

    public UNITS getUnit() {
        return unit;
    }

    public void setUnit(UNITS unit) {
        this.unit = unit;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<IngredientsDTO> getIngradients() {
        return ingradients;
    }

    public void setIngradients(Set<IngredientsDTO> ingradients) {
        this.ingradients = ingradients;
    }

    public Set<MenusDTO> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenusDTO> menus) {
        this.menus = menus;
    }

    public Set<ImagesUrlDTO> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrlDTO> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public NutriensDTO getNutriens() {
        return nutriens;
    }

    public void setNutriens(NutriensDTO nutriens) {
        this.nutriens = nutriens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuGroupsDTO)) {
            return false;
        }

        MenuGroupsDTO menuGroupsDTO = (MenuGroupsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuGroupsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuGroupsDTO{" +
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
            ", ingradients=" + getIngradients() +
            ", menus=" + getMenus() +
            ", imagesUrls=" + getImagesUrls() +
            ", nutriens=" + getNutriens() +
            "}";
    }
}
