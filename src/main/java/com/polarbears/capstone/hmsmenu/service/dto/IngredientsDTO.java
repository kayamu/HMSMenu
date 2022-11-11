package com.polarbears.capstone.hmsmenu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.Ingredients} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngredientsDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createdDate;

    private Set<ImagesUrlDTO> imagesUrls = new HashSet<>();

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
        if (!(o instanceof IngredientsDTO)) {
            return false;
        }

        IngredientsDTO ingredientsDTO = (IngredientsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingredientsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", imagesUrls=" + getImagesUrls() +
            ", nutriens=" + getNutriens() +
            "}";
    }
}
