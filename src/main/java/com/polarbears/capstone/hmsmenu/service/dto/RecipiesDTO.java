package com.polarbears.capstone.hmsmenu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.Recipies} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipiesDTO implements Serializable {

    private Long id;

    private String name;

    private String recipe;

    private String explanation;

    private LocalDate createdDate;

    private Set<ImagesUrlDTO> imagesUrls = new HashSet<>();

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

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecipiesDTO)) {
            return false;
        }

        RecipiesDTO recipiesDTO = (RecipiesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recipiesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipiesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", recipe='" + getRecipe() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", imagesUrls=" + getImagesUrls() +
            "}";
    }
}
