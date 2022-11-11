package com.polarbears.capstone.hmsmenu.web.rest;

import com.polarbears.capstone.hmsmenu.repository.ImagesUrlRepository;
import com.polarbears.capstone.hmsmenu.service.ImagesUrlQueryService;
import com.polarbears.capstone.hmsmenu.service.ImagesUrlService;
import com.polarbears.capstone.hmsmenu.service.criteria.ImagesUrlCriteria;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsmenu.domain.ImagesUrl}.
 */
@RestController
@RequestMapping("/api")
public class ImagesUrlResource {

    private final Logger log = LoggerFactory.getLogger(ImagesUrlResource.class);

    private static final String ENTITY_NAME = "hmsmenuImagesUrl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagesUrlService imagesUrlService;

    private final ImagesUrlRepository imagesUrlRepository;

    private final ImagesUrlQueryService imagesUrlQueryService;

    public ImagesUrlResource(
        ImagesUrlService imagesUrlService,
        ImagesUrlRepository imagesUrlRepository,
        ImagesUrlQueryService imagesUrlQueryService
    ) {
        this.imagesUrlService = imagesUrlService;
        this.imagesUrlRepository = imagesUrlRepository;
        this.imagesUrlQueryService = imagesUrlQueryService;
    }

    /**
     * {@code POST  /images-urls} : Create a new imagesUrl.
     *
     * @param imagesUrlDTO the imagesUrlDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagesUrlDTO, or with status {@code 400 (Bad Request)} if the imagesUrl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/images-urls")
    public ResponseEntity<ImagesUrlDTO> createImagesUrl(@RequestBody ImagesUrlDTO imagesUrlDTO) throws URISyntaxException {
        log.debug("REST request to save ImagesUrl : {}", imagesUrlDTO);
        if (imagesUrlDTO.getId() != null) {
            throw new BadRequestAlertException("A new imagesUrl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImagesUrlDTO result = imagesUrlService.save(imagesUrlDTO);
        return ResponseEntity
            .created(new URI("/api/images-urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /images-urls/:id} : Updates an existing imagesUrl.
     *
     * @param id the id of the imagesUrlDTO to save.
     * @param imagesUrlDTO the imagesUrlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesUrlDTO,
     * or with status {@code 400 (Bad Request)} if the imagesUrlDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagesUrlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/images-urls/{id}")
    public ResponseEntity<ImagesUrlDTO> updateImagesUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagesUrlDTO imagesUrlDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ImagesUrl : {}, {}", id, imagesUrlDTO);
        if (imagesUrlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesUrlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImagesUrlDTO result = imagesUrlService.update(imagesUrlDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagesUrlDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /images-urls/:id} : Partial updates given fields of an existing imagesUrl, field will ignore if it is null
     *
     * @param id the id of the imagesUrlDTO to save.
     * @param imagesUrlDTO the imagesUrlDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesUrlDTO,
     * or with status {@code 400 (Bad Request)} if the imagesUrlDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imagesUrlDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagesUrlDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/images-urls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagesUrlDTO> partialUpdateImagesUrl(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagesUrlDTO imagesUrlDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImagesUrl partially : {}, {}", id, imagesUrlDTO);
        if (imagesUrlDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesUrlDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesUrlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagesUrlDTO> result = imagesUrlService.partialUpdate(imagesUrlDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagesUrlDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /images-urls} : get all the imagesUrls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imagesUrls in body.
     */
    @GetMapping("/images-urls")
    public ResponseEntity<List<ImagesUrlDTO>> getAllImagesUrls(ImagesUrlCriteria criteria) {
        log.debug("REST request to get ImagesUrls by criteria: {}", criteria);
        List<ImagesUrlDTO> entityList = imagesUrlQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /images-urls/count} : count all the imagesUrls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/images-urls/count")
    public ResponseEntity<Long> countImagesUrls(ImagesUrlCriteria criteria) {
        log.debug("REST request to count ImagesUrls by criteria: {}", criteria);
        return ResponseEntity.ok().body(imagesUrlQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /images-urls/:id} : get the "id" imagesUrl.
     *
     * @param id the id of the imagesUrlDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagesUrlDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/images-urls/{id}")
    public ResponseEntity<ImagesUrlDTO> getImagesUrl(@PathVariable Long id) {
        log.debug("REST request to get ImagesUrl : {}", id);
        Optional<ImagesUrlDTO> imagesUrlDTO = imagesUrlService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagesUrlDTO);
    }

    /**
     * {@code DELETE  /images-urls/:id} : delete the "id" imagesUrl.
     *
     * @param id the id of the imagesUrlDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/images-urls/{id}")
    public ResponseEntity<Void> deleteImagesUrl(@PathVariable Long id) {
        log.debug("REST request to delete ImagesUrl : {}", id);
        imagesUrlService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
