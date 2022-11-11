package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.repository.MealsRepository;
import com.polarbears.capstone.hmsmenu.service.MealsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MealsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealsMapper;
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
 * Integration tests for the {@link MealsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MealsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/meals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MealsRepository mealsRepository;

    @Mock
    private MealsRepository mealsRepositoryMock;

    @Autowired
    private MealsMapper mealsMapper;

    @Mock
    private MealsService mealsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMealsMockMvc;

    private Meals meals;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meals createEntity(EntityManager em) {
        Meals meals = new Meals().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE);
        return meals;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meals createUpdatedEntity(EntityManager em) {
        Meals meals = new Meals().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);
        return meals;
    }

    @BeforeEach
    public void initTest() {
        meals = createEntity(em);
    }

    @Test
    @Transactional
    void createMeals() throws Exception {
        int databaseSizeBeforeCreate = mealsRepository.findAll().size();
        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);
        restMealsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealsDTO)))
            .andExpect(status().isCreated());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeCreate + 1);
        Meals testMeals = mealsList.get(mealsList.size() - 1);
        assertThat(testMeals.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeals.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createMealsWithExistingId() throws Exception {
        // Create the Meals with an existing ID
        meals.setId(1L);
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        int databaseSizeBeforeCreate = mealsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMealsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeals() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList
        restMealsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meals.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMealsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mealsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMealsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mealsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMealsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mealsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMealsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mealsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMeals() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get the meals
        restMealsMockMvc
            .perform(get(ENTITY_API_URL_ID, meals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meals.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMealsByIdFiltering() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        Long id = meals.getId();

        defaultMealsShouldBeFound("id.equals=" + id);
        defaultMealsShouldNotBeFound("id.notEquals=" + id);

        defaultMealsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMealsShouldNotBeFound("id.greaterThan=" + id);

        defaultMealsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMealsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMealsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where name equals to DEFAULT_NAME
        defaultMealsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the mealsList where name equals to UPDATED_NAME
        defaultMealsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMealsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the mealsList where name equals to UPDATED_NAME
        defaultMealsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where name is not null
        defaultMealsShouldBeFound("name.specified=true");

        // Get all the mealsList where name is null
        defaultMealsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMealsByNameContainsSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where name contains DEFAULT_NAME
        defaultMealsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the mealsList where name contains UPDATED_NAME
        defaultMealsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where name does not contain DEFAULT_NAME
        defaultMealsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the mealsList where name does not contain UPDATED_NAME
        defaultMealsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the mealsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the mealsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate is not null
        defaultMealsShouldBeFound("createdDate.specified=true");

        // Get all the mealsList where createdDate is null
        defaultMealsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the mealsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the mealsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the mealsList where createdDate is less than UPDATED_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        // Get all the mealsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultMealsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the mealsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultMealsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMealsByImagesUrlsIsEqualToSomething() throws Exception {
        ImagesUrl imagesUrls;
        if (TestUtil.findAll(em, ImagesUrl.class).isEmpty()) {
            mealsRepository.saveAndFlush(meals);
            imagesUrls = ImagesUrlResourceIT.createEntity(em);
        } else {
            imagesUrls = TestUtil.findAll(em, ImagesUrl.class).get(0);
        }
        em.persist(imagesUrls);
        em.flush();
        meals.addImagesUrls(imagesUrls);
        mealsRepository.saveAndFlush(meals);
        Long imagesUrlsId = imagesUrls.getId();

        // Get all the mealsList where imagesUrls equals to imagesUrlsId
        defaultMealsShouldBeFound("imagesUrlsId.equals=" + imagesUrlsId);

        // Get all the mealsList where imagesUrls equals to (imagesUrlsId + 1)
        defaultMealsShouldNotBeFound("imagesUrlsId.equals=" + (imagesUrlsId + 1));
    }

    @Test
    @Transactional
    void getAllMealsByMealIngredientsIsEqualToSomething() throws Exception {
        MealIngredients mealIngredients;
        if (TestUtil.findAll(em, MealIngredients.class).isEmpty()) {
            mealsRepository.saveAndFlush(meals);
            mealIngredients = MealIngredientsResourceIT.createEntity(em);
        } else {
            mealIngredients = TestUtil.findAll(em, MealIngredients.class).get(0);
        }
        em.persist(mealIngredients);
        em.flush();
        meals.addMealIngredients(mealIngredients);
        mealsRepository.saveAndFlush(meals);
        Long mealIngredientsId = mealIngredients.getId();

        // Get all the mealsList where mealIngredients equals to mealIngredientsId
        defaultMealsShouldBeFound("mealIngredientsId.equals=" + mealIngredientsId);

        // Get all the mealsList where mealIngredients equals to (mealIngredientsId + 1)
        defaultMealsShouldNotBeFound("mealIngredientsId.equals=" + (mealIngredientsId + 1));
    }

    @Test
    @Transactional
    void getAllMealsByNutriensIsEqualToSomething() throws Exception {
        Nutriens nutriens;
        if (TestUtil.findAll(em, Nutriens.class).isEmpty()) {
            mealsRepository.saveAndFlush(meals);
            nutriens = NutriensResourceIT.createEntity(em);
        } else {
            nutriens = TestUtil.findAll(em, Nutriens.class).get(0);
        }
        em.persist(nutriens);
        em.flush();
        meals.setNutriens(nutriens);
        mealsRepository.saveAndFlush(meals);
        Long nutriensId = nutriens.getId();

        // Get all the mealsList where nutriens equals to nutriensId
        defaultMealsShouldBeFound("nutriensId.equals=" + nutriensId);

        // Get all the mealsList where nutriens equals to (nutriensId + 1)
        defaultMealsShouldNotBeFound("nutriensId.equals=" + (nutriensId + 1));
    }

    @Test
    @Transactional
    void getAllMealsByRecipiesIsEqualToSomething() throws Exception {
        Recipies recipies;
        if (TestUtil.findAll(em, Recipies.class).isEmpty()) {
            mealsRepository.saveAndFlush(meals);
            recipies = RecipiesResourceIT.createEntity(em);
        } else {
            recipies = TestUtil.findAll(em, Recipies.class).get(0);
        }
        em.persist(recipies);
        em.flush();
        meals.setRecipies(recipies);
        mealsRepository.saveAndFlush(meals);
        Long recipiesId = recipies.getId();

        // Get all the mealsList where recipies equals to recipiesId
        defaultMealsShouldBeFound("recipiesId.equals=" + recipiesId);

        // Get all the mealsList where recipies equals to (recipiesId + 1)
        defaultMealsShouldNotBeFound("recipiesId.equals=" + (recipiesId + 1));
    }

    @Test
    @Transactional
    void getAllMealsByMenusIsEqualToSomething() throws Exception {
        Menus menus;
        if (TestUtil.findAll(em, Menus.class).isEmpty()) {
            mealsRepository.saveAndFlush(meals);
            menus = MenusResourceIT.createEntity(em);
        } else {
            menus = TestUtil.findAll(em, Menus.class).get(0);
        }
        em.persist(menus);
        em.flush();
        meals.addMenus(menus);
        mealsRepository.saveAndFlush(meals);
        Long menusId = menus.getId();

        // Get all the mealsList where menus equals to menusId
        defaultMealsShouldBeFound("menusId.equals=" + menusId);

        // Get all the mealsList where menus equals to (menusId + 1)
        defaultMealsShouldNotBeFound("menusId.equals=" + (menusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMealsShouldBeFound(String filter) throws Exception {
        restMealsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meals.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMealsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMealsShouldNotBeFound(String filter) throws Exception {
        restMealsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMealsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeals() throws Exception {
        // Get the meals
        restMealsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeals() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();

        // Update the meals
        Meals updatedMeals = mealsRepository.findById(meals.getId()).get();
        // Disconnect from session so that the updates on updatedMeals are not directly saved in db
        em.detach(updatedMeals);
        updatedMeals.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);
        MealsDTO mealsDTO = mealsMapper.toDto(updatedMeals);

        restMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
        Meals testMeals = mealsList.get(mealsList.size() - 1);
        assertThat(testMeals.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeals.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mealsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mealsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mealsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMealsWithPatch() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();

        // Update the meals using partial update
        Meals partialUpdatedMeals = new Meals();
        partialUpdatedMeals.setId(meals.getId());

        restMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeals.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeals))
            )
            .andExpect(status().isOk());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
        Meals testMeals = mealsList.get(mealsList.size() - 1);
        assertThat(testMeals.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeals.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMealsWithPatch() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();

        // Update the meals using partial update
        Meals partialUpdatedMeals = new Meals();
        partialUpdatedMeals.setId(meals.getId());

        partialUpdatedMeals.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeals.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeals))
            )
            .andExpect(status().isOk());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
        Meals testMeals = mealsList.get(mealsList.size() - 1);
        assertThat(testMeals.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeals.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mealsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mealsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeals() throws Exception {
        int databaseSizeBeforeUpdate = mealsRepository.findAll().size();
        meals.setId(count.incrementAndGet());

        // Create the Meals
        MealsDTO mealsDTO = mealsMapper.toDto(meals);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMealsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mealsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meals in the database
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeals() throws Exception {
        // Initialize the database
        mealsRepository.saveAndFlush(meals);

        int databaseSizeBeforeDelete = mealsRepository.findAll().size();

        // Delete the meals
        restMealsMockMvc
            .perform(delete(ENTITY_API_URL_ID, meals.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meals> mealsList = mealsRepository.findAll();
        assertThat(mealsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
