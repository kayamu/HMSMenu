package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.domain.enumeration.IMAGETYPES;
import com.polarbears.capstone.hmsmenu.repository.ImagesUrlRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.ImagesUrlCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.ImagesUrlMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ImagesUrlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagesUrlResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_URL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final IMAGETYPES DEFAULT_TYPE = IMAGETYPES.PROMOTED;
    private static final IMAGETYPES UPDATED_TYPE = IMAGETYPES.NORMAL;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/images-urls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagesUrlRepository imagesUrlRepository;

    @Autowired
    private ImagesUrlMapper imagesUrlMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagesUrlMockMvc;

    private ImagesUrl imagesUrl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagesUrl createEntity(EntityManager em) {
        ImagesUrl imagesUrl = new ImagesUrl()
            .name(DEFAULT_NAME)
            .urlAddress(DEFAULT_URL_ADDRESS)
            .explanation(DEFAULT_EXPLANATION)
            .type(DEFAULT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE);
        return imagesUrl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagesUrl createUpdatedEntity(EntityManager em) {
        ImagesUrl imagesUrl = new ImagesUrl()
            .name(UPDATED_NAME)
            .urlAddress(UPDATED_URL_ADDRESS)
            .explanation(UPDATED_EXPLANATION)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);
        return imagesUrl;
    }

    @BeforeEach
    public void initTest() {
        imagesUrl = createEntity(em);
    }

    @Test
    @Transactional
    void createImagesUrl() throws Exception {
        int databaseSizeBeforeCreate = imagesUrlRepository.findAll().size();
        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);
        restImagesUrlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO)))
            .andExpect(status().isCreated());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeCreate + 1);
        ImagesUrl testImagesUrl = imagesUrlList.get(imagesUrlList.size() - 1);
        assertThat(testImagesUrl.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImagesUrl.getUrlAddress()).isEqualTo(DEFAULT_URL_ADDRESS);
        assertThat(testImagesUrl.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testImagesUrl.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testImagesUrl.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createImagesUrlWithExistingId() throws Exception {
        // Create the ImagesUrl with an existing ID
        imagesUrl.setId(1L);
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        int databaseSizeBeforeCreate = imagesUrlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagesUrlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImagesUrls() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagesUrl.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].urlAddress").value(hasItem(DEFAULT_URL_ADDRESS)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getImagesUrl() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get the imagesUrl
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL_ID, imagesUrl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imagesUrl.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.urlAddress").value(DEFAULT_URL_ADDRESS))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getImagesUrlsByIdFiltering() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        Long id = imagesUrl.getId();

        defaultImagesUrlShouldBeFound("id.equals=" + id);
        defaultImagesUrlShouldNotBeFound("id.notEquals=" + id);

        defaultImagesUrlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImagesUrlShouldNotBeFound("id.greaterThan=" + id);

        defaultImagesUrlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImagesUrlShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where name equals to DEFAULT_NAME
        defaultImagesUrlShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the imagesUrlList where name equals to UPDATED_NAME
        defaultImagesUrlShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where name in DEFAULT_NAME or UPDATED_NAME
        defaultImagesUrlShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the imagesUrlList where name equals to UPDATED_NAME
        defaultImagesUrlShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where name is not null
        defaultImagesUrlShouldBeFound("name.specified=true");

        // Get all the imagesUrlList where name is null
        defaultImagesUrlShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesUrlsByNameContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where name contains DEFAULT_NAME
        defaultImagesUrlShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the imagesUrlList where name contains UPDATED_NAME
        defaultImagesUrlShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where name does not contain DEFAULT_NAME
        defaultImagesUrlShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the imagesUrlList where name does not contain UPDATED_NAME
        defaultImagesUrlShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByUrlAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where urlAddress equals to DEFAULT_URL_ADDRESS
        defaultImagesUrlShouldBeFound("urlAddress.equals=" + DEFAULT_URL_ADDRESS);

        // Get all the imagesUrlList where urlAddress equals to UPDATED_URL_ADDRESS
        defaultImagesUrlShouldNotBeFound("urlAddress.equals=" + UPDATED_URL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByUrlAddressIsInShouldWork() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where urlAddress in DEFAULT_URL_ADDRESS or UPDATED_URL_ADDRESS
        defaultImagesUrlShouldBeFound("urlAddress.in=" + DEFAULT_URL_ADDRESS + "," + UPDATED_URL_ADDRESS);

        // Get all the imagesUrlList where urlAddress equals to UPDATED_URL_ADDRESS
        defaultImagesUrlShouldNotBeFound("urlAddress.in=" + UPDATED_URL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByUrlAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where urlAddress is not null
        defaultImagesUrlShouldBeFound("urlAddress.specified=true");

        // Get all the imagesUrlList where urlAddress is null
        defaultImagesUrlShouldNotBeFound("urlAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesUrlsByUrlAddressContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where urlAddress contains DEFAULT_URL_ADDRESS
        defaultImagesUrlShouldBeFound("urlAddress.contains=" + DEFAULT_URL_ADDRESS);

        // Get all the imagesUrlList where urlAddress contains UPDATED_URL_ADDRESS
        defaultImagesUrlShouldNotBeFound("urlAddress.contains=" + UPDATED_URL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByUrlAddressNotContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where urlAddress does not contain DEFAULT_URL_ADDRESS
        defaultImagesUrlShouldNotBeFound("urlAddress.doesNotContain=" + DEFAULT_URL_ADDRESS);

        // Get all the imagesUrlList where urlAddress does not contain UPDATED_URL_ADDRESS
        defaultImagesUrlShouldBeFound("urlAddress.doesNotContain=" + UPDATED_URL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByExplanationIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where explanation equals to DEFAULT_EXPLANATION
        defaultImagesUrlShouldBeFound("explanation.equals=" + DEFAULT_EXPLANATION);

        // Get all the imagesUrlList where explanation equals to UPDATED_EXPLANATION
        defaultImagesUrlShouldNotBeFound("explanation.equals=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByExplanationIsInShouldWork() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where explanation in DEFAULT_EXPLANATION or UPDATED_EXPLANATION
        defaultImagesUrlShouldBeFound("explanation.in=" + DEFAULT_EXPLANATION + "," + UPDATED_EXPLANATION);

        // Get all the imagesUrlList where explanation equals to UPDATED_EXPLANATION
        defaultImagesUrlShouldNotBeFound("explanation.in=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByExplanationIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where explanation is not null
        defaultImagesUrlShouldBeFound("explanation.specified=true");

        // Get all the imagesUrlList where explanation is null
        defaultImagesUrlShouldNotBeFound("explanation.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesUrlsByExplanationContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where explanation contains DEFAULT_EXPLANATION
        defaultImagesUrlShouldBeFound("explanation.contains=" + DEFAULT_EXPLANATION);

        // Get all the imagesUrlList where explanation contains UPDATED_EXPLANATION
        defaultImagesUrlShouldNotBeFound("explanation.contains=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByExplanationNotContainsSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where explanation does not contain DEFAULT_EXPLANATION
        defaultImagesUrlShouldNotBeFound("explanation.doesNotContain=" + DEFAULT_EXPLANATION);

        // Get all the imagesUrlList where explanation does not contain UPDATED_EXPLANATION
        defaultImagesUrlShouldBeFound("explanation.doesNotContain=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where type equals to DEFAULT_TYPE
        defaultImagesUrlShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the imagesUrlList where type equals to UPDATED_TYPE
        defaultImagesUrlShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultImagesUrlShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the imagesUrlList where type equals to UPDATED_TYPE
        defaultImagesUrlShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where type is not null
        defaultImagesUrlShouldBeFound("type.specified=true");

        // Get all the imagesUrlList where type is null
        defaultImagesUrlShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate equals to DEFAULT_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the imagesUrlList where createdDate equals to UPDATED_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the imagesUrlList where createdDate equals to UPDATED_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate is not null
        defaultImagesUrlShouldBeFound("createdDate.specified=true");

        // Get all the imagesUrlList where createdDate is null
        defaultImagesUrlShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the imagesUrlList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the imagesUrlList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate is less than DEFAULT_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the imagesUrlList where createdDate is less than UPDATED_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        // Get all the imagesUrlList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultImagesUrlShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the imagesUrlList where createdDate is greater than SMALLER_CREATED_DATE
        defaultImagesUrlShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImagesUrlsByMenuGroupsIsEqualToSomething() throws Exception {
        MenuGroups menuGroups;
        if (TestUtil.findAll(em, MenuGroups.class).isEmpty()) {
            imagesUrlRepository.saveAndFlush(imagesUrl);
            menuGroups = MenuGroupsResourceIT.createEntity(em);
        } else {
            menuGroups = TestUtil.findAll(em, MenuGroups.class).get(0);
        }
        em.persist(menuGroups);
        em.flush();
        imagesUrl.addMenuGroups(menuGroups);
        imagesUrlRepository.saveAndFlush(imagesUrl);
        Long menuGroupsId = menuGroups.getId();

        // Get all the imagesUrlList where menuGroups equals to menuGroupsId
        defaultImagesUrlShouldBeFound("menuGroupsId.equals=" + menuGroupsId);

        // Get all the imagesUrlList where menuGroups equals to (menuGroupsId + 1)
        defaultImagesUrlShouldNotBeFound("menuGroupsId.equals=" + (menuGroupsId + 1));
    }

    @Test
    @Transactional
    void getAllImagesUrlsByMenusIsEqualToSomething() throws Exception {
        Menus menus;
        if (TestUtil.findAll(em, Menus.class).isEmpty()) {
            imagesUrlRepository.saveAndFlush(imagesUrl);
            menus = MenusResourceIT.createEntity(em);
        } else {
            menus = TestUtil.findAll(em, Menus.class).get(0);
        }
        em.persist(menus);
        em.flush();
        imagesUrl.addMenus(menus);
        imagesUrlRepository.saveAndFlush(imagesUrl);
        Long menusId = menus.getId();

        // Get all the imagesUrlList where menus equals to menusId
        defaultImagesUrlShouldBeFound("menusId.equals=" + menusId);

        // Get all the imagesUrlList where menus equals to (menusId + 1)
        defaultImagesUrlShouldNotBeFound("menusId.equals=" + (menusId + 1));
    }

    @Test
    @Transactional
    void getAllImagesUrlsByMealsIsEqualToSomething() throws Exception {
        Meals meals;
        if (TestUtil.findAll(em, Meals.class).isEmpty()) {
            imagesUrlRepository.saveAndFlush(imagesUrl);
            meals = MealsResourceIT.createEntity(em);
        } else {
            meals = TestUtil.findAll(em, Meals.class).get(0);
        }
        em.persist(meals);
        em.flush();
        imagesUrl.addMeals(meals);
        imagesUrlRepository.saveAndFlush(imagesUrl);
        Long mealsId = meals.getId();

        // Get all the imagesUrlList where meals equals to mealsId
        defaultImagesUrlShouldBeFound("mealsId.equals=" + mealsId);

        // Get all the imagesUrlList where meals equals to (mealsId + 1)
        defaultImagesUrlShouldNotBeFound("mealsId.equals=" + (mealsId + 1));
    }

    @Test
    @Transactional
    void getAllImagesUrlsByIngredientsIsEqualToSomething() throws Exception {
        Ingredients ingredients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            imagesUrlRepository.saveAndFlush(imagesUrl);
            ingredients = IngredientsResourceIT.createEntity(em);
        } else {
            ingredients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        em.persist(ingredients);
        em.flush();
        imagesUrl.addIngredients(ingredients);
        imagesUrlRepository.saveAndFlush(imagesUrl);
        Long ingredientsId = ingredients.getId();

        // Get all the imagesUrlList where ingredients equals to ingredientsId
        defaultImagesUrlShouldBeFound("ingredientsId.equals=" + ingredientsId);

        // Get all the imagesUrlList where ingredients equals to (ingredientsId + 1)
        defaultImagesUrlShouldNotBeFound("ingredientsId.equals=" + (ingredientsId + 1));
    }

    @Test
    @Transactional
    void getAllImagesUrlsByRecipeIsEqualToSomething() throws Exception {
        Recipies recipe;
        if (TestUtil.findAll(em, Recipies.class).isEmpty()) {
            imagesUrlRepository.saveAndFlush(imagesUrl);
            recipe = RecipiesResourceIT.createEntity(em);
        } else {
            recipe = TestUtil.findAll(em, Recipies.class).get(0);
        }
        em.persist(recipe);
        em.flush();
        imagesUrl.addRecipe(recipe);
        imagesUrlRepository.saveAndFlush(imagesUrl);
        Long recipeId = recipe.getId();

        // Get all the imagesUrlList where recipe equals to recipeId
        defaultImagesUrlShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the imagesUrlList where recipe equals to (recipeId + 1)
        defaultImagesUrlShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagesUrlShouldBeFound(String filter) throws Exception {
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagesUrl.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].urlAddress").value(hasItem(DEFAULT_URL_ADDRESS)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagesUrlShouldNotBeFound(String filter) throws Exception {
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagesUrlMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImagesUrl() throws Exception {
        // Get the imagesUrl
        restImagesUrlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImagesUrl() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();

        // Update the imagesUrl
        ImagesUrl updatedImagesUrl = imagesUrlRepository.findById(imagesUrl.getId()).get();
        // Disconnect from session so that the updates on updatedImagesUrl are not directly saved in db
        em.detach(updatedImagesUrl);
        updatedImagesUrl
            .name(UPDATED_NAME)
            .urlAddress(UPDATED_URL_ADDRESS)
            .explanation(UPDATED_EXPLANATION)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(updatedImagesUrl);

        restImagesUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesUrlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
        ImagesUrl testImagesUrl = imagesUrlList.get(imagesUrlList.size() - 1);
        assertThat(testImagesUrl.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImagesUrl.getUrlAddress()).isEqualTo(UPDATED_URL_ADDRESS);
        assertThat(testImagesUrl.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testImagesUrl.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImagesUrl.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesUrlDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagesUrlWithPatch() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();

        // Update the imagesUrl using partial update
        ImagesUrl partialUpdatedImagesUrl = new ImagesUrl();
        partialUpdatedImagesUrl.setId(imagesUrl.getId());

        partialUpdatedImagesUrl.type(UPDATED_TYPE).createdDate(UPDATED_CREATED_DATE);

        restImagesUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagesUrl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagesUrl))
            )
            .andExpect(status().isOk());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
        ImagesUrl testImagesUrl = imagesUrlList.get(imagesUrlList.size() - 1);
        assertThat(testImagesUrl.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImagesUrl.getUrlAddress()).isEqualTo(DEFAULT_URL_ADDRESS);
        assertThat(testImagesUrl.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testImagesUrl.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImagesUrl.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateImagesUrlWithPatch() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();

        // Update the imagesUrl using partial update
        ImagesUrl partialUpdatedImagesUrl = new ImagesUrl();
        partialUpdatedImagesUrl.setId(imagesUrl.getId());

        partialUpdatedImagesUrl
            .name(UPDATED_NAME)
            .urlAddress(UPDATED_URL_ADDRESS)
            .explanation(UPDATED_EXPLANATION)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restImagesUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagesUrl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImagesUrl))
            )
            .andExpect(status().isOk());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
        ImagesUrl testImagesUrl = imagesUrlList.get(imagesUrlList.size() - 1);
        assertThat(testImagesUrl.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImagesUrl.getUrlAddress()).isEqualTo(UPDATED_URL_ADDRESS);
        assertThat(testImagesUrl.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testImagesUrl.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImagesUrl.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagesUrlDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImagesUrl() throws Exception {
        int databaseSizeBeforeUpdate = imagesUrlRepository.findAll().size();
        imagesUrl.setId(count.incrementAndGet());

        // Create the ImagesUrl
        ImagesUrlDTO imagesUrlDTO = imagesUrlMapper.toDto(imagesUrl);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesUrlMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imagesUrlDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagesUrl in the database
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImagesUrl() throws Exception {
        // Initialize the database
        imagesUrlRepository.saveAndFlush(imagesUrl);

        int databaseSizeBeforeDelete = imagesUrlRepository.findAll().size();

        // Delete the imagesUrl
        restImagesUrlMockMvc
            .perform(delete(ENTITY_API_URL_ID, imagesUrl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImagesUrl> imagesUrlList = imagesUrlRepository.findAll();
        assertThat(imagesUrlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
