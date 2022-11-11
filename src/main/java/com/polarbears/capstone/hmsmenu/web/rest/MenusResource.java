package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.MenusRepository;
import com.polarbears.capstone.hmsmenu.service.MenusQueryService;
import com.polarbears.capstone.hmsmenu.service.MenusService;
import com.polarbears.capstone.hmsmenu.service.criteria.MenusCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.Menus}.
 */
@RestController
@RequestMapping("/api")
public class MenusResource {

    private final Logger log = LoggerFactory.getLogger(MenusResource.class);

    private static final String ENTITY_NAME = "hmsmenuMenus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenusService menusService;

    private final MenusRepository menusRepository;

    private final MenusQueryService menusQueryService;

    public MenusResource(MenusService menusService, MenusRepository menusRepository, MenusQueryService menusQueryService) {
        this.menusService = menusService;
        this.menusRepository = menusRepository;
        this.menusQueryService = menusQueryService;
    }

    /**
     * {@code POST  /menus} : Create a new menus.
     *
     * @param menusDTO the menusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menusDTO, or with status {@code 400 (Bad Request)} if the menus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menus")
    public ResponseEntity<MenusDTO> createMenus(@RequestBody MenusDTO menusDTO) throws URISyntaxException {
        log.debug("REST request to save Menus : {}", menusDTO);
        if (menusDTO.getId() != null) {
            throw new BadRequestAlertException("A new menus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenusDTO result = menusService.save(menusDTO);
        return ResponseEntity
            .created(new URI("/api/menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menus/:id} : Updates an existing menus.
     *
     * @param id the id of the menusDTO to save.
     * @param menusDTO the menusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menusDTO,
     * or with status {@code 400 (Bad Request)} if the menusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menus/{id}")
    public ResponseEntity<MenusDTO> updateMenus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenusDTO menusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Menus : {}, {}", id, menusDTO);
        if (menusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenusDTO result = menusService.update(menusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menus/:id} : Partial updates given fields of an existing menus, field will ignore if it is null
     *
     * @param id the id of the menusDTO to save.
     * @param menusDTO the menusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menusDTO,
     * or with status {@code 400 (Bad Request)} if the menusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the menusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the menusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/menus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenusDTO> partialUpdateMenus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenusDTO menusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Menus partially : {}, {}", id, menusDTO);
        if (menusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenusDTO> result = menusService.partialUpdate(menusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /menus} : get all the menus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menus in body.
     */
    @GetMapping("/menus")
    public ResponseEntity<List<MenusDTO>> getAllMenus(
        MenusCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Menus by criteria: {}", criteria);
        Page<MenusDTO> page = menusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /menus/count} : count all the menus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/menus/count")
    public ResponseEntity<Long> countMenus(MenusCriteria criteria) {
        log.debug("REST request to count Menus by criteria: {}", criteria);
        return ResponseEntity.ok().body(menusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menus/:id} : get the "id" menus.
     *
     * @param id the id of the menusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menus/{id}")
    public ResponseEntity<MenusDTO> getMenus(@PathVariable Long id) {
        log.debug("REST request to get Menus : {}", id);
        Optional<MenusDTO> menusDTO = menusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menusDTO);
    }

    /**
     * {@code DELETE  /menus/:id} : delete the "id" menus.
     *
     * @param id the id of the menusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> deleteMenus(@PathVariable Long id) {
        log.debug("REST request to delete Menus : {}", id);
        menusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
