package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.MealsRepository;
import com.polarbears.capstone.hmsmenu.service.MealsQueryService;
import com.polarbears.capstone.hmsmenu.service.MealsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MealsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.Meals}.
 */
@RestController
@RequestMapping("/api")
public class MealsResource {

    private final Logger log = LoggerFactory.getLogger(MealsResource.class);

    private static final String ENTITY_NAME = "hmsmenuMeals";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MealsService mealsService;

    private final MealsRepository mealsRepository;

    private final MealsQueryService mealsQueryService;

    public MealsResource(MealsService mealsService, MealsRepository mealsRepository, MealsQueryService mealsQueryService) {
        this.mealsService = mealsService;
        this.mealsRepository = mealsRepository;
        this.mealsQueryService = mealsQueryService;
    }

    /**
     * {@code POST  /meals} : Create a new meals.
     *
     * @param mealsDTO the mealsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mealsDTO, or with status {@code 400 (Bad Request)} if the meals has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meals")
    public ResponseEntity<MealsDTO> createMeals(@RequestBody MealsDTO mealsDTO) throws URISyntaxException {
        log.debug("REST request to save Meals : {}", mealsDTO);
        if (mealsDTO.getId() != null) {
            throw new BadRequestAlertException("A new meals cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MealsDTO result = mealsService.save(mealsDTO);
        return ResponseEntity
            .created(new URI("/api/meals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meals/:id} : Updates an existing meals.
     *
     * @param id the id of the mealsDTO to save.
     * @param mealsDTO the mealsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealsDTO,
     * or with status {@code 400 (Bad Request)} if the mealsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mealsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meals/{id}")
    public ResponseEntity<MealsDTO> updateMeals(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealsDTO mealsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Meals : {}, {}", id, mealsDTO);
        if (mealsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MealsDTO result = mealsService.update(mealsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meals/:id} : Partial updates given fields of an existing meals, field will ignore if it is null
     *
     * @param id the id of the mealsDTO to save.
     * @param mealsDTO the mealsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealsDTO,
     * or with status {@code 400 (Bad Request)} if the mealsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mealsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mealsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MealsDTO> partialUpdateMeals(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealsDTO mealsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Meals partially : {}, {}", id, mealsDTO);
        if (mealsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MealsDTO> result = mealsService.partialUpdate(mealsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mealsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meals} : get all the meals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meals in body.
     */
    @GetMapping("/meals")
    public ResponseEntity<List<MealsDTO>> getAllMeals(
        MealsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Meals by criteria: {}", criteria);
        Page<MealsDTO> page = mealsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meals/count} : count all the meals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/meals/count")
    public ResponseEntity<Long> countMeals(MealsCriteria criteria) {
        log.debug("REST request to count Meals by criteria: {}", criteria);
        return ResponseEntity.ok().body(mealsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meals/:id} : get the "id" meals.
     *
     * @param id the id of the mealsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mealsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meals/{id}")
    public ResponseEntity<MealsDTO> getMeals(@PathVariable Long id) {
        log.debug("REST request to get Meals : {}", id);
        Optional<MealsDTO> mealsDTO = mealsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mealsDTO);
    }

    /**
     * {@code DELETE  /meals/:id} : delete the "id" meals.
     *
     * @param id the id of the mealsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meals/{id}")
    public ResponseEntity<Void> deleteMeals(@PathVariable Long id) {
        log.debug("REST request to delete Meals : {}", id);
        mealsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
