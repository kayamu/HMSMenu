package com.polarbears.capstone.hmsmenu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.Nutriens} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NutriensDTO implements Serializable {

    private Long id;

    private String name;

    private Double protein;

    private Double carb;

    private Double fat;

    private Double kcal;

    private LocalDate createdDate;

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

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarb() {
        return carb;
    }

    public void setCarb(Double carb) {
        this.carb = carb;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NutriensDTO)) {
            return false;
        }

        NutriensDTO nutriensDTO = (NutriensDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nutriensDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NutriensDTO{" +
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
