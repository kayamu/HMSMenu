package com.polarbears.capstone.hmsmenu.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.Nutriens} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.NutriensResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nutriens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NutriensCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter protein;

    private DoubleFilter carb;

    private DoubleFilter fat;

    private DoubleFilter kcal;

    private LocalDateFilter createdDate;

    private LongFilter menuGroupsId;

    private LongFilter menusId;

    private LongFilter mealIngredientsId;

    private LongFilter mealsId;

    private LongFilter ingredientsId;

    private Boolean distinct;

    public NutriensCriteria() {}

    public NutriensCriteria(NutriensCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.protein = other.protein == null ? null : other.protein.copy();
        this.carb = other.carb == null ? null : other.carb.copy();
        this.fat = other.fat == null ? null : other.fat.copy();
        this.kcal = other.kcal == null ? null : other.kcal.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.menuGroupsId = other.menuGroupsId == null ? null : other.menuGroupsId.copy();
        this.menusId = other.menusId == null ? null : other.menusId.copy();
        this.mealIngredientsId = other.mealIngredientsId == null ? null : other.mealIngredientsId.copy();
        this.mealsId = other.mealsId == null ? null : other.mealsId.copy();
        this.ingredientsId = other.ingredientsId == null ? null : other.ingredientsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NutriensCriteria copy() {
        return new NutriensCriteria(this);
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

    public DoubleFilter getProtein() {
        return protein;
    }

    public DoubleFilter protein() {
        if (protein == null) {
            protein = new DoubleFilter();
        }
        return protein;
    }

    public void setProtein(DoubleFilter protein) {
        this.protein = protein;
    }

    public DoubleFilter getCarb() {
        return carb;
    }

    public DoubleFilter carb() {
        if (carb == null) {
            carb = new DoubleFilter();
        }
        return carb;
    }

    public void setCarb(DoubleFilter carb) {
        this.carb = carb;
    }

    public DoubleFilter getFat() {
        return fat;
    }

    public DoubleFilter fat() {
        if (fat == null) {
            fat = new DoubleFilter();
        }
        return fat;
    }

    public void setFat(DoubleFilter fat) {
        this.fat = fat;
    }

    public DoubleFilter getKcal() {
        return kcal;
    }

    public DoubleFilter kcal() {
        if (kcal == null) {
            kcal = new DoubleFilter();
        }
        return kcal;
    }

    public void setKcal(DoubleFilter kcal) {
        this.kcal = kcal;
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
        final NutriensCriteria that = (NutriensCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(protein, that.protein) &&
            Objects.equals(carb, that.carb) &&
            Objects.equals(fat, that.fat) &&
            Objects.equals(kcal, that.kcal) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(menuGroupsId, that.menuGroupsId) &&
            Objects.equals(menusId, that.menusId) &&
            Objects.equals(mealIngredientsId, that.mealIngredientsId) &&
            Objects.equals(mealsId, that.mealsId) &&
            Objects.equals(ingredientsId, that.ingredientsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            protein,
            carb,
            fat,
            kcal,
            createdDate,
            menuGroupsId,
            menusId,
            mealIngredientsId,
            mealsId,
            ingredientsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NutriensCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (protein != null ? "protein=" + protein + ", " : "") +
            (carb != null ? "carb=" + carb + ", " : "") +
            (fat != null ? "fat=" + fat + ", " : "") +
            (kcal != null ? "kcal=" + kcal + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (menuGroupsId != null ? "menuGroupsId=" + menuGroupsId + ", " : "") +
            (menusId != null ? "menusId=" + menusId + ", " : "") +
            (mealIngredientsId != null ? "mealIngredientsId=" + mealIngredientsId + ", " : "") +
            (mealsId != null ? "mealsId=" + mealsId + ", " : "") +
            (ingredientsId != null ? "ingredientsId=" + ingredientsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
