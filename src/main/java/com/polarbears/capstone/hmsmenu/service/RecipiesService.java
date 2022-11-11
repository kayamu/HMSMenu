package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.Recipies;
import com.polarbears.capstone.hmsmenu.repository.RecipiesRepository;
import com.polarbears.capstone.hmsmenu.service.dto.RecipiesDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.RecipiesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Recipies}.
 */
@Service
@Transactional
public class RecipiesService {

    private final Logger log = LoggerFactory.getLogger(RecipiesService.class);

    private final RecipiesRepository recipiesRepository;

    private final RecipiesMapper recipiesMapper;

    public RecipiesService(RecipiesRepository recipiesRepository, RecipiesMapper recipiesMapper) {
        this.recipiesRepository = recipiesRepository;
        this.recipiesMapper = recipiesMapper;
    }

    /**
     * Save a recipies.
     *
     * @param recipiesDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipiesDTO save(RecipiesDTO recipiesDTO) {
        log.debug("Request to save Recipies : {}", recipiesDTO);
        Recipies recipies = recipiesMapper.toEntity(recipiesDTO);
        recipies = recipiesRepository.save(recipies);
        return recipiesMapper.toDto(recipies);
    }

    /**
     * Update a recipies.
     *
     * @param recipiesDTO the entity to save.
     * @return the persisted entity.
     */
    public RecipiesDTO update(RecipiesDTO recipiesDTO) {
        log.debug("Request to update Recipies : {}", recipiesDTO);
        Recipies recipies = recipiesMapper.toEntity(recipiesDTO);
        recipies = recipiesRepository.save(recipies);
        return recipiesMapper.toDto(recipies);
    }

    /**
     * Partially update a recipies.
     *
     * @param recipiesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecipiesDTO> partialUpdate(RecipiesDTO recipiesDTO) {
        log.debug("Request to partially update Recipies : {}", recipiesDTO);

        return recipiesRepository
            .findById(recipiesDTO.getId())
            .map(existingRecipies -> {
                recipiesMapper.partialUpdate(existingRecipies, recipiesDTO);

                return existingRecipies;
            })
            .map(recipiesRepository::save)
            .map(recipiesMapper::toDto);
    }

    /**
     * Get all the recipies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RecipiesDTO> findAll() {
        log.debug("Request to get all Recipies");
        return recipiesRepository.findAll().stream().map(recipiesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the recipies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RecipiesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recipiesRepository.findAllWithEagerRelationships(pageable).map(recipiesMapper::toDto);
    }

    /**
     * Get one recipies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecipiesDTO> findOne(Long id) {
        log.debug("Request to get Recipies : {}", id);
        return recipiesRepository.findOneWithEagerRelationships(id).map(recipiesMapper::toDto);
    }

    /**
     * Delete the recipies by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Recipies : {}", id);
        recipiesRepository.deleteById(id);
    }
}
