package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.Meals;
import com.polarbears.capstone.hmsmenu.repository.MealsRepository;
import com.polarbears.capstone.hmsmenu.service.dto.MealsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MealsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Meals}.
 */
@Service
@Transactional
public class MealsService {

    private final Logger log = LoggerFactory.getLogger(MealsService.class);

    private final MealsRepository mealsRepository;

    private final MealsMapper mealsMapper;

    public MealsService(MealsRepository mealsRepository, MealsMapper mealsMapper) {
        this.mealsRepository = mealsRepository;
        this.mealsMapper = mealsMapper;
    }

    /**
     * Save a meals.
     *
     * @param mealsDTO the entity to save.
     * @return the persisted entity.
     */
    public MealsDTO save(MealsDTO mealsDTO) {
        log.debug("Request to save Meals : {}", mealsDTO);
        Meals meals = mealsMapper.toEntity(mealsDTO);
        meals = mealsRepository.save(meals);
        return mealsMapper.toDto(meals);
    }

    /**
     * Update a meals.
     *
     * @param mealsDTO the entity to save.
     * @return the persisted entity.
     */
    public MealsDTO update(MealsDTO mealsDTO) {
        log.debug("Request to update Meals : {}", mealsDTO);
        Meals meals = mealsMapper.toEntity(mealsDTO);
        meals = mealsRepository.save(meals);
        return mealsMapper.toDto(meals);
    }

    /**
     * Partially update a meals.
     *
     * @param mealsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MealsDTO> partialUpdate(MealsDTO mealsDTO) {
        log.debug("Request to partially update Meals : {}", mealsDTO);

        return mealsRepository
            .findById(mealsDTO.getId())
            .map(existingMeals -> {
                mealsMapper.partialUpdate(existingMeals, mealsDTO);

                return existingMeals;
            })
            .map(mealsRepository::save)
            .map(mealsMapper::toDto);
    }

    /**
     * Get all the meals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MealsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Meals");
        return mealsRepository.findAll(pageable).map(mealsMapper::toDto);
    }

    /**
     * Get all the meals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MealsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mealsRepository.findAllWithEagerRelationships(pageable).map(mealsMapper::toDto);
    }

    /**
     * Get one meals by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MealsDTO> findOne(Long id) {
        log.debug("Request to get Meals : {}", id);
        return mealsRepository.findOneWithEagerRelationships(id).map(mealsMapper::toDto);
    }

    /**
     * Delete the meals by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Meals : {}", id);
        mealsRepository.deleteById(id);
    }
}
