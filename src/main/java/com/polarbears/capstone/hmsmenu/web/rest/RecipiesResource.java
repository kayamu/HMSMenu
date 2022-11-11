package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.RecipiesRepository;
import com.polarbears.capstone.hmsmenu.service.RecipiesQueryService;
import com.polarbears.capstone.hmsmenu.service.RecipiesService;
import com.polarbears.capstone.hmsmenu.service.criteria.RecipiesCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.Recipies}.
 */
@RestController
@RequestMapping("/api")
public class RecipiesResource {

    private final Logger log = LoggerFactory.getLogger(RecipiesResource.class);

    private static final String ENTITY_NAME = "hmsmenuRecipies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipiesService recipiesService;

    private final RecipiesRepository recipiesRepository;

    private final RecipiesQueryService recipiesQueryService;

    public RecipiesResource(
        RecipiesService recipiesService,
        RecipiesRepository recipiesRepository,
        RecipiesQueryService recipiesQueryService
    ) {
        this.recipiesService = recipiesService;
        this.recipiesRepository = recipiesRepository;
        this.recipiesQueryService = recipiesQueryService;
    }

    /**
     * {@code POST  /recipies} : Create a new recipies.
     *
     * @param recipiesDTO the recipiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipiesDTO, or with status {@code 400 (Bad Request)} if the recipies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recipies")
    public ResponseEntity<RecipiesDTO> createRecipies(@RequestBody RecipiesDTO recipiesDTO) throws URISyntaxException {
        log.debug("REST request to save Recipies : {}", recipiesDTO);
        if (recipiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new recipies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecipiesDTO result = recipiesService.save(recipiesDTO);
        return ResponseEntity
            .created(new URI("/api/recipies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recipies/:id} : Updates an existing recipies.
     *
     * @param id the id of the recipiesDTO to save.
     * @param recipiesDTO the recipiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipiesDTO,
     * or with status {@code 400 (Bad Request)} if the recipiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipies/{id}")
    public ResponseEntity<RecipiesDTO> updateRecipies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecipiesDTO recipiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Recipies : {}, {}", id, recipiesDTO);
        if (recipiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecipiesDTO result = recipiesService.update(recipiesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recipies/:id} : Partial updates given fields of an existing recipies, field will ignore if it is null
     *
     * @param id the id of the recipiesDTO to save.
     * @param recipiesDTO the recipiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipiesDTO,
     * or with status {@code 400 (Bad Request)} if the recipiesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recipiesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recipies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecipiesDTO> partialUpdateRecipies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecipiesDTO recipiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recipies partially : {}, {}", id, recipiesDTO);
        if (recipiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecipiesDTO> result = recipiesService.partialUpdate(recipiesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipiesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recipies} : get all the recipies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipies in body.
     */
    @GetMapping("/recipies")
    public ResponseEntity<List<RecipiesDTO>> getAllRecipies(RecipiesCriteria criteria) {
        log.debug("REST request to get Recipies by criteria: {}", criteria);
        List<RecipiesDTO> entityList = recipiesQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /recipies/count} : count all the recipies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/recipies/count")
    public ResponseEntity<Long> countRecipies(RecipiesCriteria criteria) {
        log.debug("REST request to count Recipies by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipiesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recipies/:id} : get the "id" recipies.
     *
     * @param id the id of the recipiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipies/{id}")
    public ResponseEntity<RecipiesDTO> getRecipies(@PathVariable Long id) {
        log.debug("REST request to get Recipies : {}", id);
        Optional<RecipiesDTO> recipiesDTO = recipiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipiesDTO);
    }

    /**
     * {@code DELETE  /recipies/:id} : delete the "id" recipies.
     *
     * @param id the id of the recipiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recipies/{id}")
    public ResponseEntity<Void> deleteRecipies(@PathVariable Long id) {
        log.debug("REST request to delete Recipies : {}", id);
        recipiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
