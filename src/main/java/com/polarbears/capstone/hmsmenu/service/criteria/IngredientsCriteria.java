package com.polarbears.capstone.hmsmenu.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.Ingredients} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.IngredientsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ingredients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngredientsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter createdDate;

    private LongFilter mealIngredientsId;

    private LongFilter imagesUrlsId;

    private LongFilter nutriensId;

    private LongFilter menuGroupsId;

    private Boolean distinct;

    public IngredientsCriteria() {}

    public IngredientsCriteria(IngredientsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.mealIngredientsId = other.mealIngredientsId == null ? null : other.mealIngredientsId.copy();
        this.imagesUrlsId = other.imagesUrlsId == null ? null : other.imagesUrlsId.copy();
        this.nutriensId = other.nutriensId == null ? null : other.nutriensId.copy();
        this.menuGroupsId = other.menuGroupsId == null ? null : other.menuGroupsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IngredientsCriteria copy() {
        return new IngredientsCriteria(this);
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

    public LongFilter getMealIngredientsId() {
        return mealIngredientsId;
    }

    public LongFilter mealIngredientsId() {
        if (mealIngredientsId == null) {
            mealIngredientsId = new LongFilter();
        }
        return mealIngredientsId;
    }

    public void setMealIngredientsId(LongFilter mealIngredientsId) {
        this.mealIngredientsId = mealIngredientsId;
    }

    public LongFilter getImagesUrlsId() {
        return imagesUrlsId;
    }

    public LongFilter imagesUrlsId() {
        if (imagesUrlsId == null) {
            imagesUrlsId = new LongFilter();
        }
        return imagesUrlsId;
    }

    public void setImagesUrlsId(LongFilter imagesUrlsId) {
        this.imagesUrlsId = imagesUrlsId;
    }

    public LongFilter getNutriensId() {
        return nutriensId;
    }

    public LongFilter nutriensId() {
        if (nutriensId == null) {
            nutriensId = new LongFilter();
        }
        return nutriensId;
    }

    public void setNutriensId(LongFilter nutriensId) {
        this.nutriensId = nutriensId;
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
        final IngredientsCriteria that = (IngredientsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(mealIngredientsId, that.mealIngredientsId) &&
            Objects.equals(imagesUrlsId, that.imagesUrlsId) &&
            Objects.equals(nutriensId, that.nutriensId) &&
            Objects.equals(menuGroupsId, that.menuGroupsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate, mealIngredientsId, imagesUrlsId, nutriensId, menuGroupsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (mealIngredientsId != null ? "mealIngredientsId=" + mealIngredientsId + ", " : "") +
            (imagesUrlsId != null ? "imagesUrlsId=" + imagesUrlsId + ", " : "") +
            (nutriensId != null ? "nutriensId=" + nutriensId + ", " : "") +
            (menuGroupsId != null ? "menuGroupsId=" + menuGroupsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
