package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.domain.enumeration.BODYFATS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.GOALS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.UNITS;
import com.polarbears.capstone.hmsmenu.repository.MenuGroupsRepository;
import com.polarbears.capstone.hmsmenu.service.MenuGroupsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MenuGroupsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenuGroupsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenuGroupsMapper;
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
 * Integration tests for the {@link MenuGroupsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenuGroupsResourceIT {

    private static final Long DEFAULT_CONTACT_ID = 1L;
    private static final Long UPDATED_CONTACT_ID = 2L;
    private static final Long SMALLER_CONTACT_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final Double SMALLER_COST = 1D - 1D;

    private static final Double DEFAULT_SALES_PRICE = 1D;
    private static final Double UPDATED_SALES_PRICE = 2D;
    private static final Double SMALLER_SALES_PRICE = 1D - 1D;

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final GOALS DEFAULT_GOAL = GOALS.LOSEFAT;
    private static final GOALS UPDATED_GOAL = GOALS.MAINTAIN;

    private static final BODYFATS DEFAULT_BODY_TYPE = BODYFATS.LOW;
    private static final BODYFATS UPDATED_BODY_TYPE = BODYFATS.MEDIUM;

    private static final Integer DEFAULT_ACTIVITY_LEVEL_MIN = 1;
    private static final Integer UPDATED_ACTIVITY_LEVEL_MIN = 2;
    private static final Integer SMALLER_ACTIVITY_LEVEL_MIN = 1 - 1;

    private static final Integer DEFAULT_ACTIVITY_LEVEL_MAX = 1;
    private static final Integer UPDATED_ACTIVITY_LEVEL_MAX = 2;
    private static final Integer SMALLER_ACTIVITY_LEVEL_MAX = 1 - 1;

    private static final Double DEFAULT_WEIGHT_MIN = 1D;
    private static final Double UPDATED_WEIGHT_MIN = 2D;
    private static final Double SMALLER_WEIGHT_MIN = 1D - 1D;

    private static final Double DEFAULT_WEIGHT_MAX = 1D;
    private static final Double UPDATED_WEIGHT_MAX = 2D;
    private static final Double SMALLER_WEIGHT_MAX = 1D - 1D;

    private static final Double DEFAULT_DAILY_KCAL_MIN = 1D;
    private static final Double UPDATED_DAILY_KCAL_MIN = 2D;
    private static final Double SMALLER_DAILY_KCAL_MIN = 1D - 1D;

    private static final Double DEFAULT_DAILY_KCAL_MAX = 1D;
    private static final Double UPDATED_DAILY_KCAL_MAX = 2D;
    private static final Double SMALLER_DAILY_KCAL_MAX = 1D - 1D;

    private static final Double DEFAULT_TARGET_WEIGHT_MIN = 1D;
    private static final Double UPDATED_TARGET_WEIGHT_MIN = 2D;
    private static final Double SMALLER_TARGET_WEIGHT_MIN = 1D - 1D;

    private static final Double DEFAULT_TARGET_WEIGHT_MAX = 1D;
    private static final Double UPDATED_TARGET_WEIGHT_MAX = 2D;
    private static final Double SMALLER_TARGET_WEIGHT_MAX = 1D - 1D;

    private static final UNITS DEFAULT_UNIT = UNITS.KG;
    private static final UNITS UPDATED_UNIT = UNITS.LB;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/menu-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuGroupsRepository menuGroupsRepository;

    @Mock
    private MenuGroupsRepository menuGroupsRepositoryMock;

    @Autowired
    private MenuGroupsMapper menuGroupsMapper;

    @Mock
    private MenuGroupsService menuGroupsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuGroupsMockMvc;

    private MenuGroups menuGroups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuGroups createEntity(EntityManager em) {
        MenuGroups menuGroups = new MenuGroups()
            .contactId(DEFAULT_CONTACT_ID)
            .name(DEFAULT_NAME)
            .cost(DEFAULT_COST)
            .salesPrice(DEFAULT_SALES_PRICE)
            .explanation(DEFAULT_EXPLANATION)
            .goal(DEFAULT_GOAL)
            .bodyType(DEFAULT_BODY_TYPE)
            .activityLevelMin(DEFAULT_ACTIVITY_LEVEL_MIN)
            .activityLevelMax(DEFAULT_ACTIVITY_LEVEL_MAX)
            .weightMin(DEFAULT_WEIGHT_MIN)
            .weightMax(DEFAULT_WEIGHT_MAX)
            .dailyKcalMin(DEFAULT_DAILY_KCAL_MIN)
            .dailyKcalMax(DEFAULT_DAILY_KCAL_MAX)
            .targetWeightMin(DEFAULT_TARGET_WEIGHT_MIN)
            .targetWeightMax(DEFAULT_TARGET_WEIGHT_MAX)
            .unit(DEFAULT_UNIT)
            .createdDate(DEFAULT_CREATED_DATE);
        return menuGroups;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuGroups createUpdatedEntity(EntityManager em) {
        MenuGroups menuGroups = new MenuGroups()
            .contactId(UPDATED_CONTACT_ID)
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .goal(UPDATED_GOAL)
            .bodyType(UPDATED_BODY_TYPE)
            .activityLevelMin(UPDATED_ACTIVITY_LEVEL_MIN)
            .activityLevelMax(UPDATED_ACTIVITY_LEVEL_MAX)
            .weightMin(UPDATED_WEIGHT_MIN)
            .weightMax(UPDATED_WEIGHT_MAX)
            .dailyKcalMin(UPDATED_DAILY_KCAL_MIN)
            .dailyKcalMax(UPDATED_DAILY_KCAL_MAX)
            .targetWeightMin(UPDATED_TARGET_WEIGHT_MIN)
            .targetWeightMax(UPDATED_TARGET_WEIGHT_MAX)
            .unit(UPDATED_UNIT)
            .createdDate(UPDATED_CREATED_DATE);
        return menuGroups;
    }

    @BeforeEach
    public void initTest() {
        menuGroups = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuGroups() throws Exception {
        int databaseSizeBeforeCreate = menuGroupsRepository.findAll().size();
        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);
        restMenuGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        MenuGroups testMenuGroups = menuGroupsList.get(menuGroupsList.size() - 1);
        assertThat(testMenuGroups.getContactId()).isEqualTo(DEFAULT_CONTACT_ID);
        assertThat(testMenuGroups.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuGroups.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testMenuGroups.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testMenuGroups.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testMenuGroups.getGoal()).isEqualTo(DEFAULT_GOAL);
        assertThat(testMenuGroups.getBodyType()).isEqualTo(DEFAULT_BODY_TYPE);
        assertThat(testMenuGroups.getActivityLevelMin()).isEqualTo(DEFAULT_ACTIVITY_LEVEL_MIN);
        assertThat(testMenuGroups.getActivityLevelMax()).isEqualTo(DEFAULT_ACTIVITY_LEVEL_MAX);
        assertThat(testMenuGroups.getWeightMin()).isEqualTo(DEFAULT_WEIGHT_MIN);
        assertThat(testMenuGroups.getWeightMax()).isEqualTo(DEFAULT_WEIGHT_MAX);
        assertThat(testMenuGroups.getDailyKcalMin()).isEqualTo(DEFAULT_DAILY_KCAL_MIN);
        assertThat(testMenuGroups.getDailyKcalMax()).isEqualTo(DEFAULT_DAILY_KCAL_MAX);
        assertThat(testMenuGroups.getTargetWeightMin()).isEqualTo(DEFAULT_TARGET_WEIGHT_MIN);
        assertThat(testMenuGroups.getTargetWeightMax()).isEqualTo(DEFAULT_TARGET_WEIGHT_MAX);
        assertThat(testMenuGroups.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testMenuGroups.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createMenuGroupsWithExistingId() throws Exception {
        // Create the MenuGroups with an existing ID
        menuGroups.setId(1L);
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        int databaseSizeBeforeCreate = menuGroupsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenuGroups() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactId").value(hasItem(DEFAULT_CONTACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].goal").value(hasItem(DEFAULT_GOAL.toString())))
            .andExpect(jsonPath("$.[*].bodyType").value(hasItem(DEFAULT_BODY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityLevelMin").value(hasItem(DEFAULT_ACTIVITY_LEVEL_MIN)))
            .andExpect(jsonPath("$.[*].activityLevelMax").value(hasItem(DEFAULT_ACTIVITY_LEVEL_MAX)))
            .andExpect(jsonPath("$.[*].weightMin").value(hasItem(DEFAULT_WEIGHT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].weightMax").value(hasItem(DEFAULT_WEIGHT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].dailyKcalMin").value(hasItem(DEFAULT_DAILY_KCAL_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].dailyKcalMax").value(hasItem(DEFAULT_DAILY_KCAL_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].targetWeightMin").value(hasItem(DEFAULT_TARGET_WEIGHT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].targetWeightMax").value(hasItem(DEFAULT_TARGET_WEIGHT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(menuGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menuGroupsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menuGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menuGroupsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenuGroups() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get the menuGroups
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL_ID, menuGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuGroups.getId().intValue()))
            .andExpect(jsonPath("$.contactId").value(DEFAULT_CONTACT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.salesPrice").value(DEFAULT_SALES_PRICE.doubleValue()))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.goal").value(DEFAULT_GOAL.toString()))
            .andExpect(jsonPath("$.bodyType").value(DEFAULT_BODY_TYPE.toString()))
            .andExpect(jsonPath("$.activityLevelMin").value(DEFAULT_ACTIVITY_LEVEL_MIN))
            .andExpect(jsonPath("$.activityLevelMax").value(DEFAULT_ACTIVITY_LEVEL_MAX))
            .andExpect(jsonPath("$.weightMin").value(DEFAULT_WEIGHT_MIN.doubleValue()))
            .andExpect(jsonPath("$.weightMax").value(DEFAULT_WEIGHT_MAX.doubleValue()))
            .andExpect(jsonPath("$.dailyKcalMin").value(DEFAULT_DAILY_KCAL_MIN.doubleValue()))
            .andExpect(jsonPath("$.dailyKcalMax").value(DEFAULT_DAILY_KCAL_MAX.doubleValue()))
            .andExpect(jsonPath("$.targetWeightMin").value(DEFAULT_TARGET_WEIGHT_MIN.doubleValue()))
            .andExpect(jsonPath("$.targetWeightMax").value(DEFAULT_TARGET_WEIGHT_MAX.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMenuGroupsByIdFiltering() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        Long id = menuGroups.getId();

        defaultMenuGroupsShouldBeFound("id.equals=" + id);
        defaultMenuGroupsShouldNotBeFound("id.notEquals=" + id);

        defaultMenuGroupsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMenuGroupsShouldNotBeFound("id.greaterThan=" + id);

        defaultMenuGroupsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMenuGroupsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId equals to DEFAULT_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.equals=" + DEFAULT_CONTACT_ID);

        // Get all the menuGroupsList where contactId equals to UPDATED_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.equals=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId in DEFAULT_CONTACT_ID or UPDATED_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.in=" + DEFAULT_CONTACT_ID + "," + UPDATED_CONTACT_ID);

        // Get all the menuGroupsList where contactId equals to UPDATED_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.in=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId is not null
        defaultMenuGroupsShouldBeFound("contactId.specified=true");

        // Get all the menuGroupsList where contactId is null
        defaultMenuGroupsShouldNotBeFound("contactId.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId is greater than or equal to DEFAULT_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.greaterThanOrEqual=" + DEFAULT_CONTACT_ID);

        // Get all the menuGroupsList where contactId is greater than or equal to UPDATED_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.greaterThanOrEqual=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId is less than or equal to DEFAULT_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.lessThanOrEqual=" + DEFAULT_CONTACT_ID);

        // Get all the menuGroupsList where contactId is less than or equal to SMALLER_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.lessThanOrEqual=" + SMALLER_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId is less than DEFAULT_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.lessThan=" + DEFAULT_CONTACT_ID);

        // Get all the menuGroupsList where contactId is less than UPDATED_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.lessThan=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByContactIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where contactId is greater than DEFAULT_CONTACT_ID
        defaultMenuGroupsShouldNotBeFound("contactId.greaterThan=" + DEFAULT_CONTACT_ID);

        // Get all the menuGroupsList where contactId is greater than SMALLER_CONTACT_ID
        defaultMenuGroupsShouldBeFound("contactId.greaterThan=" + SMALLER_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where name equals to DEFAULT_NAME
        defaultMenuGroupsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuGroupsList where name equals to UPDATED_NAME
        defaultMenuGroupsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuGroupsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuGroupsList where name equals to UPDATED_NAME
        defaultMenuGroupsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where name is not null
        defaultMenuGroupsShouldBeFound("name.specified=true");

        // Get all the menuGroupsList where name is null
        defaultMenuGroupsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where name contains DEFAULT_NAME
        defaultMenuGroupsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the menuGroupsList where name contains UPDATED_NAME
        defaultMenuGroupsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where name does not contain DEFAULT_NAME
        defaultMenuGroupsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the menuGroupsList where name does not contain UPDATED_NAME
        defaultMenuGroupsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost equals to DEFAULT_COST
        defaultMenuGroupsShouldBeFound("cost.equals=" + DEFAULT_COST);

        // Get all the menuGroupsList where cost equals to UPDATED_COST
        defaultMenuGroupsShouldNotBeFound("cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost in DEFAULT_COST or UPDATED_COST
        defaultMenuGroupsShouldBeFound("cost.in=" + DEFAULT_COST + "," + UPDATED_COST);

        // Get all the menuGroupsList where cost equals to UPDATED_COST
        defaultMenuGroupsShouldNotBeFound("cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost is not null
        defaultMenuGroupsShouldBeFound("cost.specified=true");

        // Get all the menuGroupsList where cost is null
        defaultMenuGroupsShouldNotBeFound("cost.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost is greater than or equal to DEFAULT_COST
        defaultMenuGroupsShouldBeFound("cost.greaterThanOrEqual=" + DEFAULT_COST);

        // Get all the menuGroupsList where cost is greater than or equal to UPDATED_COST
        defaultMenuGroupsShouldNotBeFound("cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost is less than or equal to DEFAULT_COST
        defaultMenuGroupsShouldBeFound("cost.lessThanOrEqual=" + DEFAULT_COST);

        // Get all the menuGroupsList where cost is less than or equal to SMALLER_COST
        defaultMenuGroupsShouldNotBeFound("cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost is less than DEFAULT_COST
        defaultMenuGroupsShouldNotBeFound("cost.lessThan=" + DEFAULT_COST);

        // Get all the menuGroupsList where cost is less than UPDATED_COST
        defaultMenuGroupsShouldBeFound("cost.lessThan=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where cost is greater than DEFAULT_COST
        defaultMenuGroupsShouldNotBeFound("cost.greaterThan=" + DEFAULT_COST);

        // Get all the menuGroupsList where cost is greater than SMALLER_COST
        defaultMenuGroupsShouldBeFound("cost.greaterThan=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice equals to DEFAULT_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.equals=" + DEFAULT_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice equals to UPDATED_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.equals=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice in DEFAULT_SALES_PRICE or UPDATED_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.in=" + DEFAULT_SALES_PRICE + "," + UPDATED_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice equals to UPDATED_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.in=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice is not null
        defaultMenuGroupsShouldBeFound("salesPrice.specified=true");

        // Get all the menuGroupsList where salesPrice is null
        defaultMenuGroupsShouldNotBeFound("salesPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice is greater than or equal to DEFAULT_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.greaterThanOrEqual=" + DEFAULT_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice is greater than or equal to UPDATED_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.greaterThanOrEqual=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice is less than or equal to DEFAULT_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.lessThanOrEqual=" + DEFAULT_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice is less than or equal to SMALLER_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.lessThanOrEqual=" + SMALLER_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice is less than DEFAULT_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.lessThan=" + DEFAULT_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice is less than UPDATED_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.lessThan=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsBySalesPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where salesPrice is greater than DEFAULT_SALES_PRICE
        defaultMenuGroupsShouldNotBeFound("salesPrice.greaterThan=" + DEFAULT_SALES_PRICE);

        // Get all the menuGroupsList where salesPrice is greater than SMALLER_SALES_PRICE
        defaultMenuGroupsShouldBeFound("salesPrice.greaterThan=" + SMALLER_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByExplanationIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where explanation equals to DEFAULT_EXPLANATION
        defaultMenuGroupsShouldBeFound("explanation.equals=" + DEFAULT_EXPLANATION);

        // Get all the menuGroupsList where explanation equals to UPDATED_EXPLANATION
        defaultMenuGroupsShouldNotBeFound("explanation.equals=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByExplanationIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where explanation in DEFAULT_EXPLANATION or UPDATED_EXPLANATION
        defaultMenuGroupsShouldBeFound("explanation.in=" + DEFAULT_EXPLANATION + "," + UPDATED_EXPLANATION);

        // Get all the menuGroupsList where explanation equals to UPDATED_EXPLANATION
        defaultMenuGroupsShouldNotBeFound("explanation.in=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByExplanationIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where explanation is not null
        defaultMenuGroupsShouldBeFound("explanation.specified=true");

        // Get all the menuGroupsList where explanation is null
        defaultMenuGroupsShouldNotBeFound("explanation.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByExplanationContainsSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where explanation contains DEFAULT_EXPLANATION
        defaultMenuGroupsShouldBeFound("explanation.contains=" + DEFAULT_EXPLANATION);

        // Get all the menuGroupsList where explanation contains UPDATED_EXPLANATION
        defaultMenuGroupsShouldNotBeFound("explanation.contains=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByExplanationNotContainsSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where explanation does not contain DEFAULT_EXPLANATION
        defaultMenuGroupsShouldNotBeFound("explanation.doesNotContain=" + DEFAULT_EXPLANATION);

        // Get all the menuGroupsList where explanation does not contain UPDATED_EXPLANATION
        defaultMenuGroupsShouldBeFound("explanation.doesNotContain=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByGoalIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where goal equals to DEFAULT_GOAL
        defaultMenuGroupsShouldBeFound("goal.equals=" + DEFAULT_GOAL);

        // Get all the menuGroupsList where goal equals to UPDATED_GOAL
        defaultMenuGroupsShouldNotBeFound("goal.equals=" + UPDATED_GOAL);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByGoalIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where goal in DEFAULT_GOAL or UPDATED_GOAL
        defaultMenuGroupsShouldBeFound("goal.in=" + DEFAULT_GOAL + "," + UPDATED_GOAL);

        // Get all the menuGroupsList where goal equals to UPDATED_GOAL
        defaultMenuGroupsShouldNotBeFound("goal.in=" + UPDATED_GOAL);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByGoalIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where goal is not null
        defaultMenuGroupsShouldBeFound("goal.specified=true");

        // Get all the menuGroupsList where goal is null
        defaultMenuGroupsShouldNotBeFound("goal.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByBodyTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where bodyType equals to DEFAULT_BODY_TYPE
        defaultMenuGroupsShouldBeFound("bodyType.equals=" + DEFAULT_BODY_TYPE);

        // Get all the menuGroupsList where bodyType equals to UPDATED_BODY_TYPE
        defaultMenuGroupsShouldNotBeFound("bodyType.equals=" + UPDATED_BODY_TYPE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByBodyTypeIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where bodyType in DEFAULT_BODY_TYPE or UPDATED_BODY_TYPE
        defaultMenuGroupsShouldBeFound("bodyType.in=" + DEFAULT_BODY_TYPE + "," + UPDATED_BODY_TYPE);

        // Get all the menuGroupsList where bodyType equals to UPDATED_BODY_TYPE
        defaultMenuGroupsShouldNotBeFound("bodyType.in=" + UPDATED_BODY_TYPE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByBodyTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where bodyType is not null
        defaultMenuGroupsShouldBeFound("bodyType.specified=true");

        // Get all the menuGroupsList where bodyType is null
        defaultMenuGroupsShouldNotBeFound("bodyType.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin equals to DEFAULT_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldBeFound("activityLevelMin.equals=" + DEFAULT_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin equals to UPDATED_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.equals=" + UPDATED_ACTIVITY_LEVEL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin in DEFAULT_ACTIVITY_LEVEL_MIN or UPDATED_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldBeFound("activityLevelMin.in=" + DEFAULT_ACTIVITY_LEVEL_MIN + "," + UPDATED_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin equals to UPDATED_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.in=" + UPDATED_ACTIVITY_LEVEL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin is not null
        defaultMenuGroupsShouldBeFound("activityLevelMin.specified=true");

        // Get all the menuGroupsList where activityLevelMin is null
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin is greater than or equal to DEFAULT_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldBeFound("activityLevelMin.greaterThanOrEqual=" + DEFAULT_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin is greater than or equal to (DEFAULT_ACTIVITY_LEVEL_MIN + 1)
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.greaterThanOrEqual=" + (DEFAULT_ACTIVITY_LEVEL_MIN + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin is less than or equal to DEFAULT_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldBeFound("activityLevelMin.lessThanOrEqual=" + DEFAULT_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin is less than or equal to SMALLER_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.lessThanOrEqual=" + SMALLER_ACTIVITY_LEVEL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin is less than DEFAULT_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.lessThan=" + DEFAULT_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin is less than (DEFAULT_ACTIVITY_LEVEL_MIN + 1)
        defaultMenuGroupsShouldBeFound("activityLevelMin.lessThan=" + (DEFAULT_ACTIVITY_LEVEL_MIN + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMin is greater than DEFAULT_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldNotBeFound("activityLevelMin.greaterThan=" + DEFAULT_ACTIVITY_LEVEL_MIN);

        // Get all the menuGroupsList where activityLevelMin is greater than SMALLER_ACTIVITY_LEVEL_MIN
        defaultMenuGroupsShouldBeFound("activityLevelMin.greaterThan=" + SMALLER_ACTIVITY_LEVEL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax equals to DEFAULT_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldBeFound("activityLevelMax.equals=" + DEFAULT_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax equals to UPDATED_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.equals=" + UPDATED_ACTIVITY_LEVEL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax in DEFAULT_ACTIVITY_LEVEL_MAX or UPDATED_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldBeFound("activityLevelMax.in=" + DEFAULT_ACTIVITY_LEVEL_MAX + "," + UPDATED_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax equals to UPDATED_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.in=" + UPDATED_ACTIVITY_LEVEL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax is not null
        defaultMenuGroupsShouldBeFound("activityLevelMax.specified=true");

        // Get all the menuGroupsList where activityLevelMax is null
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax is greater than or equal to DEFAULT_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldBeFound("activityLevelMax.greaterThanOrEqual=" + DEFAULT_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax is greater than or equal to (DEFAULT_ACTIVITY_LEVEL_MAX + 1)
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.greaterThanOrEqual=" + (DEFAULT_ACTIVITY_LEVEL_MAX + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax is less than or equal to DEFAULT_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldBeFound("activityLevelMax.lessThanOrEqual=" + DEFAULT_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax is less than or equal to SMALLER_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.lessThanOrEqual=" + SMALLER_ACTIVITY_LEVEL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax is less than DEFAULT_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.lessThan=" + DEFAULT_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax is less than (DEFAULT_ACTIVITY_LEVEL_MAX + 1)
        defaultMenuGroupsShouldBeFound("activityLevelMax.lessThan=" + (DEFAULT_ACTIVITY_LEVEL_MAX + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByActivityLevelMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where activityLevelMax is greater than DEFAULT_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldNotBeFound("activityLevelMax.greaterThan=" + DEFAULT_ACTIVITY_LEVEL_MAX);

        // Get all the menuGroupsList where activityLevelMax is greater than SMALLER_ACTIVITY_LEVEL_MAX
        defaultMenuGroupsShouldBeFound("activityLevelMax.greaterThan=" + SMALLER_ACTIVITY_LEVEL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin equals to DEFAULT_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.equals=" + DEFAULT_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin equals to UPDATED_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.equals=" + UPDATED_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin in DEFAULT_WEIGHT_MIN or UPDATED_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.in=" + DEFAULT_WEIGHT_MIN + "," + UPDATED_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin equals to UPDATED_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.in=" + UPDATED_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin is not null
        defaultMenuGroupsShouldBeFound("weightMin.specified=true");

        // Get all the menuGroupsList where weightMin is null
        defaultMenuGroupsShouldNotBeFound("weightMin.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin is greater than or equal to DEFAULT_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.greaterThanOrEqual=" + DEFAULT_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin is greater than or equal to UPDATED_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.greaterThanOrEqual=" + UPDATED_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin is less than or equal to DEFAULT_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.lessThanOrEqual=" + DEFAULT_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin is less than or equal to SMALLER_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.lessThanOrEqual=" + SMALLER_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin is less than DEFAULT_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.lessThan=" + DEFAULT_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin is less than UPDATED_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.lessThan=" + UPDATED_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMin is greater than DEFAULT_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("weightMin.greaterThan=" + DEFAULT_WEIGHT_MIN);

        // Get all the menuGroupsList where weightMin is greater than SMALLER_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("weightMin.greaterThan=" + SMALLER_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax equals to DEFAULT_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.equals=" + DEFAULT_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax equals to UPDATED_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.equals=" + UPDATED_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax in DEFAULT_WEIGHT_MAX or UPDATED_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.in=" + DEFAULT_WEIGHT_MAX + "," + UPDATED_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax equals to UPDATED_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.in=" + UPDATED_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax is not null
        defaultMenuGroupsShouldBeFound("weightMax.specified=true");

        // Get all the menuGroupsList where weightMax is null
        defaultMenuGroupsShouldNotBeFound("weightMax.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax is greater than or equal to DEFAULT_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.greaterThanOrEqual=" + DEFAULT_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax is greater than or equal to UPDATED_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.greaterThanOrEqual=" + UPDATED_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax is less than or equal to DEFAULT_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.lessThanOrEqual=" + DEFAULT_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax is less than or equal to SMALLER_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.lessThanOrEqual=" + SMALLER_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax is less than DEFAULT_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.lessThan=" + DEFAULT_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax is less than UPDATED_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.lessThan=" + UPDATED_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByWeightMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where weightMax is greater than DEFAULT_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("weightMax.greaterThan=" + DEFAULT_WEIGHT_MAX);

        // Get all the menuGroupsList where weightMax is greater than SMALLER_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("weightMax.greaterThan=" + SMALLER_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin equals to DEFAULT_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.equals=" + DEFAULT_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin equals to UPDATED_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.equals=" + UPDATED_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin in DEFAULT_DAILY_KCAL_MIN or UPDATED_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.in=" + DEFAULT_DAILY_KCAL_MIN + "," + UPDATED_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin equals to UPDATED_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.in=" + UPDATED_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin is not null
        defaultMenuGroupsShouldBeFound("dailyKcalMin.specified=true");

        // Get all the menuGroupsList where dailyKcalMin is null
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin is greater than or equal to DEFAULT_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.greaterThanOrEqual=" + DEFAULT_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin is greater than or equal to UPDATED_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.greaterThanOrEqual=" + UPDATED_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin is less than or equal to DEFAULT_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.lessThanOrEqual=" + DEFAULT_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin is less than or equal to SMALLER_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.lessThanOrEqual=" + SMALLER_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin is less than DEFAULT_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.lessThan=" + DEFAULT_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin is less than UPDATED_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.lessThan=" + UPDATED_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMin is greater than DEFAULT_DAILY_KCAL_MIN
        defaultMenuGroupsShouldNotBeFound("dailyKcalMin.greaterThan=" + DEFAULT_DAILY_KCAL_MIN);

        // Get all the menuGroupsList where dailyKcalMin is greater than SMALLER_DAILY_KCAL_MIN
        defaultMenuGroupsShouldBeFound("dailyKcalMin.greaterThan=" + SMALLER_DAILY_KCAL_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax equals to DEFAULT_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.equals=" + DEFAULT_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax equals to UPDATED_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.equals=" + UPDATED_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax in DEFAULT_DAILY_KCAL_MAX or UPDATED_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.in=" + DEFAULT_DAILY_KCAL_MAX + "," + UPDATED_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax equals to UPDATED_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.in=" + UPDATED_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax is not null
        defaultMenuGroupsShouldBeFound("dailyKcalMax.specified=true");

        // Get all the menuGroupsList where dailyKcalMax is null
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax is greater than or equal to DEFAULT_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.greaterThanOrEqual=" + DEFAULT_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax is greater than or equal to UPDATED_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.greaterThanOrEqual=" + UPDATED_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax is less than or equal to DEFAULT_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.lessThanOrEqual=" + DEFAULT_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax is less than or equal to SMALLER_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.lessThanOrEqual=" + SMALLER_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax is less than DEFAULT_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.lessThan=" + DEFAULT_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax is less than UPDATED_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.lessThan=" + UPDATED_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByDailyKcalMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where dailyKcalMax is greater than DEFAULT_DAILY_KCAL_MAX
        defaultMenuGroupsShouldNotBeFound("dailyKcalMax.greaterThan=" + DEFAULT_DAILY_KCAL_MAX);

        // Get all the menuGroupsList where dailyKcalMax is greater than SMALLER_DAILY_KCAL_MAX
        defaultMenuGroupsShouldBeFound("dailyKcalMax.greaterThan=" + SMALLER_DAILY_KCAL_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin equals to DEFAULT_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.equals=" + DEFAULT_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin equals to UPDATED_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.equals=" + UPDATED_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin in DEFAULT_TARGET_WEIGHT_MIN or UPDATED_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.in=" + DEFAULT_TARGET_WEIGHT_MIN + "," + UPDATED_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin equals to UPDATED_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.in=" + UPDATED_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin is not null
        defaultMenuGroupsShouldBeFound("targetWeightMin.specified=true");

        // Get all the menuGroupsList where targetWeightMin is null
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin is greater than or equal to DEFAULT_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.greaterThanOrEqual=" + DEFAULT_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin is greater than or equal to UPDATED_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.greaterThanOrEqual=" + UPDATED_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin is less than or equal to DEFAULT_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.lessThanOrEqual=" + DEFAULT_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin is less than or equal to SMALLER_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.lessThanOrEqual=" + SMALLER_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin is less than DEFAULT_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.lessThan=" + DEFAULT_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin is less than UPDATED_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.lessThan=" + UPDATED_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMin is greater than DEFAULT_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldNotBeFound("targetWeightMin.greaterThan=" + DEFAULT_TARGET_WEIGHT_MIN);

        // Get all the menuGroupsList where targetWeightMin is greater than SMALLER_TARGET_WEIGHT_MIN
        defaultMenuGroupsShouldBeFound("targetWeightMin.greaterThan=" + SMALLER_TARGET_WEIGHT_MIN);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax equals to DEFAULT_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.equals=" + DEFAULT_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax equals to UPDATED_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.equals=" + UPDATED_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax in DEFAULT_TARGET_WEIGHT_MAX or UPDATED_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.in=" + DEFAULT_TARGET_WEIGHT_MAX + "," + UPDATED_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax equals to UPDATED_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.in=" + UPDATED_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax is not null
        defaultMenuGroupsShouldBeFound("targetWeightMax.specified=true");

        // Get all the menuGroupsList where targetWeightMax is null
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax is greater than or equal to DEFAULT_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.greaterThanOrEqual=" + DEFAULT_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax is greater than or equal to UPDATED_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.greaterThanOrEqual=" + UPDATED_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax is less than or equal to DEFAULT_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.lessThanOrEqual=" + DEFAULT_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax is less than or equal to SMALLER_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.lessThanOrEqual=" + SMALLER_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax is less than DEFAULT_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.lessThan=" + DEFAULT_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax is less than UPDATED_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.lessThan=" + UPDATED_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByTargetWeightMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where targetWeightMax is greater than DEFAULT_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldNotBeFound("targetWeightMax.greaterThan=" + DEFAULT_TARGET_WEIGHT_MAX);

        // Get all the menuGroupsList where targetWeightMax is greater than SMALLER_TARGET_WEIGHT_MAX
        defaultMenuGroupsShouldBeFound("targetWeightMax.greaterThan=" + SMALLER_TARGET_WEIGHT_MAX);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where unit equals to DEFAULT_UNIT
        defaultMenuGroupsShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the menuGroupsList where unit equals to UPDATED_UNIT
        defaultMenuGroupsShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultMenuGroupsShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the menuGroupsList where unit equals to UPDATED_UNIT
        defaultMenuGroupsShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where unit is not null
        defaultMenuGroupsShouldBeFound("unit.specified=true");

        // Get all the menuGroupsList where unit is null
        defaultMenuGroupsShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the menuGroupsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the menuGroupsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate is not null
        defaultMenuGroupsShouldBeFound("createdDate.specified=true");

        // Get all the menuGroupsList where createdDate is null
        defaultMenuGroupsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menuGroupsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menuGroupsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the menuGroupsList where createdDate is less than UPDATED_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        // Get all the menuGroupsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultMenuGroupsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the menuGroupsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultMenuGroupsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuGroupsByIngradientsIsEqualToSomething() throws Exception {
        Ingredients ingradients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            menuGroupsRepository.saveAndFlush(menuGroups);
            ingradients = IngredientsResourceIT.createEntity(em);
        } else {
            ingradients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        em.persist(ingradients);
        em.flush();
        menuGroups.addIngradients(ingradients);
        menuGroupsRepository.saveAndFlush(menuGroups);
        Long ingradientsId = ingradients.getId();

        // Get all the menuGroupsList where ingradients equals to ingradientsId
        defaultMenuGroupsShouldBeFound("ingradientsId.equals=" + ingradientsId);

        // Get all the menuGroupsList where ingradients equals to (ingradientsId + 1)
        defaultMenuGroupsShouldNotBeFound("ingradientsId.equals=" + (ingradientsId + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByMenusIsEqualToSomething() throws Exception {
        Menus menus;
        if (TestUtil.findAll(em, Menus.class).isEmpty()) {
            menuGroupsRepository.saveAndFlush(menuGroups);
            menus = MenusResourceIT.createEntity(em);
        } else {
            menus = TestUtil.findAll(em, Menus.class).get(0);
        }
        em.persist(menus);
        em.flush();
        menuGroups.addMenus(menus);
        menuGroupsRepository.saveAndFlush(menuGroups);
        Long menusId = menus.getId();

        // Get all the menuGroupsList where menus equals to menusId
        defaultMenuGroupsShouldBeFound("menusId.equals=" + menusId);

        // Get all the menuGroupsList where menus equals to (menusId + 1)
        defaultMenuGroupsShouldNotBeFound("menusId.equals=" + (menusId + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByImagesUrlsIsEqualToSomething() throws Exception {
        ImagesUrl imagesUrls;
        if (TestUtil.findAll(em, ImagesUrl.class).isEmpty()) {
            menuGroupsRepository.saveAndFlush(menuGroups);
            imagesUrls = ImagesUrlResourceIT.createEntity(em);
        } else {
            imagesUrls = TestUtil.findAll(em, ImagesUrl.class).get(0);
        }
        em.persist(imagesUrls);
        em.flush();
        menuGroups.addImagesUrls(imagesUrls);
        menuGroupsRepository.saveAndFlush(menuGroups);
        Long imagesUrlsId = imagesUrls.getId();

        // Get all the menuGroupsList where imagesUrls equals to imagesUrlsId
        defaultMenuGroupsShouldBeFound("imagesUrlsId.equals=" + imagesUrlsId);

        // Get all the menuGroupsList where imagesUrls equals to (imagesUrlsId + 1)
        defaultMenuGroupsShouldNotBeFound("imagesUrlsId.equals=" + (imagesUrlsId + 1));
    }

    @Test
    @Transactional
    void getAllMenuGroupsByNutriensIsEqualToSomething() throws Exception {
        Nutriens nutriens;
        if (TestUtil.findAll(em, Nutriens.class).isEmpty()) {
            menuGroupsRepository.saveAndFlush(menuGroups);
            nutriens = NutriensResourceIT.createEntity(em);
        } else {
            nutriens = TestUtil.findAll(em, Nutriens.class).get(0);
        }
        em.persist(nutriens);
        em.flush();
        menuGroups.setNutriens(nutriens);
        menuGroupsRepository.saveAndFlush(menuGroups);
        Long nutriensId = nutriens.getId();

        // Get all the menuGroupsList where nutriens equals to nutriensId
        defaultMenuGroupsShouldBeFound("nutriensId.equals=" + nutriensId);

        // Get all the menuGroupsList where nutriens equals to (nutriensId + 1)
        defaultMenuGroupsShouldNotBeFound("nutriensId.equals=" + (nutriensId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuGroupsShouldBeFound(String filter) throws Exception {
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactId").value(hasItem(DEFAULT_CONTACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].goal").value(hasItem(DEFAULT_GOAL.toString())))
            .andExpect(jsonPath("$.[*].bodyType").value(hasItem(DEFAULT_BODY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityLevelMin").value(hasItem(DEFAULT_ACTIVITY_LEVEL_MIN)))
            .andExpect(jsonPath("$.[*].activityLevelMax").value(hasItem(DEFAULT_ACTIVITY_LEVEL_MAX)))
            .andExpect(jsonPath("$.[*].weightMin").value(hasItem(DEFAULT_WEIGHT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].weightMax").value(hasItem(DEFAULT_WEIGHT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].dailyKcalMin").value(hasItem(DEFAULT_DAILY_KCAL_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].dailyKcalMax").value(hasItem(DEFAULT_DAILY_KCAL_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].targetWeightMin").value(hasItem(DEFAULT_TARGET_WEIGHT_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].targetWeightMax").value(hasItem(DEFAULT_TARGET_WEIGHT_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuGroupsShouldNotBeFound(String filter) throws Exception {
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuGroupsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenuGroups() throws Exception {
        // Get the menuGroups
        restMenuGroupsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuGroups() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();

        // Update the menuGroups
        MenuGroups updatedMenuGroups = menuGroupsRepository.findById(menuGroups.getId()).get();
        // Disconnect from session so that the updates on updatedMenuGroups are not directly saved in db
        em.detach(updatedMenuGroups);
        updatedMenuGroups
            .contactId(UPDATED_CONTACT_ID)
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .goal(UPDATED_GOAL)
            .bodyType(UPDATED_BODY_TYPE)
            .activityLevelMin(UPDATED_ACTIVITY_LEVEL_MIN)
            .activityLevelMax(UPDATED_ACTIVITY_LEVEL_MAX)
            .weightMin(UPDATED_WEIGHT_MIN)
            .weightMax(UPDATED_WEIGHT_MAX)
            .dailyKcalMin(UPDATED_DAILY_KCAL_MIN)
            .dailyKcalMax(UPDATED_DAILY_KCAL_MAX)
            .targetWeightMin(UPDATED_TARGET_WEIGHT_MIN)
            .targetWeightMax(UPDATED_TARGET_WEIGHT_MAX)
            .unit(UPDATED_UNIT)
            .createdDate(UPDATED_CREATED_DATE);
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(updatedMenuGroups);

        restMenuGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
        MenuGroups testMenuGroups = menuGroupsList.get(menuGroupsList.size() - 1);
        assertThat(testMenuGroups.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenuGroups.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuGroups.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMenuGroups.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testMenuGroups.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testMenuGroups.getGoal()).isEqualTo(UPDATED_GOAL);
        assertThat(testMenuGroups.getBodyType()).isEqualTo(UPDATED_BODY_TYPE);
        assertThat(testMenuGroups.getActivityLevelMin()).isEqualTo(UPDATED_ACTIVITY_LEVEL_MIN);
        assertThat(testMenuGroups.getActivityLevelMax()).isEqualTo(UPDATED_ACTIVITY_LEVEL_MAX);
        assertThat(testMenuGroups.getWeightMin()).isEqualTo(UPDATED_WEIGHT_MIN);
        assertThat(testMenuGroups.getWeightMax()).isEqualTo(UPDATED_WEIGHT_MAX);
        assertThat(testMenuGroups.getDailyKcalMin()).isEqualTo(UPDATED_DAILY_KCAL_MIN);
        assertThat(testMenuGroups.getDailyKcalMax()).isEqualTo(UPDATED_DAILY_KCAL_MAX);
        assertThat(testMenuGroups.getTargetWeightMin()).isEqualTo(UPDATED_TARGET_WEIGHT_MIN);
        assertThat(testMenuGroups.getTargetWeightMax()).isEqualTo(UPDATED_TARGET_WEIGHT_MAX);
        assertThat(testMenuGroups.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMenuGroups.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuGroupsWithPatch() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();

        // Update the menuGroups using partial update
        MenuGroups partialUpdatedMenuGroups = new MenuGroups();
        partialUpdatedMenuGroups.setId(menuGroups.getId());

        partialUpdatedMenuGroups
            .contactId(UPDATED_CONTACT_ID)
            .explanation(UPDATED_EXPLANATION)
            .bodyType(UPDATED_BODY_TYPE)
            .activityLevelMin(UPDATED_ACTIVITY_LEVEL_MIN)
            .weightMin(UPDATED_WEIGHT_MIN)
            .dailyKcalMin(UPDATED_DAILY_KCAL_MIN)
            .dailyKcalMax(UPDATED_DAILY_KCAL_MAX)
            .targetWeightMin(UPDATED_TARGET_WEIGHT_MIN)
            .unit(UPDATED_UNIT);

        restMenuGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuGroups))
            )
            .andExpect(status().isOk());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
        MenuGroups testMenuGroups = menuGroupsList.get(menuGroupsList.size() - 1);
        assertThat(testMenuGroups.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenuGroups.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuGroups.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testMenuGroups.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testMenuGroups.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testMenuGroups.getGoal()).isEqualTo(DEFAULT_GOAL);
        assertThat(testMenuGroups.getBodyType()).isEqualTo(UPDATED_BODY_TYPE);
        assertThat(testMenuGroups.getActivityLevelMin()).isEqualTo(UPDATED_ACTIVITY_LEVEL_MIN);
        assertThat(testMenuGroups.getActivityLevelMax()).isEqualTo(DEFAULT_ACTIVITY_LEVEL_MAX);
        assertThat(testMenuGroups.getWeightMin()).isEqualTo(UPDATED_WEIGHT_MIN);
        assertThat(testMenuGroups.getWeightMax()).isEqualTo(DEFAULT_WEIGHT_MAX);
        assertThat(testMenuGroups.getDailyKcalMin()).isEqualTo(UPDATED_DAILY_KCAL_MIN);
        assertThat(testMenuGroups.getDailyKcalMax()).isEqualTo(UPDATED_DAILY_KCAL_MAX);
        assertThat(testMenuGroups.getTargetWeightMin()).isEqualTo(UPDATED_TARGET_WEIGHT_MIN);
        assertThat(testMenuGroups.getTargetWeightMax()).isEqualTo(DEFAULT_TARGET_WEIGHT_MAX);
        assertThat(testMenuGroups.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMenuGroups.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMenuGroupsWithPatch() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();

        // Update the menuGroups using partial update
        MenuGroups partialUpdatedMenuGroups = new MenuGroups();
        partialUpdatedMenuGroups.setId(menuGroups.getId());

        partialUpdatedMenuGroups
            .contactId(UPDATED_CONTACT_ID)
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .goal(UPDATED_GOAL)
            .bodyType(UPDATED_BODY_TYPE)
            .activityLevelMin(UPDATED_ACTIVITY_LEVEL_MIN)
            .activityLevelMax(UPDATED_ACTIVITY_LEVEL_MAX)
            .weightMin(UPDATED_WEIGHT_MIN)
            .weightMax(UPDATED_WEIGHT_MAX)
            .dailyKcalMin(UPDATED_DAILY_KCAL_MIN)
            .dailyKcalMax(UPDATED_DAILY_KCAL_MAX)
            .targetWeightMin(UPDATED_TARGET_WEIGHT_MIN)
            .targetWeightMax(UPDATED_TARGET_WEIGHT_MAX)
            .unit(UPDATED_UNIT)
            .createdDate(UPDATED_CREATED_DATE);

        restMenuGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuGroups))
            )
            .andExpect(status().isOk());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
        MenuGroups testMenuGroups = menuGroupsList.get(menuGroupsList.size() - 1);
        assertThat(testMenuGroups.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenuGroups.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuGroups.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMenuGroups.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testMenuGroups.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testMenuGroups.getGoal()).isEqualTo(UPDATED_GOAL);
        assertThat(testMenuGroups.getBodyType()).isEqualTo(UPDATED_BODY_TYPE);
        assertThat(testMenuGroups.getActivityLevelMin()).isEqualTo(UPDATED_ACTIVITY_LEVEL_MIN);
        assertThat(testMenuGroups.getActivityLevelMax()).isEqualTo(UPDATED_ACTIVITY_LEVEL_MAX);
        assertThat(testMenuGroups.getWeightMin()).isEqualTo(UPDATED_WEIGHT_MIN);
        assertThat(testMenuGroups.getWeightMax()).isEqualTo(UPDATED_WEIGHT_MAX);
        assertThat(testMenuGroups.getDailyKcalMin()).isEqualTo(UPDATED_DAILY_KCAL_MIN);
        assertThat(testMenuGroups.getDailyKcalMax()).isEqualTo(UPDATED_DAILY_KCAL_MAX);
        assertThat(testMenuGroups.getTargetWeightMin()).isEqualTo(UPDATED_TARGET_WEIGHT_MIN);
        assertThat(testMenuGroups.getTargetWeightMax()).isEqualTo(UPDATED_TARGET_WEIGHT_MAX);
        assertThat(testMenuGroups.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testMenuGroups.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuGroupsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuGroups() throws Exception {
        int databaseSizeBeforeUpdate = menuGroupsRepository.findAll().size();
        menuGroups.setId(count.incrementAndGet());

        // Create the MenuGroups
        MenuGroupsDTO menuGroupsDTO = menuGroupsMapper.toDto(menuGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menuGroupsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuGroups in the database
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuGroups() throws Exception {
        // Initialize the database
        menuGroupsRepository.saveAndFlush(menuGroups);

        int databaseSizeBeforeDelete = menuGroupsRepository.findAll().size();

        // Delete the menuGroups
        restMenuGroupsMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuGroups.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuGroups> menuGroupsList = menuGroupsRepository.findAll();
        assertThat(menuGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
