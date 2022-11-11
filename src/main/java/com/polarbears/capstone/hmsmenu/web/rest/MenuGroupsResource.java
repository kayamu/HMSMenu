package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.MenuGroupsRepository;
import com.polarbears.capstone.hmsmenu.service.MenuGroupsQueryService;
import com.polarbears.capstone.hmsmenu.service.MenuGroupsService;
import com.polarbears.capstone.hmsmenu.service.criteria.MenuGroupsCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenuGroupsDTO;
import com.polarbears.capstone.hmsmenu.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.MenuGroups}.
 */
@RestController
@RequestMapping("/api")
public class MenuGroupsResource {

    private final Logger log = LoggerFactory.getLogger(MenuGroupsResource.class);

    private static final String ENTITY_NAME = "hmsmenuMenuGroups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuGroupsService menuGroupsService;

    private final MenuGroupsRepository menuGroupsRepository;

    private final MenuGroupsQueryService menuGroupsQueryService;

    public MenuGroupsResource(
        MenuGroupsService menuGroupsService,
        MenuGroupsRepository menuGroupsRepository,
        MenuGroupsQueryService menuGroupsQueryService
    ) {
        this.menuGroupsService = menuGroupsService;
        this.menuGroupsRepository = menuGroupsRepository;
        this.menuGroupsQueryService = menuGroupsQueryService;
    }

    /**
     * {@code POST  /menu-groups} : Create a new menuGroups.
     *
     * @param menuGroupsDTO the menuGroupsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuGroupsDTO, or with status {@code 400 (Bad Request)} if the menuGroups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-groups")
    public ResponseEntity<MenuGroupsDTO> createMenuGroups(@Valid @RequestBody MenuGroupsDTO menuGroupsDTO) throws URISyntaxException {
        log.debug("REST request to save MenuGroups : {}", menuGroupsDTO);
        if (menuGroupsDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuGroups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuGroupsDTO result = menuGroupsService.save(menuGroupsDTO);
        return ResponseEntity
            .created(new URI("/api/menu-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-groups/:id} : Updates an existing menuGroups.
     *
     * @param id the id of the menuGroupsDTO to save.
     * @param menuGroupsDTO the menuGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the menuGroupsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-groups/{id}")
    public ResponseEntity<MenuGroupsDTO> updateMenuGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MenuGroupsDTO menuGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MenuGroups : {}, {}", id, menuGroupsDTO);
        if (menuGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuGroupsDTO result = menuGroupsService.update(menuGroupsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuGroupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menu-groups/:id} : Partial updates given fields of an existing menuGroups, field will ignore if it is null
     *
     * @param id the id of the menuGroupsDTO to save.
     * @param menuGroupsDTO the menuGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the menuGroupsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the menuGroupsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/menu-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuGroupsDTO> partialUpdateMenuGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MenuGroupsDTO menuGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuGroups partially : {}, {}", id, menuGroupsDTO);
        if (menuGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuGroupsDTO> result = menuGroupsService.partialUpdate(menuGroupsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuGroupsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-groups} : get all the menuGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuGroups in body.
     */
    @GetMapping("/menu-groups")
    public ResponseEntity<List<MenuGroupsDTO>> getAllMenuGroups(
        MenuGroupsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MenuGroups by criteria: {}", criteria);
        Page<MenuGroupsDTO> page = menuGroupsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /menu-groups/count} : count all the menuGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/menu-groups/count")
    public ResponseEntity<Long> countMenuGroups(MenuGroupsCriteria criteria) {
        log.debug("REST request to count MenuGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuGroupsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menu-groups/:id} : get the "id" menuGroups.
     *
     * @param id the id of the menuGroupsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuGroupsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-groups/{id}")
    public ResponseEntity<MenuGroupsDTO> getMenuGroups(@PathVariable Long id) {
        log.debug("REST request to get MenuGroups : {}", id);
        Optional<MenuGroupsDTO> menuGroupsDTO = menuGroupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuGroupsDTO);
    }

    /**
     * {@code DELETE  /menu-groups/:id} : delete the "id" menuGroups.
     *
     * @param id the id of the menuGroupsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-groups/{id}")
    public ResponseEntity<Void> deleteMenuGroups(@PathVariable Long id) {
        log.debug("REST request to delete MenuGroups : {}", id);
        menuGroupsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
