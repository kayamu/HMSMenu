package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.repository.MealIngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.MealIngredientsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MealIngredientsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealIngredientsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MealIngredientsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MealIngredientsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/meal-ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MealIngredientsRepository mealIngredientsRepository;

    @Mock
    private MealIngredientsRepository mealIngredientsRepositoryMock;

    @Autowired
    private MealIngredientsMapper mealIngredientsMapper;

    @Mock
    private MealIngredientsService mealIngredientsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealIngredientsMockMvc;

    private MealIngredients mealIngredients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealIngredients createEntity(EntityManager em) {
        MealIngredients mealIngredients = new MealIngredients()
            .name(DEFAULT_NAME)
            .amount(DEFAULT_AMOUNT)
            .unit(DEFAULT_UNIT)
            .createdDate(DEFAULT_CREATED_DATE);
        return mealIngredients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MealIngredients createUpdatedEntity(EntityManager em) {
        MealIngredients mealIngredients = new MealIngredients()
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .unit(UPDATED_UNIT)
            .createdDate(UPDATED_CREATED_DATE);
        return mealIngredients;
    }

    @BeforeEach
    public void initTest() {
        mealIngredients = createEntity(em);
    }

    @Test
    @Transactional
    void createMealIngredients() throws Exception {
        int databaseSizeBeforeCreate = mealIngredientsRepository.findAll().size();
        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);
        restMealIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeCreate + 1);
        MealIngredients testMealIngredients = mealIngredientsList.get(mealIngredientsList.size() - 1);
        assertThat(testMealIngredients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMealIngredients.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMealIngredients.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testMealIngredients.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createMealIngredientsWithExistingId() throws Exception {
        // Create the MealIngredients with an existing ID
        mealIngredients.setId(1L);
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        int databaseSizeBeforeCreate = mealIngredientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMealIngredients() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mealIngredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMealIngredientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mealIngredientsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMealIngredientsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mealIngredientsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMealIngredientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mealIngredientsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMealIngredientsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mealIngredientsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMealIngredients() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get the mealIngredients
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL_ID, mealIngredients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mealIngredients.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMealIngredientsByIdFiltering() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        Long id = mealIngredients.getId();

        defaultMealIngredientsShouldBeFound("id.equals=" + id);
        defaultMealIngredientsShouldNotBeFound("id.notEquals=" + id);

        defaultMealIngredientsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMealIngredientsShouldNotBeFound("id.greaterThan=" + id);

        defaultMealIngredientsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMealIngredientsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where name equals to DEFAULT_NAME
        defaultMealIngredientsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the mealIngredientsList where name equals to UPDATED_NAME
        defaultMealIngredientsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMealIngredientsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the mealIngredientsList where name equals to UPDATED_NAME
        defaultMealIngredientsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where name is not null
        defaultMealIngredientsShouldBeFound("name.specified=true");

        // Get all the mealIngredientsList where name is null
        defaultMealIngredientsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNameContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where name contains DEFAULT_NAME
        defaultMealIngredientsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the mealIngredientsList where name contains UPDATED_NAME
        defaultMealIngredientsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where name does not contain DEFAULT_NAME
        defaultMealIngredientsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the mealIngredientsList where name does not contain UPDATED_NAME
        defaultMealIngredientsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where amount equals to DEFAULT_AMOUNT
        defaultMealIngredientsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the mealIngredientsList where amount equals to UPDATED_AMOUNT
        defaultMealIngredientsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultMealIngredientsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the mealIngredientsList where amount equals to UPDATED_AMOUNT
        defaultMealIngredientsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where amount is not null
        defaultMealIngredientsShouldBeFound("amount.specified=true");

        // Get all the mealIngredientsList where amount is null
        defaultMealIngredientsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllMealIngredientsByAmountContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where amount contains DEFAULT_AMOUNT
        defaultMealIngredientsShouldBeFound("amount.contains=" + DEFAULT_AMOUNT);

        // Get all the mealIngredientsList where amount contains UPDATED_AMOUNT
        defaultMealIngredientsShouldNotBeFound("amount.contains=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByAmountNotContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where amount does not contain DEFAULT_AMOUNT
        defaultMealIngredientsShouldNotBeFound("amount.doesNotContain=" + DEFAULT_AMOUNT);

        // Get all the mealIngredientsList where amount does not contain UPDATED_AMOUNT
        defaultMealIngredientsShouldBeFound("amount.doesNotContain=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where unit equals to DEFAULT_UNIT
        defaultMealIngredientsShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the mealIngredientsList where unit equals to UPDATED_UNIT
        defaultMealIngredientsShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultMealIngredientsShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the mealIngredientsList where unit equals to UPDATED_UNIT
        defaultMealIngredientsShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where unit is not null
        defaultMealIngredientsShouldBeFound("unit.specified=true");

        // Get all the mealIngredientsList where unit is null
        defaultMealIngredientsShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllMealIngredientsByUnitContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where unit contains DEFAULT_UNIT
        defaultMealIngredientsShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the mealIngredientsList where unit contains UPDATED_UNIT
        defaultMealIngredientsShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where unit does not contain DEFAULT_UNIT
        defaultMealIngredientsShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the mealIngredientsList where unit does not contain UPDATED_UNIT
        defaultMealIngredientsShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate is not null
        defaultMealIngredientsShouldBeFound("createdDate.specified=true");

        // Get all the mealIngredientsList where createdDate is null
        defaultMealIngredientsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate is less than UPDATED_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        // Get all the mealIngredientsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultMealIngredientsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the mealIngredientsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultMealIngredientsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealIngredientsByNutriensIsEqualToSomething() throws Exception {
        Nutriens nutriens;
        if (TestUtil.findAll(em, Nutriens.class).isEmpty()) {
            mealIngredientsRepository.saveAndFlush(mealIngredients);
            nutriens = NutriensResourceIT.createEntity(em);
        } else {
            nutriens = TestUtil.findAll(em, Nutriens.class).get(0);
        }
        em.persist(nutriens);
        em.flush();
        mealIngredients.setNutriens(nutriens);
        mealIngredientsRepository.saveAndFlush(mealIngredients);
        Long nutriensId = nutriens.getId();

        // Get all the mealIngredientsList where nutriens equals to nutriensId
        defaultMealIngredientsShouldBeFound("nutriensId.equals=" + nutriensId);

        // Get all the mealIngredientsList where nutriens equals to (nutriensId + 1)
        defaultMealIngredientsShouldNotBeFound("nutriensId.equals=" + (nutriensId + 1));
    }

    @Test
    @Transactional
    void getAllMealIngredientsByIngradientsIsEqualToSomething() throws Exception {
        Ingredients ingradients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            mealIngredientsRepository.saveAndFlush(mealIngredients);
            ingradients = IngredientsResourceIT.createEntity(em);
        } else {
            ingradients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        em.persist(ingradients);
        em.flush();
        mealIngredients.setIngradients(ingradients);
        mealIngredientsRepository.saveAndFlush(mealIngredients);
        Long ingradientsId = ingradients.getId();

        // Get all the mealIngredientsList where ingradients equals to ingradientsId
        defaultMealIngredientsShouldBeFound("ingradientsId.equals=" + ingradientsId);

        // Get all the mealIngredientsList where ingradients equals to (ingradientsId + 1)
        defaultMealIngredientsShouldNotBeFound("ingradientsId.equals=" + (ingradientsId + 1));
    }

    @Test
    @Transactional
    void getAllMealIngredientsByMealsIsEqualToSomething() throws Exception {
        Meals meals;
        if (TestUtil.findAll(em, Meals.class).isEmpty()) {
            mealIngredientsRepository.saveAndFlush(mealIngredients);
            meals = MealsResourceIT.createEntity(em);
        } else {
            meals = TestUtil.findAll(em, Meals.class).get(0);
        }
        em.persist(meals);
        em.flush();
        mealIngredients.addMeals(meals);
        mealIngredientsRepository.saveAndFlush(mealIngredients);
        Long mealsId = meals.getId();

        // Get all the mealIngredientsList where meals equals to mealsId
        defaultMealIngredientsShouldBeFound("mealsId.equals=" + mealsId);

        // Get all the mealIngredientsList where meals equals to (mealsId + 1)
        defaultMealIngredientsShouldNotBeFound("mealsId.equals=" + (mealsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMealIngredientsShouldBeFound(String filter) throws Exception {
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mealIngredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMealIngredientsShouldNotBeFound(String filter) throws Exception {
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMealIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMealIngredients() throws Exception {
        // Get the mealIngredients
        restMealIngredientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMealIngredients() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();

        // Update the mealIngredients
        MealIngredients updatedMealIngredients = mealIngredientsRepository.findById(mealIngredients.getId()).get();
        // Disconnect from session so that the updates on updatedMealIngredients are not directly saved in db
        em.detach(updatedMealIngredients);
        updatedMealIngredients.name(UPDATED_NAME).amount(UPDATED_AMOUNT).unit(UPDATED_UNIT).createdDate(UPDATED_CREATED_DATE);
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(updatedMealIngredients);

        restMealIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealIngredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
        MealIngredients testMealIngredients = mealIngredientsList.get(mealIngredientsList.size() - 1);
        assertThat(testMealIngredients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMealIngredients.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMealIngredients.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMealIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealIngredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMealIngredientsWithPatch() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();

        // Update the mealIngredients using partial update
        MealIngredients partialUpdatedMealIngredients = new MealIngredients();
        partialUpdatedMealIngredients.setId(mealIngredients.getId());

        partialUpdatedMealIngredients.amount(UPDATED_AMOUNT).unit(UPDATED_UNIT).createdDate(UPDATED_CREATED_DATE);

        restMealIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealIngredients))
            )
            .andExpect(status().isOk());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
        MealIngredients testMealIngredients = mealIngredientsList.get(mealIngredientsList.size() - 1);
        assertThat(testMealIngredients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMealIngredients.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMealIngredients.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMealIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMealIngredientsWithPatch() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();

        // Update the mealIngredients using partial update
        MealIngredients partialUpdatedMealIngredients = new MealIngredients();
        partialUpdatedMealIngredients.setId(mealIngredients.getId());

        partialUpdatedMealIngredients.name(UPDATED_NAME).amount(UPDATED_AMOUNT).unit(UPDATED_UNIT).createdDate(UPDATED_CREATED_DATE);

        restMealIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMealIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMealIngredients))
            )
            .andExpect(status().isOk());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
        MealIngredients testMealIngredients = mealIngredientsList.get(mealIngredientsList.size() - 1);
        assertThat(testMealIngredients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMealIngredients.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMealIngredients.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMealIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mealIngredientsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMealIngredients() throws Exception {
        int databaseSizeBeforeUpdate = mealIngredientsRepository.findAll().size();
        mealIngredients.setId(count.incrementAndGet());

        // Create the MealIngredients
        MealIngredientsDTO mealIngredientsDTO = mealIngredientsMapper.toDto(mealIngredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealIngredientsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MealIngredients in the database
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMealIngredients() throws Exception {
        // Initialize the database
        mealIngredientsRepository.saveAndFlush(mealIngredients);

        int databaseSizeBeforeDelete = mealIngredientsRepository.findAll().size();

        // Delete the mealIngredients
        restMealIngredientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, mealIngredients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MealIngredients> mealIngredientsList = mealIngredientsRepository.findAll();
        assertThat(mealIngredientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
