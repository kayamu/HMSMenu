package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.repository.RecipiesRepository;
import com.polarbears.capstone.hmsmenu.service.RecipiesService;
import com.polarbears.capstone.hmsmenu.service.criteria.RecipiesCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.RecipiesMapper;
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
 * Integration tests for the {@link RecipiesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecipiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPE = "AAAAAAAAAA";
    private static final String UPDATED_RECIPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/recipies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecipiesRepository recipiesRepository;

    @Mock
    private RecipiesRepository recipiesRepositoryMock;

    @Autowired
    private RecipiesMapper recipiesMapper;

    @Mock
    private RecipiesService recipiesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipiesMockMvc;

    private Recipies recipies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipies createEntity(EntityManager em) {
        Recipies recipies = new Recipies()
            .name(DEFAULT_NAME)
            .recipe(DEFAULT_RECIPE)
            .explanation(DEFAULT_EXPLANATION)
            .createdDate(DEFAULT_CREATED_DATE);
        return recipies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipies createUpdatedEntity(EntityManager em) {
        Recipies recipies = new Recipies()
            .name(UPDATED_NAME)
            .recipe(UPDATED_RECIPE)
            .explanation(UPDATED_EXPLANATION)
            .createdDate(UPDATED_CREATED_DATE);
        return recipies;
    }

    @BeforeEach
    public void initTest() {
        recipies = createEntity(em);
    }

    @Test
    @Transactional
    void createRecipies() throws Exception {
        int databaseSizeBeforeCreate = recipiesRepository.findAll().size();
        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);
        restRecipiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipiesDTO)))
            .andExpect(status().isCreated());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeCreate + 1);
        Recipies testRecipies = recipiesList.get(recipiesList.size() - 1);
        assertThat(testRecipies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipies.getRecipe()).isEqualTo(DEFAULT_RECIPE);
        assertThat(testRecipies.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testRecipies.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createRecipiesWithExistingId() throws Exception {
        // Create the Recipies with an existing ID
        recipies.setId(1L);
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        int databaseSizeBeforeCreate = recipiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecipies() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recipe").value(hasItem(DEFAULT_RECIPE)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(recipiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recipiesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recipiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recipiesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecipies() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get the recipies
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL_ID, recipies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipies.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.recipe").value(DEFAULT_RECIPE))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getRecipiesByIdFiltering() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        Long id = recipies.getId();

        defaultRecipiesShouldBeFound("id.equals=" + id);
        defaultRecipiesShouldNotBeFound("id.notEquals=" + id);

        defaultRecipiesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRecipiesShouldNotBeFound("id.greaterThan=" + id);

        defaultRecipiesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRecipiesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecipiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where name equals to DEFAULT_NAME
        defaultRecipiesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the recipiesList where name equals to UPDATED_NAME
        defaultRecipiesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRecipiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRecipiesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the recipiesList where name equals to UPDATED_NAME
        defaultRecipiesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRecipiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where name is not null
        defaultRecipiesShouldBeFound("name.specified=true");

        // Get all the recipiesList where name is null
        defaultRecipiesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipiesByNameContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where name contains DEFAULT_NAME
        defaultRecipiesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the recipiesList where name contains UPDATED_NAME
        defaultRecipiesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRecipiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where name does not contain DEFAULT_NAME
        defaultRecipiesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the recipiesList where name does not contain UPDATED_NAME
        defaultRecipiesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRecipiesByRecipeIsEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where recipe equals to DEFAULT_RECIPE
        defaultRecipiesShouldBeFound("recipe.equals=" + DEFAULT_RECIPE);

        // Get all the recipiesList where recipe equals to UPDATED_RECIPE
        defaultRecipiesShouldNotBeFound("recipe.equals=" + UPDATED_RECIPE);
    }

    @Test
    @Transactional
    void getAllRecipiesByRecipeIsInShouldWork() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where recipe in DEFAULT_RECIPE or UPDATED_RECIPE
        defaultRecipiesShouldBeFound("recipe.in=" + DEFAULT_RECIPE + "," + UPDATED_RECIPE);

        // Get all the recipiesList where recipe equals to UPDATED_RECIPE
        defaultRecipiesShouldNotBeFound("recipe.in=" + UPDATED_RECIPE);
    }

    @Test
    @Transactional
    void getAllRecipiesByRecipeIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where recipe is not null
        defaultRecipiesShouldBeFound("recipe.specified=true");

        // Get all the recipiesList where recipe is null
        defaultRecipiesShouldNotBeFound("recipe.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipiesByRecipeContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where recipe contains DEFAULT_RECIPE
        defaultRecipiesShouldBeFound("recipe.contains=" + DEFAULT_RECIPE);

        // Get all the recipiesList where recipe contains UPDATED_RECIPE
        defaultRecipiesShouldNotBeFound("recipe.contains=" + UPDATED_RECIPE);
    }

    @Test
    @Transactional
    void getAllRecipiesByRecipeNotContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where recipe does not contain DEFAULT_RECIPE
        defaultRecipiesShouldNotBeFound("recipe.doesNotContain=" + DEFAULT_RECIPE);

        // Get all the recipiesList where recipe does not contain UPDATED_RECIPE
        defaultRecipiesShouldBeFound("recipe.doesNotContain=" + UPDATED_RECIPE);
    }

    @Test
    @Transactional
    void getAllRecipiesByExplanationIsEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where explanation equals to DEFAULT_EXPLANATION
        defaultRecipiesShouldBeFound("explanation.equals=" + DEFAULT_EXPLANATION);

        // Get all the recipiesList where explanation equals to UPDATED_EXPLANATION
        defaultRecipiesShouldNotBeFound("explanation.equals=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllRecipiesByExplanationIsInShouldWork() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where explanation in DEFAULT_EXPLANATION or UPDATED_EXPLANATION
        defaultRecipiesShouldBeFound("explanation.in=" + DEFAULT_EXPLANATION + "," + UPDATED_EXPLANATION);

        // Get all the recipiesList where explanation equals to UPDATED_EXPLANATION
        defaultRecipiesShouldNotBeFound("explanation.in=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllRecipiesByExplanationIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where explanation is not null
        defaultRecipiesShouldBeFound("explanation.specified=true");

        // Get all the recipiesList where explanation is null
        defaultRecipiesShouldNotBeFound("explanation.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipiesByExplanationContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where explanation contains DEFAULT_EXPLANATION
        defaultRecipiesShouldBeFound("explanation.contains=" + DEFAULT_EXPLANATION);

        // Get all the recipiesList where explanation contains UPDATED_EXPLANATION
        defaultRecipiesShouldNotBeFound("explanation.contains=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllRecipiesByExplanationNotContainsSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where explanation does not contain DEFAULT_EXPLANATION
        defaultRecipiesShouldNotBeFound("explanation.doesNotContain=" + DEFAULT_EXPLANATION);

        // Get all the recipiesList where explanation does not contain UPDATED_EXPLANATION
        defaultRecipiesShouldBeFound("explanation.doesNotContain=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate equals to DEFAULT_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the recipiesList where createdDate equals to UPDATED_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the recipiesList where createdDate equals to UPDATED_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate is not null
        defaultRecipiesShouldBeFound("createdDate.specified=true");

        // Get all the recipiesList where createdDate is null
        defaultRecipiesShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the recipiesList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the recipiesList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate is less than DEFAULT_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the recipiesList where createdDate is less than UPDATED_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        // Get all the recipiesList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultRecipiesShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the recipiesList where createdDate is greater than SMALLER_CREATED_DATE
        defaultRecipiesShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllRecipiesByMealIsEqualToSomething() throws Exception {
        Meals meal;
        if (TestUtil.findAll(em, Meals.class).isEmpty()) {
            recipiesRepository.saveAndFlush(recipies);
            meal = MealsResourceIT.createEntity(em);
        } else {
            meal = TestUtil.findAll(em, Meals.class).get(0);
        }
        em.persist(meal);
        em.flush();
        recipies.addMeal(meal);
        recipiesRepository.saveAndFlush(recipies);
        Long mealId = meal.getId();

        // Get all the recipiesList where meal equals to mealId
        defaultRecipiesShouldBeFound("mealId.equals=" + mealId);

        // Get all the recipiesList where meal equals to (mealId + 1)
        defaultRecipiesShouldNotBeFound("mealId.equals=" + (mealId + 1));
    }

    @Test
    @Transactional
    void getAllRecipiesByImagesUrlsIsEqualToSomething() throws Exception {
        ImagesUrl imagesUrls;
        if (TestUtil.findAll(em, ImagesUrl.class).isEmpty()) {
            recipiesRepository.saveAndFlush(recipies);
            imagesUrls = ImagesUrlResourceIT.createEntity(em);
        } else {
            imagesUrls = TestUtil.findAll(em, ImagesUrl.class).get(0);
        }
        em.persist(imagesUrls);
        em.flush();
        recipies.addImagesUrls(imagesUrls);
        recipiesRepository.saveAndFlush(recipies);
        Long imagesUrlsId = imagesUrls.getId();

        // Get all the recipiesList where imagesUrls equals to imagesUrlsId
        defaultRecipiesShouldBeFound("imagesUrlsId.equals=" + imagesUrlsId);

        // Get all the recipiesList where imagesUrls equals to (imagesUrlsId + 1)
        defaultRecipiesShouldNotBeFound("imagesUrlsId.equals=" + (imagesUrlsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecipiesShouldBeFound(String filter) throws Exception {
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recipe").value(hasItem(DEFAULT_RECIPE)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecipiesShouldNotBeFound(String filter) throws Exception {
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecipies() throws Exception {
        // Get the recipies
        restRecipiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipies() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();

        // Update the recipies
        Recipies updatedRecipies = recipiesRepository.findById(recipies.getId()).get();
        // Disconnect from session so that the updates on updatedRecipies are not directly saved in db
        em.detach(updatedRecipies);
        updatedRecipies.name(UPDATED_NAME).recipe(UPDATED_RECIPE).explanation(UPDATED_EXPLANATION).createdDate(UPDATED_CREATED_DATE);
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(updatedRecipies);

        restRecipiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
        Recipies testRecipies = recipiesList.get(recipiesList.size() - 1);
        assertThat(testRecipies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipies.getRecipe()).isEqualTo(UPDATED_RECIPE);
        assertThat(testRecipies.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testRecipies.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recipiesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipiesWithPatch() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();

        // Update the recipies using partial update
        Recipies partialUpdatedRecipies = new Recipies();
        partialUpdatedRecipies.setId(recipies.getId());

        partialUpdatedRecipies.name(UPDATED_NAME).recipe(UPDATED_RECIPE);

        restRecipiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipies))
            )
            .andExpect(status().isOk());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
        Recipies testRecipies = recipiesList.get(recipiesList.size() - 1);
        assertThat(testRecipies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipies.getRecipe()).isEqualTo(UPDATED_RECIPE);
        assertThat(testRecipies.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testRecipies.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateRecipiesWithPatch() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();

        // Update the recipies using partial update
        Recipies partialUpdatedRecipies = new Recipies();
        partialUpdatedRecipies.setId(recipies.getId());

        partialUpdatedRecipies.name(UPDATED_NAME).recipe(UPDATED_RECIPE).explanation(UPDATED_EXPLANATION).createdDate(UPDATED_CREATED_DATE);

        restRecipiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipies))
            )
            .andExpect(status().isOk());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
        Recipies testRecipies = recipiesList.get(recipiesList.size() - 1);
        assertThat(testRecipies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipies.getRecipe()).isEqualTo(UPDATED_RECIPE);
        assertThat(testRecipies.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testRecipies.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipiesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipies() throws Exception {
        int databaseSizeBeforeUpdate = recipiesRepository.findAll().size();
        recipies.setId(count.incrementAndGet());

        // Create the Recipies
        RecipiesDTO recipiesDTO = recipiesMapper.toDto(recipies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipiesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recipiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipies in the database
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipies() throws Exception {
        // Initialize the database
        recipiesRepository.saveAndFlush(recipies);

        int databaseSizeBeforeDelete = recipiesRepository.findAll().size();

        // Delete the recipies
        restRecipiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recipies> recipiesList = recipiesRepository.findAll();
        assertThat(recipiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
