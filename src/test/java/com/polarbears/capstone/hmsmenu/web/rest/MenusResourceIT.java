package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.domain.enumeration.DAYS;
import com.polarbears.capstone.hmsmenu.domain.enumeration.REPAST;
import com.polarbears.capstone.hmsmenu.repository.MenusRepository;
import com.polarbears.capstone.hmsmenu.service.MenusService;
import com.polarbears.capstone.hmsmenu.service.criteria.MenusCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenusMapper;
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
 * Integration tests for the {@link MenusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DAYS DEFAULT_MENU_DAY = DAYS.SUNDAY;
    private static final DAYS UPDATED_MENU_DAY = DAYS.MONDAY;

    private static final REPAST DEFAULT_MENU_TIME = REPAST.BREAKFAST;
    private static final REPAST UPDATED_MENU_TIME = REPAST.LUNCH;

    private static final Integer DEFAULT_CONTACT_ID = 1;
    private static final Integer UPDATED_CONTACT_ID = 2;
    private static final Integer SMALLER_CONTACT_ID = 1 - 1;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final Double SMALLER_COST = 1D - 1D;

    private static final Double DEFAULT_SALES_PRICE = 1D;
    private static final Double UPDATED_SALES_PRICE = 2D;
    private static final Double SMALLER_SALES_PRICE = 1D - 1D;

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenusRepository menusRepository;

    @Mock
    private MenusRepository menusRepositoryMock;

    @Autowired
    private MenusMapper menusMapper;

    @Mock
    private MenusService menusServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenusMockMvc;

    private Menus menus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menus createEntity(EntityManager em) {
        Menus menus = new Menus()
            .name(DEFAULT_NAME)
            .menuDay(DEFAULT_MENU_DAY)
            .menuTime(DEFAULT_MENU_TIME)
            .contactId(DEFAULT_CONTACT_ID)
            .cost(DEFAULT_COST)
            .salesPrice(DEFAULT_SALES_PRICE)
            .explanation(DEFAULT_EXPLANATION)
            .createdDate(DEFAULT_CREATED_DATE);
        return menus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menus createUpdatedEntity(EntityManager em) {
        Menus menus = new Menus()
            .name(UPDATED_NAME)
            .menuDay(UPDATED_MENU_DAY)
            .menuTime(UPDATED_MENU_TIME)
            .contactId(UPDATED_CONTACT_ID)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .createdDate(UPDATED_CREATED_DATE);
        return menus;
    }

    @BeforeEach
    public void initTest() {
        menus = createEntity(em);
    }

    @Test
    @Transactional
    void createMenus() throws Exception {
        int databaseSizeBeforeCreate = menusRepository.findAll().size();
        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);
        restMenusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menusDTO)))
            .andExpect(status().isCreated());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeCreate + 1);
        Menus testMenus = menusList.get(menusList.size() - 1);
        assertThat(testMenus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenus.getMenuDay()).isEqualTo(DEFAULT_MENU_DAY);
        assertThat(testMenus.getMenuTime()).isEqualTo(DEFAULT_MENU_TIME);
        assertThat(testMenus.getContactId()).isEqualTo(DEFAULT_CONTACT_ID);
        assertThat(testMenus.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testMenus.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testMenus.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testMenus.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createMenusWithExistingId() throws Exception {
        // Create the Menus with an existing ID
        menus.setId(1L);
        MenusDTO menusDTO = menusMapper.toDto(menus);

        int databaseSizeBeforeCreate = menusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList
        restMenusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].menuDay").value(hasItem(DEFAULT_MENU_DAY.toString())))
            .andExpect(jsonPath("$.[*].menuTime").value(hasItem(DEFAULT_MENU_TIME.toString())))
            .andExpect(jsonPath("$.[*].contactId").value(hasItem(DEFAULT_CONTACT_ID)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsEnabled() throws Exception {
        when(menusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menusRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenus() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get the menus
        restMenusMockMvc
            .perform(get(ENTITY_API_URL_ID, menus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.menuDay").value(DEFAULT_MENU_DAY.toString()))
            .andExpect(jsonPath("$.menuTime").value(DEFAULT_MENU_TIME.toString()))
            .andExpect(jsonPath("$.contactId").value(DEFAULT_CONTACT_ID))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.salesPrice").value(DEFAULT_SALES_PRICE.doubleValue()))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMenusByIdFiltering() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        Long id = menus.getId();

        defaultMenusShouldBeFound("id.equals=" + id);
        defaultMenusShouldNotBeFound("id.notEquals=" + id);

        defaultMenusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMenusShouldNotBeFound("id.greaterThan=" + id);

        defaultMenusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMenusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where name equals to DEFAULT_NAME
        defaultMenusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menusList where name equals to UPDATED_NAME
        defaultMenusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menusList where name equals to UPDATED_NAME
        defaultMenusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where name is not null
        defaultMenusShouldBeFound("name.specified=true");

        // Get all the menusList where name is null
        defaultMenusShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByNameContainsSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where name contains DEFAULT_NAME
        defaultMenusShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the menusList where name contains UPDATED_NAME
        defaultMenusShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByNameNotContainsSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where name does not contain DEFAULT_NAME
        defaultMenusShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the menusList where name does not contain UPDATED_NAME
        defaultMenusShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenusByMenuDayIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuDay equals to DEFAULT_MENU_DAY
        defaultMenusShouldBeFound("menuDay.equals=" + DEFAULT_MENU_DAY);

        // Get all the menusList where menuDay equals to UPDATED_MENU_DAY
        defaultMenusShouldNotBeFound("menuDay.equals=" + UPDATED_MENU_DAY);
    }

    @Test
    @Transactional
    void getAllMenusByMenuDayIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuDay in DEFAULT_MENU_DAY or UPDATED_MENU_DAY
        defaultMenusShouldBeFound("menuDay.in=" + DEFAULT_MENU_DAY + "," + UPDATED_MENU_DAY);

        // Get all the menusList where menuDay equals to UPDATED_MENU_DAY
        defaultMenusShouldNotBeFound("menuDay.in=" + UPDATED_MENU_DAY);
    }

    @Test
    @Transactional
    void getAllMenusByMenuDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuDay is not null
        defaultMenusShouldBeFound("menuDay.specified=true");

        // Get all the menusList where menuDay is null
        defaultMenusShouldNotBeFound("menuDay.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByMenuTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuTime equals to DEFAULT_MENU_TIME
        defaultMenusShouldBeFound("menuTime.equals=" + DEFAULT_MENU_TIME);

        // Get all the menusList where menuTime equals to UPDATED_MENU_TIME
        defaultMenusShouldNotBeFound("menuTime.equals=" + UPDATED_MENU_TIME);
    }

    @Test
    @Transactional
    void getAllMenusByMenuTimeIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuTime in DEFAULT_MENU_TIME or UPDATED_MENU_TIME
        defaultMenusShouldBeFound("menuTime.in=" + DEFAULT_MENU_TIME + "," + UPDATED_MENU_TIME);

        // Get all the menusList where menuTime equals to UPDATED_MENU_TIME
        defaultMenusShouldNotBeFound("menuTime.in=" + UPDATED_MENU_TIME);
    }

    @Test
    @Transactional
    void getAllMenusByMenuTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where menuTime is not null
        defaultMenusShouldBeFound("menuTime.specified=true");

        // Get all the menusList where menuTime is null
        defaultMenusShouldNotBeFound("menuTime.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId equals to DEFAULT_CONTACT_ID
        defaultMenusShouldBeFound("contactId.equals=" + DEFAULT_CONTACT_ID);

        // Get all the menusList where contactId equals to UPDATED_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.equals=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId in DEFAULT_CONTACT_ID or UPDATED_CONTACT_ID
        defaultMenusShouldBeFound("contactId.in=" + DEFAULT_CONTACT_ID + "," + UPDATED_CONTACT_ID);

        // Get all the menusList where contactId equals to UPDATED_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.in=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId is not null
        defaultMenusShouldBeFound("contactId.specified=true");

        // Get all the menusList where contactId is null
        defaultMenusShouldNotBeFound("contactId.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId is greater than or equal to DEFAULT_CONTACT_ID
        defaultMenusShouldBeFound("contactId.greaterThanOrEqual=" + DEFAULT_CONTACT_ID);

        // Get all the menusList where contactId is greater than or equal to UPDATED_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.greaterThanOrEqual=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId is less than or equal to DEFAULT_CONTACT_ID
        defaultMenusShouldBeFound("contactId.lessThanOrEqual=" + DEFAULT_CONTACT_ID);

        // Get all the menusList where contactId is less than or equal to SMALLER_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.lessThanOrEqual=" + SMALLER_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsLessThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId is less than DEFAULT_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.lessThan=" + DEFAULT_CONTACT_ID);

        // Get all the menusList where contactId is less than UPDATED_CONTACT_ID
        defaultMenusShouldBeFound("contactId.lessThan=" + UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByContactIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where contactId is greater than DEFAULT_CONTACT_ID
        defaultMenusShouldNotBeFound("contactId.greaterThan=" + DEFAULT_CONTACT_ID);

        // Get all the menusList where contactId is greater than SMALLER_CONTACT_ID
        defaultMenusShouldBeFound("contactId.greaterThan=" + SMALLER_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost equals to DEFAULT_COST
        defaultMenusShouldBeFound("cost.equals=" + DEFAULT_COST);

        // Get all the menusList where cost equals to UPDATED_COST
        defaultMenusShouldNotBeFound("cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost in DEFAULT_COST or UPDATED_COST
        defaultMenusShouldBeFound("cost.in=" + DEFAULT_COST + "," + UPDATED_COST);

        // Get all the menusList where cost equals to UPDATED_COST
        defaultMenusShouldNotBeFound("cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost is not null
        defaultMenusShouldBeFound("cost.specified=true");

        // Get all the menusList where cost is null
        defaultMenusShouldNotBeFound("cost.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost is greater than or equal to DEFAULT_COST
        defaultMenusShouldBeFound("cost.greaterThanOrEqual=" + DEFAULT_COST);

        // Get all the menusList where cost is greater than or equal to UPDATED_COST
        defaultMenusShouldNotBeFound("cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost is less than or equal to DEFAULT_COST
        defaultMenusShouldBeFound("cost.lessThanOrEqual=" + DEFAULT_COST);

        // Get all the menusList where cost is less than or equal to SMALLER_COST
        defaultMenusShouldNotBeFound("cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost is less than DEFAULT_COST
        defaultMenusShouldNotBeFound("cost.lessThan=" + DEFAULT_COST);

        // Get all the menusList where cost is less than UPDATED_COST
        defaultMenusShouldBeFound("cost.lessThan=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMenusByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where cost is greater than DEFAULT_COST
        defaultMenusShouldNotBeFound("cost.greaterThan=" + DEFAULT_COST);

        // Get all the menusList where cost is greater than SMALLER_COST
        defaultMenusShouldBeFound("cost.greaterThan=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice equals to DEFAULT_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.equals=" + DEFAULT_SALES_PRICE);

        // Get all the menusList where salesPrice equals to UPDATED_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.equals=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice in DEFAULT_SALES_PRICE or UPDATED_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.in=" + DEFAULT_SALES_PRICE + "," + UPDATED_SALES_PRICE);

        // Get all the menusList where salesPrice equals to UPDATED_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.in=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice is not null
        defaultMenusShouldBeFound("salesPrice.specified=true");

        // Get all the menusList where salesPrice is null
        defaultMenusShouldNotBeFound("salesPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice is greater than or equal to DEFAULT_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.greaterThanOrEqual=" + DEFAULT_SALES_PRICE);

        // Get all the menusList where salesPrice is greater than or equal to UPDATED_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.greaterThanOrEqual=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice is less than or equal to DEFAULT_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.lessThanOrEqual=" + DEFAULT_SALES_PRICE);

        // Get all the menusList where salesPrice is less than or equal to SMALLER_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.lessThanOrEqual=" + SMALLER_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice is less than DEFAULT_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.lessThan=" + DEFAULT_SALES_PRICE);

        // Get all the menusList where salesPrice is less than UPDATED_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.lessThan=" + UPDATED_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusBySalesPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where salesPrice is greater than DEFAULT_SALES_PRICE
        defaultMenusShouldNotBeFound("salesPrice.greaterThan=" + DEFAULT_SALES_PRICE);

        // Get all the menusList where salesPrice is greater than SMALLER_SALES_PRICE
        defaultMenusShouldBeFound("salesPrice.greaterThan=" + SMALLER_SALES_PRICE);
    }

    @Test
    @Transactional
    void getAllMenusByExplanationIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where explanation equals to DEFAULT_EXPLANATION
        defaultMenusShouldBeFound("explanation.equals=" + DEFAULT_EXPLANATION);

        // Get all the menusList where explanation equals to UPDATED_EXPLANATION
        defaultMenusShouldNotBeFound("explanation.equals=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenusByExplanationIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where explanation in DEFAULT_EXPLANATION or UPDATED_EXPLANATION
        defaultMenusShouldBeFound("explanation.in=" + DEFAULT_EXPLANATION + "," + UPDATED_EXPLANATION);

        // Get all the menusList where explanation equals to UPDATED_EXPLANATION
        defaultMenusShouldNotBeFound("explanation.in=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenusByExplanationIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where explanation is not null
        defaultMenusShouldBeFound("explanation.specified=true");

        // Get all the menusList where explanation is null
        defaultMenusShouldNotBeFound("explanation.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByExplanationContainsSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where explanation contains DEFAULT_EXPLANATION
        defaultMenusShouldBeFound("explanation.contains=" + DEFAULT_EXPLANATION);

        // Get all the menusList where explanation contains UPDATED_EXPLANATION
        defaultMenusShouldNotBeFound("explanation.contains=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenusByExplanationNotContainsSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where explanation does not contain DEFAULT_EXPLANATION
        defaultMenusShouldNotBeFound("explanation.doesNotContain=" + DEFAULT_EXPLANATION);

        // Get all the menusList where explanation does not contain UPDATED_EXPLANATION
        defaultMenusShouldBeFound("explanation.doesNotContain=" + UPDATED_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the menusList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the menusList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate is not null
        defaultMenusShouldBeFound("createdDate.specified=true");

        // Get all the menusList where createdDate is null
        defaultMenusShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menusList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menusList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate is less than DEFAULT_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the menusList where createdDate is less than UPDATED_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        // Get all the menusList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultMenusShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the menusList where createdDate is greater than SMALLER_CREATED_DATE
        defaultMenusShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenusByImagesUrlsIsEqualToSomething() throws Exception {
        ImagesUrl imagesUrls;
        if (TestUtil.findAll(em, ImagesUrl.class).isEmpty()) {
            menusRepository.saveAndFlush(menus);
            imagesUrls = ImagesUrlResourceIT.createEntity(em);
        } else {
            imagesUrls = TestUtil.findAll(em, ImagesUrl.class).get(0);
        }
        em.persist(imagesUrls);
        em.flush();
        menus.addImagesUrls(imagesUrls);
        menusRepository.saveAndFlush(menus);
        Long imagesUrlsId = imagesUrls.getId();

        // Get all the menusList where imagesUrls equals to imagesUrlsId
        defaultMenusShouldBeFound("imagesUrlsId.equals=" + imagesUrlsId);

        // Get all the menusList where imagesUrls equals to (imagesUrlsId + 1)
        defaultMenusShouldNotBeFound("imagesUrlsId.equals=" + (imagesUrlsId + 1));
    }

    @Test
    @Transactional
    void getAllMenusByMealsIsEqualToSomething() throws Exception {
        Meals meals;
        if (TestUtil.findAll(em, Meals.class).isEmpty()) {
            menusRepository.saveAndFlush(menus);
            meals = MealsResourceIT.createEntity(em);
        } else {
            meals = TestUtil.findAll(em, Meals.class).get(0);
        }
        em.persist(meals);
        em.flush();
        menus.addMeals(meals);
        menusRepository.saveAndFlush(menus);
        Long mealsId = meals.getId();

        // Get all the menusList where meals equals to mealsId
        defaultMenusShouldBeFound("mealsId.equals=" + mealsId);

        // Get all the menusList where meals equals to (mealsId + 1)
        defaultMenusShouldNotBeFound("mealsId.equals=" + (mealsId + 1));
    }

    @Test
    @Transactional
    void getAllMenusByNutriensIsEqualToSomething() throws Exception {
        Nutriens nutriens;
        if (TestUtil.findAll(em, Nutriens.class).isEmpty()) {
            menusRepository.saveAndFlush(menus);
            nutriens = NutriensResourceIT.createEntity(em);
        } else {
            nutriens = TestUtil.findAll(em, Nutriens.class).get(0);
        }
        em.persist(nutriens);
        em.flush();
        menus.setNutriens(nutriens);
        menusRepository.saveAndFlush(menus);
        Long nutriensId = nutriens.getId();

        // Get all the menusList where nutriens equals to nutriensId
        defaultMenusShouldBeFound("nutriensId.equals=" + nutriensId);

        // Get all the menusList where nutriens equals to (nutriensId + 1)
        defaultMenusShouldNotBeFound("nutriensId.equals=" + (nutriensId + 1));
    }

    @Test
    @Transactional
    void getAllMenusByMenuGroupsIsEqualToSomething() throws Exception {
        MenuGroups menuGroups;
        if (TestUtil.findAll(em, MenuGroups.class).isEmpty()) {
            menusRepository.saveAndFlush(menus);
            menuGroups = MenuGroupsResourceIT.createEntity(em);
        } else {
            menuGroups = TestUtil.findAll(em, MenuGroups.class).get(0);
        }
        em.persist(menuGroups);
        em.flush();
        menus.addMenuGroups(menuGroups);
        menusRepository.saveAndFlush(menus);
        Long menuGroupsId = menuGroups.getId();

        // Get all the menusList where menuGroups equals to menuGroupsId
        defaultMenusShouldBeFound("menuGroupsId.equals=" + menuGroupsId);

        // Get all the menusList where menuGroups equals to (menuGroupsId + 1)
        defaultMenusShouldNotBeFound("menuGroupsId.equals=" + (menuGroupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenusShouldBeFound(String filter) throws Exception {
        restMenusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].menuDay").value(hasItem(DEFAULT_MENU_DAY.toString())))
            .andExpect(jsonPath("$.[*].menuTime").value(hasItem(DEFAULT_MENU_TIME.toString())))
            .andExpect(jsonPath("$.[*].contactId").value(hasItem(DEFAULT_CONTACT_ID)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].salesPrice").value(hasItem(DEFAULT_SALES_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMenusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenusShouldNotBeFound(String filter) throws Exception {
        restMenusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenus() throws Exception {
        // Get the menus
        restMenusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenus() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        int databaseSizeBeforeUpdate = menusRepository.findAll().size();

        // Update the menus
        Menus updatedMenus = menusRepository.findById(menus.getId()).get();
        // Disconnect from session so that the updates on updatedMenus are not directly saved in db
        em.detach(updatedMenus);
        updatedMenus
            .name(UPDATED_NAME)
            .menuDay(UPDATED_MENU_DAY)
            .menuTime(UPDATED_MENU_TIME)
            .contactId(UPDATED_CONTACT_ID)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .createdDate(UPDATED_CREATED_DATE);
        MenusDTO menusDTO = menusMapper.toDto(updatedMenus);

        restMenusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menusDTO))
            )
            .andExpect(status().isOk());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
        Menus testMenus = menusList.get(menusList.size() - 1);
        assertThat(testMenus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenus.getMenuDay()).isEqualTo(UPDATED_MENU_DAY);
        assertThat(testMenus.getMenuTime()).isEqualTo(UPDATED_MENU_TIME);
        assertThat(testMenus.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenus.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMenus.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testMenus.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testMenus.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenusWithPatch() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        int databaseSizeBeforeUpdate = menusRepository.findAll().size();

        // Update the menus using partial update
        Menus partialUpdatedMenus = new Menus();
        partialUpdatedMenus.setId(menus.getId());

        partialUpdatedMenus.contactId(UPDATED_CONTACT_ID).cost(UPDATED_COST).createdDate(UPDATED_CREATED_DATE);

        restMenusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenus))
            )
            .andExpect(status().isOk());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
        Menus testMenus = menusList.get(menusList.size() - 1);
        assertThat(testMenus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenus.getMenuDay()).isEqualTo(DEFAULT_MENU_DAY);
        assertThat(testMenus.getMenuTime()).isEqualTo(DEFAULT_MENU_TIME);
        assertThat(testMenus.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenus.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMenus.getSalesPrice()).isEqualTo(DEFAULT_SALES_PRICE);
        assertThat(testMenus.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testMenus.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMenusWithPatch() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        int databaseSizeBeforeUpdate = menusRepository.findAll().size();

        // Update the menus using partial update
        Menus partialUpdatedMenus = new Menus();
        partialUpdatedMenus.setId(menus.getId());

        partialUpdatedMenus
            .name(UPDATED_NAME)
            .menuDay(UPDATED_MENU_DAY)
            .menuTime(UPDATED_MENU_TIME)
            .contactId(UPDATED_CONTACT_ID)
            .cost(UPDATED_COST)
            .salesPrice(UPDATED_SALES_PRICE)
            .explanation(UPDATED_EXPLANATION)
            .createdDate(UPDATED_CREATED_DATE);

        restMenusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenus))
            )
            .andExpect(status().isOk());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
        Menus testMenus = menusList.get(menusList.size() - 1);
        assertThat(testMenus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenus.getMenuDay()).isEqualTo(UPDATED_MENU_DAY);
        assertThat(testMenus.getMenuTime()).isEqualTo(UPDATED_MENU_TIME);
        assertThat(testMenus.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
        assertThat(testMenus.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMenus.getSalesPrice()).isEqualTo(UPDATED_SALES_PRICE);
        assertThat(testMenus.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testMenus.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenus() throws Exception {
        int databaseSizeBeforeUpdate = menusRepository.findAll().size();
        menus.setId(count.incrementAndGet());

        // Create the Menus
        MenusDTO menusDTO = menusMapper.toDto(menus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menus in the database
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenus() throws Exception {
        // Initialize the database
        menusRepository.saveAndFlush(menus);

        int databaseSizeBeforeDelete = menusRepository.findAll().size();

        // Delete the menus
        restMenusMockMvc
            .perform(delete(ENTITY_API_URL_ID, menus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Menus> menusList = menusRepository.findAll();
        assertThat(menusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
