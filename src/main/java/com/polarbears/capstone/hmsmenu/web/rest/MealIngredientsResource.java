package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.MealIngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.MealIngredientsQueryService;
import com.polarbears.capstone.hmsmenu.service.MealIngredientsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MealIngredientsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.MealIngredients}.
 */
@RestController
@RequestMapping("/api")
public class MealIngredientsResource {

    private final Logger log = LoggerFactory.getLogger(MealIngredientsResource.class);

    private static final String ENTITY_NAME = "hmsmenuMealIngredients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MealIngredientsService mealIngredientsService;

    private final MealIngredientsRepository mealIngredientsRepository;

    private final MealIngredientsQueryService mealIngredientsQueryService;

    public MealIngredientsResource(
        MealIngredientsService mealIngredientsService,
        MealIngredientsRepository mealIngredientsRepository,
        MealIngredientsQueryService mealIngredientsQueryService
    ) {
        this.mealIngredientsService = mealIngredientsService;
        this.mealIngredientsRepository = mealIngredientsRepository;
        this.mealIngredientsQueryService = mealIngredientsQueryService;
    }

    /**
     * {@code POST  /meal-ingredients} : Create a new mealIngredients.
     *
     * @param mealIngredientsDTO the mealIngredientsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mealIngredientsDTO, or with status {@code 400 (Bad Request)} if the mealIngredients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meal-ingredients")
    public ResponseEntity<MealIngredientsDTO> createMealIngredients(@RequestBody MealIngredientsDTO mealIngredientsDTO)
        throws URISyntaxException {
        log.debug("REST request to save MealIngredients : {}", mealIngredientsDTO);
        if (mealIngredientsDTO.getId() != null) {
            throw new BadRequestAlertException("A new mealIngredients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MealIngredientsDTO result = mealIngredientsService.save(mealIngredientsDTO);
        return ResponseEntity
            .created(new URI("/api/meal-ingredients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meal-ingredients/:id} : Updates an existing mealIngredients.
     *
     * @param id the id of the mealIngredientsDTO to save.
     * @param mealIngredientsDTO the mealIngredientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealIngredientsDTO,
     * or with status {@code 400 (Bad Request)} if the mealIngredientsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mealIngredientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meal-ingredients/{id}")
    public ResponseEntity<MealIngredientsDTO> updateMealIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealIngredientsDTO mealIngredientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MealIngredients : {}, {}", id, mealIngredientsDTO);
        if (mealIngredientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealIngredientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealIngredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MealIngredientsDTO result = mealIngredientsService.update(mealIngredientsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealIngredientsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meal-ingredients/:id} : Partial updates given fields of an existing mealIngredients, field will ignore if it is null
     *
     * @param id the id of the mealIngredientsDTO to save.
     * @param mealIngredientsDTO the mealIngredientsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealIngredientsDTO,
     * or with status {@code 400 (Bad Request)} if the mealIngredientsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mealIngredientsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mealIngredientsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meal-ingredients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MealIngredientsDTO> partialUpdateMealIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealIngredientsDTO mealIngredientsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MealIngredients partially : {}, {}", id, mealIngredientsDTO);
        if (mealIngredientsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealIngredientsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealIngredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MealIngredientsDTO> result = mealIngredientsService.partialUpdate(mealIngredientsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealIngredientsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meal-ingredients} : get all the mealIngredients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mealIngredients in body.
     */
    @GetMapping("/meal-ingredients")
    public ResponseEntity<List<MealIngredientsDTO>> getAllMealIngredients(MealIngredientsCriteria criteria) {
        log.debug("REST request to get MealIngredients by criteria: {}", criteria);
        List<MealIngredientsDTO> entityList = mealIngredientsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /meal-ingredients/count} : count all the mealIngredients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/meal-ingredients/count")
    public ResponseEntity<Long> countMealIngredients(MealIngredientsCriteria criteria) {
        log.debug("REST request to count MealIngredients by criteria: {}", criteria);
        return ResponseEntity.ok().body(mealIngredientsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meal-ingredients/:id} : get the "id" mealIngredients.
     *
     * @param id the id of the mealIngredientsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mealIngredientsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meal-ingredients/{id}")
    public ResponseEntity<MealIngredientsDTO> getMealIngredients(@PathVariable Long id) {
        log.debug("REST request to get MealIngredients : {}", id);
        Optional<MealIngredientsDTO> mealIngredientsDTO = mealIngredientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mealIngredientsDTO);
    }

    /**
     * {@code DELETE  /meal-ingredients/:id} : delete the "id" mealIngredients.
     *
     * @param id the id of the mealIngredientsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meal-ingredients/{id}")
    public ResponseEntity<Void> deleteMealIngredients(@PathVariable Long id) {
        log.debug("REST request to delete MealIngredients : {}", id);
        mealIngredientsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
