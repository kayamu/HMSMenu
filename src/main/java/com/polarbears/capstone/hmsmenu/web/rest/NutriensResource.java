package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.NutriensRepository;
import com.polarbears.capstone.hmsmenu.service.NutriensQueryService;
import com.polarbears.capstone.hmsmenu.service.NutriensService;
import com.polarbears.capstone.hmsmenu.service.criteria.NutriensCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.Nutriens}.
 */
@RestController
@RequestMapping("/api")
public class NutriensResource {

    private final Logger log = LoggerFactory.getLogger(NutriensResource.class);

    private static final String ENTITY_NAME = "hmsmenuNutriens";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NutriensService nutriensService;

    private final NutriensRepository nutriensRepository;

    private final NutriensQueryService nutriensQueryService;

    public NutriensResource(
        NutriensService nutriensService,
        NutriensRepository nutriensRepository,
        NutriensQueryService nutriensQueryService
    ) {
        this.nutriensService = nutriensService;
        this.nutriensRepository = nutriensRepository;
        this.nutriensQueryService = nutriensQueryService;
    }

    /**
     * {@code POST  /nutriens} : Create a new nutriens.
     *
     * @param nutriensDTO the nutriensDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nutriensDTO, or with status {@code 400 (Bad Request)} if the nutriens has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nutriens")
    public ResponseEntity<NutriensDTO> createNutriens(@RequestBody NutriensDTO nutriensDTO) throws URISyntaxException {
        log.debug("REST request to save Nutriens : {}", nutriensDTO);
        if (nutriensDTO.getId() != null) {
            throw new BadRequestAlertException("A new nutriens cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NutriensDTO result = nutriensService.save(nutriensDTO);
        return ResponseEntity
            .created(new URI("/api/nutriens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nutriens/:id} : Updates an existing nutriens.
     *
     * @param id the id of the nutriensDTO to save.
     * @param nutriensDTO the nutriensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nutriensDTO,
     * or with status {@code 400 (Bad Request)} if the nutriensDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nutriensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nutriens/{id}")
    public ResponseEntity<NutriensDTO> updateNutriens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NutriensDTO nutriensDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Nutriens : {}, {}", id, nutriensDTO);
        if (nutriensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nutriensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nutriensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NutriensDTO result = nutriensService.update(nutriensDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nutriensDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nutriens/:id} : Partial updates given fields of an existing nutriens, field will ignore if it is null
     *
     * @param id the id of the nutriensDTO to save.
     * @param nutriensDTO the nutriensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nutriensDTO,
     * or with status {@code 400 (Bad Request)} if the nutriensDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nutriensDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nutriensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nutriens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NutriensDTO> partialUpdateNutriens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NutriensDTO nutriensDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nutriens partially : {}, {}", id, nutriensDTO);
        if (nutriensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nutriensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nutriensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NutriensDTO> result = nutriensService.partialUpdate(nutriensDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nutriensDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nutriens} : get all the nutriens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nutriens in body.
     */
    @GetMapping("/nutriens")
    public ResponseEntity<List<NutriensDTO>> getAllNutriens(NutriensCriteria criteria) {
        log.debug("REST request to get Nutriens by criteria: {}", criteria);
        List<NutriensDTO> entityList = nutriensQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /nutriens/count} : count all the nutriens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nutriens/count")
    public ResponseEntity<Long> countNutriens(NutriensCriteria criteria) {
        log.debug("REST request to count Nutriens by criteria: {}", criteria);
        return ResponseEntity.ok().body(nutriensQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nutriens/:id} : get the "id" nutriens.
     *
     * @param id the id of the nutriensDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nutriensDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nutriens/{id}")
    public ResponseEntity<NutriensDTO> getNutriens(@PathVariable Long id) {
        log.debug("REST request to get Nutriens : {}", id);
        Optional<NutriensDTO> nutriensDTO = nutriensService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nutriensDTO);
    }

    /**
     * {@code DELETE  /nutriens/:id} : delete the "id" nutriens.
     *
     * @param id the id of the nutriensDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nutriens/{id}")
    public ResponseEntity<Void> deleteNutriens(@PathVariable Long id) {
        log.debug("REST request to delete Nutriens : {}", id);
        nutriensService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
