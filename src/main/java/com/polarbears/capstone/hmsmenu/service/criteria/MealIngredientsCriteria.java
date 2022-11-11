package com.polarbears.capstone.hmsmenu.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.MealIngredients} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.MealIngredientsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /meal-ingredients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MealIngredientsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter amount;

    private StringFilter unit;

    private LocalDateFilter createdDate;

    private LongFilter nutriensId;

    private LongFilter ingradientsId;

    private LongFilter mealsId;

    private Boolean distinct;

    public MealIngredientsCriteria() {}

    public MealIngredientsCriteria(MealIngredientsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.nutriensId = other.nutriensId == null ? null : other.nutriensId.copy();
        this.ingradientsId = other.ingradientsId == null ? null : other.ingradientsId.copy();
        this.mealsId = other.mealsId == null ? null : other.mealsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MealIngredientsCriteria copy() {
        return new MealIngredientsCriteria(this);
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

    public StringFilter getAmount() {
        return amount;
    }

    public StringFilter amount() {
        if (amount == null) {
            amount = new StringFilter();
        }
        return amount;
    }

    public void setAmount(StringFilter amount) {
        this.amount = amount;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public StringFilter unit() {
        if (unit == null) {
            unit = new StringFilter();
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
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

    public LongFilter getIngradientsId() {
        return ingradientsId;
    }

    public LongFilter ingradientsId() {
        if (ingradientsId == null) {
            ingradientsId = new LongFilter();
        }
        return ingradientsId;
    }

    public void setIngradientsId(LongFilter ingradientsId) {
        this.ingradientsId = ingradientsId;
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
        final MealIngredientsCriteria that = (MealIngredientsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(nutriensId, that.nutriensId) &&
            Objects.equals(ingradientsId, that.ingradientsId) &&
            Objects.equals(mealsId, that.mealsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, unit, createdDate, nutriensId, ingradientsId, mealsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MealIngredientsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (nutriensId != null ? "nutriensId=" + nutriensId + ", " : "") +
            (ingradientsId != null ? "ingradientsId=" + ingradientsId + ", " : "") +
            (mealsId != null ? "mealsId=" + mealsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
