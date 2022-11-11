package com.polarbears.capstone.hmsmenu.service.criteria;

import com.polarbears.capstone.hmsmenu.domain.enumeration.IMAGETYPES;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.ImagesUrl} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.ImagesUrlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images-urls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesUrlCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IMAGETYPES
     */
    public static class IMAGETYPESFilter extends Filter<IMAGETYPES> {

        public IMAGETYPESFilter() {}

        public IMAGETYPESFilter(IMAGETYPESFilter filter) {
            super(filter);
        }

        @Override
        public IMAGETYPESFilter copy() {
            return new IMAGETYPESFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter urlAddress;

    private StringFilter explanation;

    private IMAGETYPESFilter type;

    private LocalDateFilter createdDate;

    private LongFilter menuGroupsId;

    private LongFilter menusId;

    private LongFilter mealsId;

    private LongFilter ingredientsId;

    private LongFilter recipeId;

    private Boolean distinct;

    public ImagesUrlCriteria() {}

    public ImagesUrlCriteria(ImagesUrlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.urlAddress = other.urlAddress == null ? null : other.urlAddress.copy();
        this.explanation = other.explanation == null ? null : other.explanation.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.menuGroupsId = other.menuGroupsId == null ? null : other.menuGroupsId.copy();
        this.menusId = other.menusId == null ? null : other.menusId.copy();
        this.mealsId = other.mealsId == null ? null : other.mealsId.copy();
        this.ingredientsId = other.ingredientsId == null ? null : other.ingredientsId.copy();
        this.recipeId = other.recipeId == null ? null : other.recipeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImagesUrlCriteria copy() {
        return new ImagesUrlCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUrlAddress() {
        return urlAddress;
    }

    public StringFilter urlAddress() {
        if (urlAddress == null) {
            urlAddress = new StringFilter();
        }
        return urlAddress;
    }

    public void setUrlAddress(StringFilter urlAddress) {
        this.urlAddress = urlAddress;
    }

    public StringFilter getExplanation() {
        return explanation;
    }

    public StringFilter explanation() {
        if (explanation == null) {
            explanation = new StringFilter();
        }
        return explanation;
    }

    public void setExplanation(StringFilter explanation) {
        this.explanation = explanation;
    }

    public IMAGETYPESFilter getType() {
        return type;
    }

    public IMAGETYPESFilter type() {
        if (type == null) {
            type = new IMAGETYPESFilter();
        }
        return type;
    }

    public void setType(IMAGETYPESFilter type) {
        this.type = type;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            createdDate = new LocalDateFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getMenuGroupsId() {
        return menuGroupsId;
    }

    public LongFilter menuGroupsId() {
        if (menuGroupsId == null) {
            menuGroupsId = new LongFilter();
        }
        return menuGroupsId;
    }

    public void setMenuGroupsId(LongFilter menuGroupsId) {
        this.menuGroupsId = menuGroupsId;
    }

    public LongFilter getMenusId() {
        return menusId;
    }

    public LongFilter menusId() {
        if (menusId == null) {
            menusId = new LongFilter();
        }
        return menusId;
    }

    public void setMenusId(LongFilter menusId) {
        this.menusId = menusId;
    }

    public LongFilter getMealsId() {
        return mealsId;
    }

    public LongFilter mealsId() {
        if (mealsId == null) {
            mealsId = new LongFilter();
        }
        return mealsId;
    }

    public void setMealsId(LongFilter mealsId) {
        this.mealsId = mealsId;
    }

    public LongFilter getIngredientsId() {
        return ingredientsId;
    }

    public LongFilter ingredientsId() {
        if (ingredientsId == null) {
            ingredientsId = new LongFilter();
        }
        return ingredientsId;
    }

    public void setIngredientsId(LongFilter ingredientsId) {
        this.ingredientsId = ingredientsId;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public LongFilter recipeId() {
        if (recipeId == null) {
            recipeId = new LongFilter();
        }
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImagesUrlCriteria that = (ImagesUrlCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(urlAddress, that.urlAddress) &&
            Objects.equals(explanation, that.explanation) &&
            Objects.equals(type, that.type) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(menuGroupsId, that.menuGroupsId) &&
            Objects.equals(menusId, that.menusId) &&
            Objects.equals(mealsId, that.mealsId) &&
            Objects.equals(ingredientsId, that.ingredientsId) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            urlAddress,
            explanation,
            type,
            createdDate,
            menuGroupsId,
            menusId,
            mealsId,
            ingredientsId,
            recipeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesUrlCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (urlAddress != null ? "urlAddress=" + urlAddress + ", " : "") +
            (explanation != null ? "explanation=" + explanation + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (menuGroupsId != null ? "menuGroupsId=" + menuGroupsId + ", " : "") +
            (menusId != null ? "menusId=" + menusId + ", " : "") +
            (mealsId != null ? "mealsId=" + mealsId + ", " : "") +
            (ingredientsId != null ? "ingredientsId=" + ingredientsId + ", " : "") +
            (recipeId != null ? "recipeId=" + recipeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
