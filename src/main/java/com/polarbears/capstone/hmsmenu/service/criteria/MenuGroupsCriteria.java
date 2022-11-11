package com.polarbears.capstone.hmsmenu.service.criteria;

import com.polarbears.capstone.hmsmenu.domain.enumeration.BODYFATS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.GOALS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.UNITS;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.MenuGroups} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.MenuGroupsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menu-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuGroupsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GOALS
     */
    public static class GOALSFilter extends Filter<GOALS> {

        public GOALSFilter() {}

        public GOALSFilter(GOALSFilter filter) {
            super(filter);
        }

        @Override
        public GOALSFilter copy() {
            return new GOALSFilter(this);
        }
    }

    /**
     * Class for filtering BODYFATS
     */
    public static class BODYFATSFilter extends Filter<BODYFATS> {

        public BODYFATSFilter() {}

        public BODYFATSFilter(BODYFATSFilter filter) {
            super(filter);
        }

        @Override
        public BODYFATSFilter copy() {
            return new BODYFATSFilter(this);
        }
    }

    /**
     * Class for filtering UNITS
     */
    public static class UNITSFilter extends Filter<UNITS> {

        public UNITSFilter() {}

        public UNITSFilter(UNITSFilter filter) {
            super(filter);
        }

        @Override
        public UNITSFilter copy() {
            return new UNITSFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter contactId;

    private StringFilter name;

    private DoubleFilter cost;

    private DoubleFilter salesPrice;

    private StringFilter explanation;

    private GOALSFilter goal;

    private BODYFATSFilter bodyType;

    private IntegerFilter activityLevelMin;

    private IntegerFilter activityLevelMax;

    private DoubleFilter weightMin;

    private DoubleFilter weightMax;

    private DoubleFilter dailyKcalMin;

    private DoubleFilter dailyKcalMax;

    private DoubleFilter targetWeightMin;

    private DoubleFilter targetWeightMax;

    private UNITSFilter unit;

    private LocalDateFilter createdDate;

    private LongFilter ingradientsId;

    private LongFilter menusId;

    private LongFilter imagesUrlsId;

    private LongFilter nutriensId;

    private Boolean distinct;

    public MenuGroupsCriteria() {}

    public MenuGroupsCriteria(MenuGroupsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.cost = other.cost == null ? null : other.cost.copy();
        this.salesPrice = other.salesPrice == null ? null : other.salesPrice.copy();
        this.explanation = other.explanation == null ? null : other.explanation.copy();
        this.goal = other.goal == null ? null : other.goal.copy();
        this.bodyType = other.bodyType == null ? null : other.bodyType.copy();
        this.activityLevelMin = other.activityLevelMin == null ? null : other.activityLevelMin.copy();
        this.activityLevelMax = other.activityLevelMax == null ? null : other.activityLevelMax.copy();
        this.weightMin = other.weightMin == null ? null : other.weightMin.copy();
        this.weightMax = other.weightMax == null ? null : other.weightMax.copy();
        this.dailyKcalMin = other.dailyKcalMin == null ? null : other.dailyKcalMin.copy();
        this.dailyKcalMax = other.dailyKcalMax == null ? null : other.dailyKcalMax.copy();
        this.targetWeightMin = other.targetWeightMin == null ? null : other.targetWeightMin.copy();
        this.targetWeightMax = other.targetWeightMax == null ? null : other.targetWeightMax.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.ingradientsId = other.ingradientsId == null ? null : other.ingradientsId.copy();
        this.menusId = other.menusId == null ? null : other.menusId.copy();
        this.imagesUrlsId = other.imagesUrlsId == null ? null : other.imagesUrlsId.copy();
        this.nutriensId = other.nutriensId == null ? null : other.nutriensId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MenuGroupsCriteria copy() {
        return new MenuGroupsCriteria(this);
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

    public LongFilter getContactId() {
        return contactId;
    }

    public LongFilter contactId() {
        if (contactId == null) {
            contactId = new LongFilter();
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
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

    public DoubleFilter getCost() {
        return cost;
    }

    public DoubleFilter cost() {
        if (cost == null) {
            cost = new DoubleFilter();
        }
        return cost;
    }

    public void setCost(DoubleFilter cost) {
        this.cost = cost;
    }

    public DoubleFilter getSalesPrice() {
        return salesPrice;
    }

    public DoubleFilter salesPrice() {
        if (salesPrice == null) {
            salesPrice = new DoubleFilter();
        }
        return salesPrice;
    }

    public void setSalesPrice(DoubleFilter salesPrice) {
        this.salesPrice = salesPrice;
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

    public GOALSFilter getGoal() {
        return goal;
    }

    public GOALSFilter goal() {
        if (goal == null) {
            goal = new GOALSFilter();
        }
        return goal;
    }

    public void setGoal(GOALSFilter goal) {
        this.goal = goal;
    }

    public BODYFATSFilter getBodyType() {
        return bodyType;
    }

    public BODYFATSFilter bodyType() {
        if (bodyType == null) {
            bodyType = new BODYFATSFilter();
        }
        return bodyType;
    }

    public void setBodyType(BODYFATSFilter bodyType) {
        this.bodyType = bodyType;
    }

    public IntegerFilter getActivityLevelMin() {
        return activityLevelMin;
    }

    public IntegerFilter activityLevelMin() {
        if (activityLevelMin == null) {
            activityLevelMin = new IntegerFilter();
        }
        return activityLevelMin;
    }

    public void setActivityLevelMin(IntegerFilter activityLevelMin) {
        this.activityLevelMin = activityLevelMin;
    }

    public IntegerFilter getActivityLevelMax() {
        return activityLevelMax;
    }

    public IntegerFilter activityLevelMax() {
        if (activityLevelMax == null) {
            activityLevelMax = new IntegerFilter();
        }
        return activityLevelMax;
    }

    public void setActivityLevelMax(IntegerFilter activityLevelMax) {
        this.activityLevelMax = activityLevelMax;
    }

    public DoubleFilter getWeightMin() {
        return weightMin;
    }

    public DoubleFilter weightMin() {
        if (weightMin == null) {
            weightMin = new DoubleFilter();
        }
        return weightMin;
    }

    public void setWeightMin(DoubleFilter weightMin) {
        this.weightMin = weightMin;
    }

    public DoubleFilter getWeightMax() {
        return weightMax;
    }

    public DoubleFilter weightMax() {
        if (weightMax == null) {
            weightMax = new DoubleFilter();
        }
        return weightMax;
    }

    public void setWeightMax(DoubleFilter weightMax) {
        this.weightMax = weightMax;
    }

    public DoubleFilter getDailyKcalMin() {
        return dailyKcalMin;
    }

    public DoubleFilter dailyKcalMin() {
        if (dailyKcalMin == null) {
            dailyKcalMin = new DoubleFilter();
        }
        return dailyKcalMin;
    }

    public void setDailyKcalMin(DoubleFilter dailyKcalMin) {
        this.dailyKcalMin = dailyKcalMin;
    }

    public DoubleFilter getDailyKcalMax() {
        return dailyKcalMax;
    }

    public DoubleFilter dailyKcalMax() {
        if (dailyKcalMax == null) {
            dailyKcalMax = new DoubleFilter();
        }
        return dailyKcalMax;
    }

    public void setDailyKcalMax(DoubleFilter dailyKcalMax) {
        this.dailyKcalMax = dailyKcalMax;
    }

    public DoubleFilter getTargetWeightMin() {
        return targetWeightMin;
    }

    public DoubleFilter targetWeightMin() {
        if (targetWeightMin == null) {
            targetWeightMin = new DoubleFilter();
        }
        return targetWeightMin;
    }

    public void setTargetWeightMin(DoubleFilter targetWeightMin) {
        this.targetWeightMin = targetWeightMin;
    }

    public DoubleFilter getTargetWeightMax() {
        return targetWeightMax;
    }

    public DoubleFilter targetWeightMax() {
        if (targetWeightMax == null) {
            targetWeightMax = new DoubleFilter();
        }
        return targetWeightMax;
    }

    public void setTargetWeightMax(DoubleFilter targetWeightMax) {
        this.targetWeightMax = targetWeightMax;
    }

    public UNITSFilter getUnit() {
        return unit;
    }

    public UNITSFilter unit() {
        if (unit == null) {
            unit = new UNITSFilter();
        }
        return unit;
    }

    public void setUnit(UNITSFilter unit) {
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
        final MenuGroupsCriteria that = (MenuGroupsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(salesPrice, that.salesPrice) &&
            Objects.equals(explanation, that.explanation) &&
            Objects.equals(goal, that.goal) &&
            Objects.equals(bodyType, that.bodyType) &&
            Objects.equals(activityLevelMin, that.activityLevelMin) &&
            Objects.equals(activityLevelMax, that.activityLevelMax) &&
            Objects.equals(weightMin, that.weightMin) &&
            Objects.equals(weightMax, that.weightMax) &&
            Objects.equals(dailyKcalMin, that.dailyKcalMin) &&
            Objects.equals(dailyKcalMax, that.dailyKcalMax) &&
            Objects.equals(targetWeightMin, that.targetWeightMin) &&
            Objects.equals(targetWeightMax, that.targetWeightMax) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(ingradientsId, that.ingradientsId) &&
            Objects.equals(menusId, that.menusId) &&
            Objects.equals(imagesUrlsId, that.imagesUrlsId) &&
            Objects.equals(nutriensId, that.nutriensId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            contactId,
            name,
            cost,
            salesPrice,
            explanation,
            goal,
            bodyType,
            activityLevelMin,
            activityLevelMax,
            weightMin,
            weightMax,
            dailyKcalMin,
            dailyKcalMax,
            targetWeightMin,
            targetWeightMax,
            unit,
            createdDate,
            ingradientsId,
            menusId,
            imagesUrlsId,
            nutriensId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuGroupsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (cost != null ? "cost=" + cost + ", " : "") +
            (salesPrice != null ? "salesPrice=" + salesPrice + ", " : "") +
            (explanation != null ? "explanation=" + explanation + ", " : "") +
            (goal != null ? "goal=" + goal + ", " : "") +
            (bodyType != null ? "bodyType=" + bodyType + ", " : "") +
            (activityLevelMin != null ? "activityLevelMin=" + activityLevelMin + ", " : "") +
            (activityLevelMax != null ? "activityLevelMax=" + activityLevelMax + ", " : "") +
            (weightMin != null ? "weightMin=" + weightMin + ", " : "") +
            (weightMax != null ? "weightMax=" + weightMax + ", " : "") +
            (dailyKcalMin != null ? "dailyKcalMin=" + dailyKcalMin + ", " : "") +
            (dailyKcalMax != null ? "dailyKcalMax=" + dailyKcalMax + ", " : "") +
            (targetWeightMin != null ? "targetWeightMin=" + targetWeightMin + ", " : "") +
            (targetWeightMax != null ? "targetWeightMax=" + targetWeightMax + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (ingradientsId != null ? "ingradientsId=" + ingradientsId + ", " : "") +
            (menusId != null ? "menusId=" + menusId + ", " : "") +
            (imagesUrlsId != null ? "imagesUrlsId=" + imagesUrlsId + ", " : "") +
            (nutriensId != null ? "nutriensId=" + nutriensId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
