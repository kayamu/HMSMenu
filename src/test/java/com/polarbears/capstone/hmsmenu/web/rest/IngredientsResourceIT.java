package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.repository.IngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.IngredientsService;
import com.polarbears.capstone.hmsmenu.service.criteria.IngredientsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.IngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.IngredientsMapper;
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
 * Integration tests for the {@link IngredientsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IngredientsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Mock
    private IngredientsRepository ingredientsRepositoryMock;

    @Autowired
    private IngredientsMapper ingredientsMapper;

    @Mock
    private IngredientsService ingredientsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientsMockMvc;

    private Ingredients ingredients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE);
        return ingredients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createUpdatedEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);
        return ingredients;
    }

    @BeforeEach
    public void initTest() {
        ingredients = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredients() throws Exception {
        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();
        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);
        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIngredients.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createIngredientsWithExistingId() throws Exception {
        // Create the Ingredients with an existing ID
        ingredients.setId(1L);
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIngredientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ingredientsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIngredientsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ingredientsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIngredientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ingredientsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIngredientsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ingredientsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get the ingredients
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL_ID, ingredients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingredients.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getIngredientsByIdFiltering() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        Long id = ingredients.getId();

        defaultIngredientsShouldBeFound("id.equals=" + id);
        defaultIngredientsShouldNotBeFound("id.notEquals=" + id);

        defaultIngredientsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIngredientsShouldNotBeFound("id.greaterThan=" + id);

        defaultIngredientsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIngredientsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIngredientsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where name equals to DEFAULT_NAME
        defaultIngredientsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ingredientsList where name equals to UPDATED_NAME
        defaultIngredientsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultIngredientsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ingredientsList where name equals to UPDATED_NAME
        defaultIngredientsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where name is not null
        defaultIngredientsShouldBeFound("name.specified=true");

        // Get all the ingredientsList where name is null
        defaultIngredientsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsByNameContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where name contains DEFAULT_NAME
        defaultIngredientsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ingredientsList where name contains UPDATED_NAME
        defaultIngredientsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where name does not contain DEFAULT_NAME
        defaultIngredientsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ingredientsList where name does not contain UPDATED_NAME
        defaultIngredientsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the ingredientsList where createdDate equals to UPDATED_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the ingredientsList where createdDate equals to UPDATED_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate is not null
        defaultIngredientsShouldBeFound("createdDate.specified=true");

        // Get all the ingredientsList where createdDate is null
        defaultIngredientsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the ingredientsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the ingredientsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the ingredientsList where createdDate is less than UPDATED_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultIngredientsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the ingredientsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultIngredientsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllIngredientsByMealIngredientsIsEqualToSomething() throws Exception {
        MealIngredients mealIngredients;
        if (TestUtil.findAll(em, MealIngredients.class).isEmpty()) {
            ingredientsRepository.saveAndFlush(ingredients);
            mealIngredients = MealIngredientsResourceIT.createEntity(em);
        } else {
            mealIngredients = TestUtil.findAll(em, MealIngredients.class).get(0);
        }
        em.persist(mealIngredients);
        em.flush();
        ingredients.addMealIngredients(mealIngredients);
        ingredientsRepository.saveAndFlush(ingredients);
        Long mealIngredientsId = mealIngredients.getId();

        // Get all the ingredientsList where mealIngredients equals to mealIngredientsId
        defaultIngredientsShouldBeFound("mealIngredientsId.equals=" + mealIngredientsId);

        // Get all the ingredientsList where mealIngredients equals to (mealIngredientsId + 1)
        defaultIngredientsShouldNotBeFound("mealIngredientsId.equals=" + (mealIngredientsId + 1));
    }

    @Test
    @Transactional
    void getAllIngredientsByImagesUrlsIsEqualToSomething() throws Exception {
        ImagesUrl imagesUrls;
        if (TestUtil.findAll(em, ImagesUrl.class).isEmpty()) {
            ingredientsRepository.saveAndFlush(ingredients);
            imagesUrls = ImagesUrlResourceIT.createEntity(em);
        } else {
            imagesUrls = TestUtil.findAll(em, ImagesUrl.class).get(0);
        }
        em.persist(imagesUrls);
        em.flush();
        ingredients.addImagesUrls(imagesUrls);
        ingredientsRepository.saveAndFlush(ingredients);
        Long imagesUrlsId = imagesUrls.getId();

        // Get all the ingredientsList where imagesUrls equals to imagesUrlsId
        defaultIngredientsShouldBeFound("imagesUrlsId.equals=" + imagesUrlsId);

        // Get all the ingredientsList where imagesUrls equals to (imagesUrlsId + 1)
        defaultIngredientsShouldNotBeFound("imagesUrlsId.equals=" + (imagesUrlsId + 1));
    }

    @Test
    @Transactional
    void getAllIngredientsByNutriensIsEqualToSomething() throws Exception {
        Nutriens nutriens;
        if (TestUtil.findAll(em, Nutriens.class).isEmpty()) {
            ingredientsRepository.saveAndFlush(ingredients);
            nutriens = NutriensResourceIT.createEntity(em);
        } else {
            nutriens = TestUtil.findAll(em, Nutriens.class).get(0);
        }
        em.persist(nutriens);
        em.flush();
        ingredients.setNutriens(nutriens);
        ingredientsRepository.saveAndFlush(ingredients);
        Long nutriensId = nutriens.getId();

        // Get all the ingredientsList where nutriens equals to nutriensId
        defaultIngredientsShouldBeFound("nutriensId.equals=" + nutriensId);

        // Get all the ingredientsList where nutriens equals to (nutriensId + 1)
        defaultIngredientsShouldNotBeFound("nutriensId.equals=" + (nutriensId + 1));
    }

    @Test
    @Transactional
    void getAllIngredientsByMenuGroupsIsEqualToSomething() throws Exception {
        MenuGroups menuGroups;
        if (TestUtil.findAll(em, MenuGroups.class).isEmpty()) {
            ingredientsRepository.saveAndFlush(ingredients);
            menuGroups = MenuGroupsResourceIT.createEntity(em);
        } else {
            menuGroups = TestUtil.findAll(em, MenuGroups.class).get(0);
        }
        em.persist(menuGroups);
        em.flush();
        ingredients.addMenuGroups(menuGroups);
        ingredientsRepository.saveAndFlush(ingredients);
        Long menuGroupsId = menuGroups.getId();

        // Get all the ingredientsList where menuGroups equals to menuGroupsId
        defaultIngredientsShouldBeFound("menuGroupsId.equals=" + menuGroupsId);

        // Get all the ingredientsList where menuGroups equals to (menuGroupsId + 1)
        defaultIngredientsShouldNotBeFound("menuGroupsId.equals=" + (menuGroupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIngredientsShouldBeFound(String filter) throws Exception {
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIngredientsShouldNotBeFound(String filter) throws Exception {
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIngredients() throws Exception {
        // Get the ingredients
        restIngredientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients
        Ingredients updatedIngredients = ingredientsRepository.findById(ingredients.getId()).get();
        // Disconnect from session so that the updates on updatedIngredients are not directly saved in db
        em.detach(updatedIngredients);
        updatedIngredients.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(updatedIngredients);

        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients.createdDate(UPDATED_CREATED_DATE);

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIngredients.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredientsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // Create the Ingredients
        IngredientsDTO ingredientsDTO = ingredientsMapper.toDto(ingredients);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingredientsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeDelete = ingredientsRepository.findAll().size();

        // Delete the ingredients
        restIngredientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingredients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
