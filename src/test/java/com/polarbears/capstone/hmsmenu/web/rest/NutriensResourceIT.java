package com.polarbears.capstone.hmsmenu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsmenu.IntegrationTest;
import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.repository.NutriensRepository;
import com.polarbears.capstone.hmsmenu.service.criteria.NutriensCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.NutriensMapper;
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
 * Integration tests for the {@link NutriensResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NutriensResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PROTEIN = 1D;
    private static final Double UPDATED_PROTEIN = 2D;
    private static final Double SMALLER_PROTEIN = 1D - 1D;

    private static final Double DEFAULT_CARB = 1D;
    private static final Double UPDATED_CARB = 2D;
    private static final Double SMALLER_CARB = 1D - 1D;

    private static final Double DEFAULT_FAT = 1D;
    private static final Double UPDATED_FAT = 2D;
    private static final Double SMALLER_FAT = 1D - 1D;

    private static final Double DEFAULT_KCAL = 1D;
    private static final Double UPDATED_KCAL = 2D;
    private static final Double SMALLER_KCAL = 1D - 1D;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/nutriens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NutriensRepository nutriensRepository;

    @Autowired
    private NutriensMapper nutriensMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNutriensMockMvc;

    private Nutriens nutriens;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nutriens createEntity(EntityManager em) {
        Nutriens nutriens = new Nutriens()
            .name(DEFAULT_NAME)
            .protein(DEFAULT_PROTEIN)
            .carb(DEFAULT_CARB)
            .fat(DEFAULT_FAT)
            .kcal(DEFAULT_KCAL)
            .createdDate(DEFAULT_CREATED_DATE);
        return nutriens;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nutriens createUpdatedEntity(EntityManager em) {
        Nutriens nutriens = new Nutriens()
            .name(UPDATED_NAME)
            .protein(UPDATED_PROTEIN)
            .carb(UPDATED_CARB)
            .fat(UPDATED_FAT)
            .kcal(UPDATED_KCAL)
            .createdDate(UPDATED_CREATED_DATE);
        return nutriens;
    }

    @BeforeEach
    public void initTest() {
        nutriens = createEntity(em);
    }

    @Test
    @Transactional
    void createNutriens() throws Exception {
        int databaseSizeBeforeCreate = nutriensRepository.findAll().size();
        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);
        restNutriensMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nutriensDTO)))
            .andExpect(status().isCreated());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeCreate + 1);
        Nutriens testNutriens = nutriensList.get(nutriensList.size() - 1);
        assertThat(testNutriens.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNutriens.getProtein()).isEqualTo(DEFAULT_PROTEIN);
        assertThat(testNutriens.getCarb()).isEqualTo(DEFAULT_CARB);
        assertThat(testNutriens.getFat()).isEqualTo(DEFAULT_FAT);
        assertThat(testNutriens.getKcal()).isEqualTo(DEFAULT_KCAL);
        assertThat(testNutriens.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createNutriensWithExistingId() throws Exception {
        // Create the Nutriens with an existing ID
        nutriens.setId(1L);
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        int databaseSizeBeforeCreate = nutriensRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNutriensMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nutriensDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNutriens() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nutriens.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN.doubleValue())))
            .andExpect(jsonPath("$.[*].carb").value(hasItem(DEFAULT_CARB.doubleValue())))
            .andExpect(jsonPath("$.[*].fat").value(hasItem(DEFAULT_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].kcal").value(hasItem(DEFAULT_KCAL.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNutriens() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get the nutriens
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL_ID, nutriens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nutriens.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.protein").value(DEFAULT_PROTEIN.doubleValue()))
            .andExpect(jsonPath("$.carb").value(DEFAULT_CARB.doubleValue()))
            .andExpect(jsonPath("$.fat").value(DEFAULT_FAT.doubleValue()))
            .andExpect(jsonPath("$.kcal").value(DEFAULT_KCAL.doubleValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNutriensByIdFiltering() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        Long id = nutriens.getId();

        defaultNutriensShouldBeFound("id.equals=" + id);
        defaultNutriensShouldNotBeFound("id.notEquals=" + id);

        defaultNutriensShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNutriensShouldNotBeFound("id.greaterThan=" + id);

        defaultNutriensShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNutriensShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNutriensByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where name equals to DEFAULT_NAME
        defaultNutriensShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the nutriensList where name equals to UPDATED_NAME
        defaultNutriensShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNutriensByNameIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNutriensShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the nutriensList where name equals to UPDATED_NAME
        defaultNutriensShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNutriensByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where name is not null
        defaultNutriensShouldBeFound("name.specified=true");

        // Get all the nutriensList where name is null
        defaultNutriensShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByNameContainsSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where name contains DEFAULT_NAME
        defaultNutriensShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the nutriensList where name contains UPDATED_NAME
        defaultNutriensShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNutriensByNameNotContainsSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where name does not contain DEFAULT_NAME
        defaultNutriensShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the nutriensList where name does not contain UPDATED_NAME
        defaultNutriensShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein equals to DEFAULT_PROTEIN
        defaultNutriensShouldBeFound("protein.equals=" + DEFAULT_PROTEIN);

        // Get all the nutriensList where protein equals to UPDATED_PROTEIN
        defaultNutriensShouldNotBeFound("protein.equals=" + UPDATED_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein in DEFAULT_PROTEIN or UPDATED_PROTEIN
        defaultNutriensShouldBeFound("protein.in=" + DEFAULT_PROTEIN + "," + UPDATED_PROTEIN);

        // Get all the nutriensList where protein equals to UPDATED_PROTEIN
        defaultNutriensShouldNotBeFound("protein.in=" + UPDATED_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein is not null
        defaultNutriensShouldBeFound("protein.specified=true");

        // Get all the nutriensList where protein is null
        defaultNutriensShouldNotBeFound("protein.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein is greater than or equal to DEFAULT_PROTEIN
        defaultNutriensShouldBeFound("protein.greaterThanOrEqual=" + DEFAULT_PROTEIN);

        // Get all the nutriensList where protein is greater than or equal to UPDATED_PROTEIN
        defaultNutriensShouldNotBeFound("protein.greaterThanOrEqual=" + UPDATED_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein is less than or equal to DEFAULT_PROTEIN
        defaultNutriensShouldBeFound("protein.lessThanOrEqual=" + DEFAULT_PROTEIN);

        // Get all the nutriensList where protein is less than or equal to SMALLER_PROTEIN
        defaultNutriensShouldNotBeFound("protein.lessThanOrEqual=" + SMALLER_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsLessThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein is less than DEFAULT_PROTEIN
        defaultNutriensShouldNotBeFound("protein.lessThan=" + DEFAULT_PROTEIN);

        // Get all the nutriensList where protein is less than UPDATED_PROTEIN
        defaultNutriensShouldBeFound("protein.lessThan=" + UPDATED_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByProteinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where protein is greater than DEFAULT_PROTEIN
        defaultNutriensShouldNotBeFound("protein.greaterThan=" + DEFAULT_PROTEIN);

        // Get all the nutriensList where protein is greater than SMALLER_PROTEIN
        defaultNutriensShouldBeFound("protein.greaterThan=" + SMALLER_PROTEIN);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb equals to DEFAULT_CARB
        defaultNutriensShouldBeFound("carb.equals=" + DEFAULT_CARB);

        // Get all the nutriensList where carb equals to UPDATED_CARB
        defaultNutriensShouldNotBeFound("carb.equals=" + UPDATED_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb in DEFAULT_CARB or UPDATED_CARB
        defaultNutriensShouldBeFound("carb.in=" + DEFAULT_CARB + "," + UPDATED_CARB);

        // Get all the nutriensList where carb equals to UPDATED_CARB
        defaultNutriensShouldNotBeFound("carb.in=" + UPDATED_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb is not null
        defaultNutriensShouldBeFound("carb.specified=true");

        // Get all the nutriensList where carb is null
        defaultNutriensShouldNotBeFound("carb.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb is greater than or equal to DEFAULT_CARB
        defaultNutriensShouldBeFound("carb.greaterThanOrEqual=" + DEFAULT_CARB);

        // Get all the nutriensList where carb is greater than or equal to UPDATED_CARB
        defaultNutriensShouldNotBeFound("carb.greaterThanOrEqual=" + UPDATED_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb is less than or equal to DEFAULT_CARB
        defaultNutriensShouldBeFound("carb.lessThanOrEqual=" + DEFAULT_CARB);

        // Get all the nutriensList where carb is less than or equal to SMALLER_CARB
        defaultNutriensShouldNotBeFound("carb.lessThanOrEqual=" + SMALLER_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsLessThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb is less than DEFAULT_CARB
        defaultNutriensShouldNotBeFound("carb.lessThan=" + DEFAULT_CARB);

        // Get all the nutriensList where carb is less than UPDATED_CARB
        defaultNutriensShouldBeFound("carb.lessThan=" + UPDATED_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByCarbIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where carb is greater than DEFAULT_CARB
        defaultNutriensShouldNotBeFound("carb.greaterThan=" + DEFAULT_CARB);

        // Get all the nutriensList where carb is greater than SMALLER_CARB
        defaultNutriensShouldBeFound("carb.greaterThan=" + SMALLER_CARB);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat equals to DEFAULT_FAT
        defaultNutriensShouldBeFound("fat.equals=" + DEFAULT_FAT);

        // Get all the nutriensList where fat equals to UPDATED_FAT
        defaultNutriensShouldNotBeFound("fat.equals=" + UPDATED_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat in DEFAULT_FAT or UPDATED_FAT
        defaultNutriensShouldBeFound("fat.in=" + DEFAULT_FAT + "," + UPDATED_FAT);

        // Get all the nutriensList where fat equals to UPDATED_FAT
        defaultNutriensShouldNotBeFound("fat.in=" + UPDATED_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat is not null
        defaultNutriensShouldBeFound("fat.specified=true");

        // Get all the nutriensList where fat is null
        defaultNutriensShouldNotBeFound("fat.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat is greater than or equal to DEFAULT_FAT
        defaultNutriensShouldBeFound("fat.greaterThanOrEqual=" + DEFAULT_FAT);

        // Get all the nutriensList where fat is greater than or equal to UPDATED_FAT
        defaultNutriensShouldNotBeFound("fat.greaterThanOrEqual=" + UPDATED_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat is less than or equal to DEFAULT_FAT
        defaultNutriensShouldBeFound("fat.lessThanOrEqual=" + DEFAULT_FAT);

        // Get all the nutriensList where fat is less than or equal to SMALLER_FAT
        defaultNutriensShouldNotBeFound("fat.lessThanOrEqual=" + SMALLER_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsLessThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat is less than DEFAULT_FAT
        defaultNutriensShouldNotBeFound("fat.lessThan=" + DEFAULT_FAT);

        // Get all the nutriensList where fat is less than UPDATED_FAT
        defaultNutriensShouldBeFound("fat.lessThan=" + UPDATED_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByFatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where fat is greater than DEFAULT_FAT
        defaultNutriensShouldNotBeFound("fat.greaterThan=" + DEFAULT_FAT);

        // Get all the nutriensList where fat is greater than SMALLER_FAT
        defaultNutriensShouldBeFound("fat.greaterThan=" + SMALLER_FAT);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal equals to DEFAULT_KCAL
        defaultNutriensShouldBeFound("kcal.equals=" + DEFAULT_KCAL);

        // Get all the nutriensList where kcal equals to UPDATED_KCAL
        defaultNutriensShouldNotBeFound("kcal.equals=" + UPDATED_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal in DEFAULT_KCAL or UPDATED_KCAL
        defaultNutriensShouldBeFound("kcal.in=" + DEFAULT_KCAL + "," + UPDATED_KCAL);

        // Get all the nutriensList where kcal equals to UPDATED_KCAL
        defaultNutriensShouldNotBeFound("kcal.in=" + UPDATED_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal is not null
        defaultNutriensShouldBeFound("kcal.specified=true");

        // Get all the nutriensList where kcal is null
        defaultNutriensShouldNotBeFound("kcal.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal is greater than or equal to DEFAULT_KCAL
        defaultNutriensShouldBeFound("kcal.greaterThanOrEqual=" + DEFAULT_KCAL);

        // Get all the nutriensList where kcal is greater than or equal to UPDATED_KCAL
        defaultNutriensShouldNotBeFound("kcal.greaterThanOrEqual=" + UPDATED_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal is less than or equal to DEFAULT_KCAL
        defaultNutriensShouldBeFound("kcal.lessThanOrEqual=" + DEFAULT_KCAL);

        // Get all the nutriensList where kcal is less than or equal to SMALLER_KCAL
        defaultNutriensShouldNotBeFound("kcal.lessThanOrEqual=" + SMALLER_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsLessThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal is less than DEFAULT_KCAL
        defaultNutriensShouldNotBeFound("kcal.lessThan=" + DEFAULT_KCAL);

        // Get all the nutriensList where kcal is less than UPDATED_KCAL
        defaultNutriensShouldBeFound("kcal.lessThan=" + UPDATED_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByKcalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where kcal is greater than DEFAULT_KCAL
        defaultNutriensShouldNotBeFound("kcal.greaterThan=" + DEFAULT_KCAL);

        // Get all the nutriensList where kcal is greater than SMALLER_KCAL
        defaultNutriensShouldBeFound("kcal.greaterThan=" + SMALLER_KCAL);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate equals to DEFAULT_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the nutriensList where createdDate equals to UPDATED_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the nutriensList where createdDate equals to UPDATED_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate is not null
        defaultNutriensShouldBeFound("createdDate.specified=true");

        // Get all the nutriensList where createdDate is null
        defaultNutriensShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the nutriensList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the nutriensList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate is less than DEFAULT_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the nutriensList where createdDate is less than UPDATED_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        // Get all the nutriensList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultNutriensShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the nutriensList where createdDate is greater than SMALLER_CREATED_DATE
        defaultNutriensShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNutriensByMenuGroupsIsEqualToSomething() throws Exception {
        MenuGroups menuGroups;
        if (TestUtil.findAll(em, MenuGroups.class).isEmpty()) {
            nutriensRepository.saveAndFlush(nutriens);
            menuGroups = MenuGroupsResourceIT.createEntity(em);
        } else {
            menuGroups = TestUtil.findAll(em, MenuGroups.class).get(0);
        }
        em.persist(menuGroups);
        em.flush();
        nutriens.addMenuGroups(menuGroups);
        nutriensRepository.saveAndFlush(nutriens);
        Long menuGroupsId = menuGroups.getId();

        // Get all the nutriensList where menuGroups equals to menuGroupsId
        defaultNutriensShouldBeFound("menuGroupsId.equals=" + menuGroupsId);

        // Get all the nutriensList where menuGroups equals to (menuGroupsId + 1)
        defaultNutriensShouldNotBeFound("menuGroupsId.equals=" + (menuGroupsId + 1));
    }

    @Test
    @Transactional
    void getAllNutriensByMenusIsEqualToSomething() throws Exception {
        Menus menus;
        if (TestUtil.findAll(em, Menus.class).isEmpty()) {
            nutriensRepository.saveAndFlush(nutriens);
            menus = MenusResourceIT.createEntity(em);
        } else {
            menus = TestUtil.findAll(em, Menus.class).get(0);
        }
        em.persist(menus);
        em.flush();
        nutriens.addMenus(menus);
        nutriensRepository.saveAndFlush(nutriens);
        Long menusId = menus.getId();

        // Get all the nutriensList where menus equals to menusId
        defaultNutriensShouldBeFound("menusId.equals=" + menusId);

        // Get all the nutriensList where menus equals to (menusId + 1)
        defaultNutriensShouldNotBeFound("menusId.equals=" + (menusId + 1));
    }

    @Test
    @Transactional
    void getAllNutriensByMealIngredientsIsEqualToSomething() throws Exception {
        MealIngredients mealIngredients;
        if (TestUtil.findAll(em, MealIngredients.class).isEmpty()) {
            nutriensRepository.saveAndFlush(nutriens);
            mealIngredients = MealIngredientsResourceIT.createEntity(em);
        } else {
            mealIngredients = TestUtil.findAll(em, MealIngredients.class).get(0);
        }
        em.persist(mealIngredients);
        em.flush();
        nutriens.addMealIngredients(mealIngredients);
        nutriensRepository.saveAndFlush(nutriens);
        Long mealIngredientsId = mealIngredients.getId();

        // Get all the nutriensList where mealIngredients equals to mealIngredientsId
        defaultNutriensShouldBeFound("mealIngredientsId.equals=" + mealIngredientsId);

        // Get all the nutriensList where mealIngredients equals to (mealIngredientsId + 1)
        defaultNutriensShouldNotBeFound("mealIngredientsId.equals=" + (mealIngredientsId + 1));
    }

    @Test
    @Transactional
    void getAllNutriensByMealsIsEqualToSomething() throws Exception {
        Meals meals;
        if (TestUtil.findAll(em, Meals.class).isEmpty()) {
            nutriensRepository.saveAndFlush(nutriens);
            meals = MealsResourceIT.createEntity(em);
        } else {
            meals = TestUtil.findAll(em, Meals.class).get(0);
        }
        em.persist(meals);
        em.flush();
        nutriens.addMeals(meals);
        nutriensRepository.saveAndFlush(nutriens);
        Long mealsId = meals.getId();

        // Get all the nutriensList where meals equals to mealsId
        defaultNutriensShouldBeFound("mealsId.equals=" + mealsId);

        // Get all the nutriensList where meals equals to (mealsId + 1)
        defaultNutriensShouldNotBeFound("mealsId.equals=" + (mealsId + 1));
    }

    @Test
    @Transactional
    void getAllNutriensByIngredientsIsEqualToSomething() throws Exception {
        Ingredients ingredients;
        if (TestUtil.findAll(em, Ingredients.class).isEmpty()) {
            nutriensRepository.saveAndFlush(nutriens);
            ingredients = IngredientsResourceIT.createEntity(em);
        } else {
            ingredients = TestUtil.findAll(em, Ingredients.class).get(0);
        }
        em.persist(ingredients);
        em.flush();
        nutriens.addIngredients(ingredients);
        nutriensRepository.saveAndFlush(nutriens);
        Long ingredientsId = ingredients.getId();

        // Get all the nutriensList where ingredients equals to ingredientsId
        defaultNutriensShouldBeFound("ingredientsId.equals=" + ingredientsId);

        // Get all the nutriensList where ingredients equals to (ingredientsId + 1)
        defaultNutriensShouldNotBeFound("ingredientsId.equals=" + (ingredientsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNutriensShouldBeFound(String filter) throws Exception {
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nutriens.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN.doubleValue())))
            .andExpect(jsonPath("$.[*].carb").value(hasItem(DEFAULT_CARB.doubleValue())))
            .andExpect(jsonPath("$.[*].fat").value(hasItem(DEFAULT_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].kcal").value(hasItem(DEFAULT_KCAL.doubleValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNutriensShouldNotBeFound(String filter) throws Exception {
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNutriensMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNutriens() throws Exception {
        // Get the nutriens
        restNutriensMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNutriens() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();

        // Update the nutriens
        Nutriens updatedNutriens = nutriensRepository.findById(nutriens.getId()).get();
        // Disconnect from session so that the updates on updatedNutriens are not directly saved in db
        em.detach(updatedNutriens);
        updatedNutriens
            .name(UPDATED_NAME)
            .protein(UPDATED_PROTEIN)
            .carb(UPDATED_CARB)
            .fat(UPDATED_FAT)
            .kcal(UPDATED_KCAL)
            .createdDate(UPDATED_CREATED_DATE);
        NutriensDTO nutriensDTO = nutriensMapper.toDto(updatedNutriens);

        restNutriensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nutriensDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
        Nutriens testNutriens = nutriensList.get(nutriensList.size() - 1);
        assertThat(testNutriens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNutriens.getProtein()).isEqualTo(UPDATED_PROTEIN);
        assertThat(testNutriens.getCarb()).isEqualTo(UPDATED_CARB);
        assertThat(testNutriens.getFat()).isEqualTo(UPDATED_FAT);
        assertThat(testNutriens.getKcal()).isEqualTo(UPDATED_KCAL);
        assertThat(testNutriens.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nutriensDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nutriensDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNutriensWithPatch() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();

        // Update the nutriens using partial update
        Nutriens partialUpdatedNutriens = new Nutriens();
        partialUpdatedNutriens.setId(nutriens.getId());

        partialUpdatedNutriens.name(UPDATED_NAME).protein(UPDATED_PROTEIN).carb(UPDATED_CARB).kcal(UPDATED_KCAL);

        restNutriensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNutriens.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNutriens))
            )
            .andExpect(status().isOk());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
        Nutriens testNutriens = nutriensList.get(nutriensList.size() - 1);
        assertThat(testNutriens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNutriens.getProtein()).isEqualTo(UPDATED_PROTEIN);
        assertThat(testNutriens.getCarb()).isEqualTo(UPDATED_CARB);
        assertThat(testNutriens.getFat()).isEqualTo(DEFAULT_FAT);
        assertThat(testNutriens.getKcal()).isEqualTo(UPDATED_KCAL);
        assertThat(testNutriens.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateNutriensWithPatch() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();

        // Update the nutriens using partial update
        Nutriens partialUpdatedNutriens = new Nutriens();
        partialUpdatedNutriens.setId(nutriens.getId());

        partialUpdatedNutriens
            .name(UPDATED_NAME)
            .protein(UPDATED_PROTEIN)
            .carb(UPDATED_CARB)
            .fat(UPDATED_FAT)
            .kcal(UPDATED_KCAL)
            .createdDate(UPDATED_CREATED_DATE);

        restNutriensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNutriens.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNutriens))
            )
            .andExpect(status().isOk());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
        Nutriens testNutriens = nutriensList.get(nutriensList.size() - 1);
        assertThat(testNutriens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNutriens.getProtein()).isEqualTo(UPDATED_PROTEIN);
        assertThat(testNutriens.getCarb()).isEqualTo(UPDATED_CARB);
        assertThat(testNutriens.getFat()).isEqualTo(UPDATED_FAT);
        assertThat(testNutriens.getKcal()).isEqualTo(UPDATED_KCAL);
        assertThat(testNutriens.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nutriensDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNutriens() throws Exception {
        int databaseSizeBeforeUpdate = nutriensRepository.findAll().size();
        nutriens.setId(count.incrementAndGet());

        // Create the Nutriens
        NutriensDTO nutriensDTO = nutriensMapper.toDto(nutriens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNutriensMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nutriensDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nutriens in the database
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNutriens() throws Exception {
        // Initialize the database
        nutriensRepository.saveAndFlush(nutriens);

        int databaseSizeBeforeDelete = nutriensRepository.findAll().size();

        // Delete the nutriens
        restNutriensMockMvc
            .perform(delete(ENTITY_API_URL_ID, nutriens.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nutriens> nutriensList = nutriensRepository.findAll();
        assertThat(nutriensList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
