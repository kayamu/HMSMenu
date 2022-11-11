package com.polarbears.capstone.hmsmenu.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.Recipies} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.RecipiesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recipies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipiesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter recipe;

    private StringFilter explanation;

    private LocalDateFilter createdDate;

    private LongFilter mealId;

    private LongFilter imagesUrlsId;

    private Boolean distinct;

    public RecipiesCriteria() {}

    public RecipiesCriteria(RecipiesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.recipe = other.recipe == null ? null : other.recipe.copy();
        this.explanation = other.explanation == null ? null : other.explanation.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.mealId = other.mealId == null ? null : other.mealId.copy();
        this.imagesUrlsId = other.imagesUrlsId == null ? null : other.imagesUrlsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RecipiesCriteria copy() {
        return new RecipiesCriteria(this);
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

    public StringFilter getRecipe() {
        return recipe;
    }

    public StringFilter recipe() {
        if (recipe == null) {
            recipe = new StringFilter();
        }
        return recipe;
    }

    public void setRecipe(StringFilter recipe) {
        this.recipe = recipe;
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

    public LongFilter getMealId() {
        return mealId;
    }

    public LongFilter mealId() {
        if (mealId == null) {
            mealId = new LongFilter();
        }
        return mealId;
    }

    public void setMealId(LongFilter mealId) {
        this.mealId = mealId;
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
        final RecipiesCriteria that = (RecipiesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(recipe, that.recipe) &&
            Objects.equals(explanation, that.explanation) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(mealId, that.mealId) &&
            Objects.equals(imagesUrlsId, that.imagesUrlsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recipe, explanation, createdDate, mealId, imagesUrlsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipiesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (recipe != null ? "recipe=" + recipe + ", " : "") +
            (explanation != null ? "explanation=" + explanation + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (mealId != null ? "mealId=" + mealId + ", " : "") +
            (imagesUrlsId != null ? "imagesUrlsId=" + imagesUrlsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
