package com.polarbears.capstone.hmsmenu.service.dto;

import com.polarbears.capstone.hmsmenu.domain.enumeration.DAYS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.REPAST;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.Menus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenusDTO implements Serializable {

    private Long id;

    private String name;

    private DAYS menuDay;

    private REPAST menuTime;

    private Integer contactId;

    private Double cost;

    private Double salesPrice;

    private String explanation;

    private LocalDate createdDate;

    private Set<ImagesUrlDTO> imagesUrls = new HashSet<>();

    private Set<MealsDTO> meals = new HashSet<>();

    private NutriensDTO nutriens;

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

    public DAYS getMenuDay() {
        return menuDay;
    }

    public void setMenuDay(DAYS menuDay) {
        this.menuDay = menuDay;
    }

    public REPAST getMenuTime() {
        return menuTime;
    }

    public void setMenuTime(REPAST menuTime) {
        this.menuTime = menuTime;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ImagesUrlDTO> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(Set<ImagesUrlDTO> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public Set<MealsDTO> getMeals() {
        return meals;
    }

    public void setMeals(Set<MealsDTO> meals) {
        this.meals = meals;
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
        if (!(o instanceof MenusDTO)) {
            return false;
        }

        MenusDTO menusDTO = (MenusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenusDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", menuDay='" + getMenuDay() + "'" +
            ", menuTime='" + getMenuTime() + "'" +
            ", contactId=" + getContactId() +
            ", cost=" + getCost() +
            ", salesPrice=" + getSalesPrice() +
            ", explanation='" + getExplanation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", imagesUrls=" + getImagesUrls() +
            ", meals=" + getMeals() +
            ", nutriens=" + getNutriens() +
            "}";
    }
}
