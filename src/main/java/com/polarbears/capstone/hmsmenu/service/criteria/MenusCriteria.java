package com.polarbears.capstone.hmsmenu.service.criteria;

import com.polarbears.capstone.hmsmenu.domain.enumeration.DAYS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.REPAST;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsmenu.domain.Menus} entity. This class is used
 * in {@link com.polarbears.capstone.hmsmenu.web.rest.MenusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenusCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DAYS
     */
    public static class DAYSFilter extends Filter<DAYS> {

        public DAYSFilter() {}

        public DAYSFilter(DAYSFilter filter) {
            super(filter);
        }

        @Override
        public DAYSFilter copy() {
            return new DAYSFilter(this);
        }
    }

    /**
     * Class for filtering REPAST
     */
    public static class REPASTFilter extends Filter<REPAST> {

        public REPASTFilter() {}

        public REPASTFilter(REPASTFilter filter) {
            super(filter);
        }

        @Override
        public REPASTFilter copy() {
            return new REPASTFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DAYSFilter menuDay;

    private REPASTFilter menuTime;

    private IntegerFilter contactId;

    private DoubleFilter cost;

    private DoubleFilter salesPrice;

    private StringFilter explanation;

    private LocalDateFilter createdDate;

    private LongFilter imagesUrlsId;

    private LongFilter mealsId;

    private LongFilter nutriensId;

    private LongFilter menuGroupsId;

    private Boolean distinct;

    public MenusCriteria() {}

    public MenusCriteria(MenusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.menuDay = other.menuDay == null ? null : other.menuDay.copy();
        this.menuTime = other.menuTime == null ? null : other.menuTime.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.cost = other.cost == null ? null : other.cost.copy();
        this.salesPrice = other.salesPrice == null ? null : other.salesPrice.copy();
        this.explanation = other.explanation == null ? null : other.explanation.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.imagesUrlsId = other.imagesUrlsId == null ? null : other.imagesUrlsId.copy();
        this.mealsId = other.mealsId == null ? null : other.mealsId.copy();
        this.nutriensId = other.nutriensId == null ? null : other.nutriensId.copy();
        this.menuGroupsId = other.menuGroupsId == null ? null : other.menuGroupsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MenusCriteria copy() {
        return new MenusCriteria(this);
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

    public DAYSFilter getMenuDay() {
        return menuDay;
    }

    public DAYSFilter menuDay() {
        if (menuDay == null) {
            menuDay = new DAYSFilter();
        }
        return menuDay;
    }

    public void setMenuDay(DAYSFilter menuDay) {
        this.menuDay = menuDay;
    }

    public REPASTFilter getMenuTime() {
        return menuTime;
    }

    public REPASTFilter menuTime() {
        if (menuTime == null) {
            menuTime = new REPASTFilter();
        }
        return menuTime;
    }

    public void setMenuTime(REPASTFilter menuTime) {
        this.menuTime = menuTime;
    }

    public IntegerFilter getContactId() {
        return contactId;
    }

    public IntegerFilter contactId() {
        if (contactId == null) {
            contactId = new IntegerFilter();
        }
        return contactId;
    }

    public void setContactId(IntegerFilter contactId) {
        this.contactId = contactId;
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
        final MenusCriteria that = (MenusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(menuDay, that.menuDay) &&
            Objects.equals(menuTime, that.menuTime) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(salesPrice, that.salesPrice) &&
            Objects.equals(explanation, that.explanation) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(imagesUrlsId, that.imagesUrlsId) &&
            Objects.equals(mealsId, that.mealsId) &&
            Objects.equals(nutriensId, that.nutriensId) &&
            Objects.equals(menuGroupsId, that.menuGroupsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            menuDay,
            menuTime,
            contactId,
            cost,
            salesPrice,
            explanation,
            createdDate,
            imagesUrlsId,
            mealsId,
            nutriensId,
            menuGroupsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (menuDay != null ? "menuDay=" + menuDay + ", " : "") +
            (menuTime != null ? "menuTime=" + menuTime + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (cost != null ? "cost=" + cost + ", " : "") +
            (salesPrice != null ? "salesPrice=" + salesPrice + ", " : "") +
            (explanation != null ? "explanation=" + explanation + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (imagesUrlsId != null ? "imagesUrlsId=" + imagesUrlsId + ", " : "") +
            (mealsId != null ? "mealsId=" + mealsId + ", " : "") +
            (nutriensId != null ? "nutriensId=" + nutriensId + ", " : "") +
            (menuGroupsId != null ? "menuGroupsId=" + menuGroupsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
