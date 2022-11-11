package com.polarbears.capstone.hmsmenu.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.Meals} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.MealsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /meals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter createdDate;

    private LongFilter imagesUrlsId;

    private LongFilter mealIngredientsId;

    private LongFilter nutriensId;

    private LongFilter recipiesId;

    private LongFilter menusId;

    private Boolean distinct;

    public MealsCriteria() {}

    public MealsCriteria(MealsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.imagesUrlsId = other.imagesUrlsId == null ? null : other.imagesUrlsId.copy();
        this.mealIngredientsId = other.mealIngredientsId == null ? null : other.mealIngredientsId.copy();
        this.nutriensId = other.nutriensId == null ? null : other.nutriensId.copy();
        this.recipiesId = other.recipiesId == null ? null : other.recipiesId.copy();
        this.menusId = other.menusId == null ? null : other.menusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MealsCriteria copy() {
        return new MealsCriteria(this);
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

    public LongFilter getRecipiesId() {
        return recipiesId;
    }

    public LongFilter recipiesId() {
        if (recipiesId == null) {
            recipiesId = new LongFilter();
        }
        return recipiesId;
    }

    public void setRecipiesId(LongFilter recipiesId) {
        this.recipiesId = recipiesId;
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
        final MealsCriteria that = (MealsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(imagesUrlsId, that.imagesUrlsId) &&
            Objects.equals(mealIngredientsId, that.mealIngredientsId) &&
            Objects.equals(nutriensId, that.nutriensId) &&
            Objects.equals(recipiesId, that.recipiesId) &&
            Objects.equals(menusId, that.menusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdDate, imagesUrlsId, mealIngredientsId, nutriensId, recipiesId, menusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (imagesUrlsId != null ? "imagesUrlsId=" + imagesUrlsId + ", " : "") +
            (mealIngredientsId != null ? "mealIngredientsId=" + mealIngredientsId + ", " : "") +
            (nutriensId != null ? "nutriensId=" + nutriensId + ", " : "") +
            (recipiesId != null ? "recipiesId=" + recipiesId + ", " : "") +
            (menusId != null ? "menusId=" + menusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
