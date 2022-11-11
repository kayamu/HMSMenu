package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.Ingredients;
import com.polarbears.capstone.hmsmenu.repository.IngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.dto.IngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.IngredientsMapper;
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
 * Service Implementation for managing {@link Ingredients}.
 */
@Service
@Transactional
public class IngredientsService {

    private final Logger log = LoggerFactory.getLogger(IngredientsService.class);

    private final IngredientsRepository ingredientsRepository;

    private final IngredientsMapper ingredientsMapper;

    public IngredientsService(IngredientsRepository ingredientsRepository, IngredientsMapper ingredientsMapper) {
        this.ingredientsRepository = ingredientsRepository;
        this.ingredientsMapper = ingredientsMapper;
    }

    /**
     * Save a ingredients.
     *
     * @param ingredientsDTO the entity to save.
     * @return the persisted entity.
     */
    public IngredientsDTO save(IngredientsDTO ingredientsDTO) {
        log.debug("Request to save Ingredients : {}", ingredientsDTO);
        Ingredients ingredients = ingredientsMapper.toEntity(ingredientsDTO);
        ingredients = ingredientsRepository.save(ingredients);
        return ingredientsMapper.toDto(ingredients);
    }

    /**
     * Update a ingredients.
     *
     * @param ingredientsDTO the entity to save.
     * @return the persisted entity.
     */
    public IngredientsDTO update(IngredientsDTO ingredientsDTO) {
        log.debug("Request to update Ingredients : {}", ingredientsDTO);
        Ingredients ingredients = ingredientsMapper.toEntity(ingredientsDTO);
        ingredients = ingredientsRepository.save(ingredients);
        return ingredientsMapper.toDto(ingredients);
    }

    /**
     * Partially update a ingredients.
     *
     * @param ingredientsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IngredientsDTO> partialUpdate(IngredientsDTO ingredientsDTO) {
        log.debug("Request to partially update Ingredients : {}", ingredientsDTO);

        return ingredientsRepository
            .findById(ingredientsDTO.getId())
            .map(existingIngredients -> {
                ingredientsMapper.partialUpdate(existingIngredients, ingredientsDTO);

                return existingIngredients;
            })
            .map(ingredientsRepository::save)
            .map(ingredientsMapper::toDto);
    }

    /**
     * Get all the ingredients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IngredientsDTO> findAll() {
        log.debug("Request to get all Ingredients");
        return ingredientsRepository.findAll().stream().map(ingredientsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the ingredients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IngredientsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ingredientsRepository.findAllWithEagerRelationships(pageable).map(ingredientsMapper::toDto);
    }

    /**
     * Get one ingredients by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IngredientsDTO> findOne(Long id) {
        log.debug("Request to get Ingredients : {}", id);
        return ingredientsRepository.findOneWithEagerRelationships(id).map(ingredientsMapper::toDto);
    }

    /**
     * Delete the ingredients by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ingredients : {}", id);
        ingredientsRepository.deleteById(id);
    }
}
