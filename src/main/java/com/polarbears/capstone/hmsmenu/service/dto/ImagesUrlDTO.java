package com.polarbears.capstone.hmsmenu.service.dto;

import com.polarbears.capstone.hmsmenu.domain.enumeration.IMAGETYPES;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsmenu.domain.ImagesUrl} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesUrlDTO implements Serializable {

    private Long id;

    private String name;

    private String urlAddress;

    private String explanation;

    private IMAGETYPES type;

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

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public IMAGETYPES getType() {
        return type;
    }

    public void setType(IMAGETYPES type) {
        this.type = type;
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
        if (!(o instanceof ImagesUrlDTO)) {
            return false;
        }

        ImagesUrlDTO imagesUrlDTO = (ImagesUrlDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagesUrlDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesUrlDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", urlAddress='" + getUrlAddress() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", type='" + getType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
