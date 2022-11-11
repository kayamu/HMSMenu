package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.MealIngredients;
import com.polarbears.capstone.hmsmenu.repository.MealIngredientsRepository;
import com.polarbears.capstone.hmsmenu.service.dto.MealIngredientsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealIngredientsMapper;
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
 * Service Implementation for managing {@link MealIngredients}.
 */
@Service
@Transactional
public class MealIngredientsService {

    private final Logger log = LoggerFactory.getLogger(MealIngredientsService.class);

    private final MealIngredientsRepository mealIngredientsRepository;

    private final MealIngredientsMapper mealIngredientsMapper;

    public MealIngredientsService(MealIngredientsRepository mealIngredientsRepository, MealIngredientsMapper mealIngredientsMapper) {
        this.mealIngredientsRepository = mealIngredientsRepository;
        this.mealIngredientsMapper = mealIngredientsMapper;
    }

    /**
     * Save a mealIngredients.
     *
     * @param mealIngredientsDTO the entity to save.
     * @return the persisted entity.
     */
    public MealIngredientsDTO save(MealIngredientsDTO mealIngredientsDTO) {
        log.debug("Request to save MealIngredients : {}", mealIngredientsDTO);
        MealIngredients mealIngredients = mealIngredientsMapper.toEntity(mealIngredientsDTO);
        mealIngredients = mealIngredientsRepository.save(mealIngredients);
        return mealIngredientsMapper.toDto(mealIngredients);
    }

    /**
     * Update a mealIngredients.
     *
     * @param mealIngredientsDTO the entity to save.
     * @return the persisted entity.
     */
    public MealIngredientsDTO update(MealIngredientsDTO mealIngredientsDTO) {
        log.debug("Request to update MealIngredients : {}", mealIngredientsDTO);
        MealIngredients mealIngredients = mealIngredientsMapper.toEntity(mealIngredientsDTO);
        mealIngredients = mealIngredientsRepository.save(mealIngredients);
        return mealIngredientsMapper.toDto(mealIngredients);
    }

    /**
     * Partially update a mealIngredients.
     *
     * @param mealIngredientsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MealIngredientsDTO> partialUpdate(MealIngredientsDTO mealIngredientsDTO) {
        log.debug("Request to partially update MealIngredients : {}", mealIngredientsDTO);

        return mealIngredientsRepository
            .findById(mealIngredientsDTO.getId())
            .map(existingMealIngredients -> {
                mealIngredientsMapper.partialUpdate(existingMealIngredients, mealIngredientsDTO);

                return existingMealIngredients;
            })
            .map(mealIngredientsRepository::save)
            .map(mealIngredientsMapper::toDto);
    }

    /**
     * Get all the mealIngredients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MealIngredientsDTO> findAll() {
        log.debug("Request to get all MealIngredients");
        return mealIngredientsRepository
            .findAll()
            .stream()
            .map(mealIngredientsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the mealIngredients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MealIngredientsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mealIngredientsRepository.findAllWithEagerRelationships(pageable).map(mealIngredientsMapper::toDto);
    }

    /**
     * Get one mealIngredients by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MealIngredientsDTO> findOne(Long id) {
        log.debug("Request to get MealIngredients : {}", id);
        return mealIngredientsRepository.findOneWithEagerRelationships(id).map(mealIngredientsMapper::toDto);
    }

    /**
     * Delete the mealIngredients by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MealIngredients : {}", id);
        mealIngredientsRepository.deleteById(id);
    }
}
