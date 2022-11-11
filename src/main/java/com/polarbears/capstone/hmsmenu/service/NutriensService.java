package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.Nutriens;
import com.polarbears.capstone.hmsmenu.repository.NutriensRepository;
import com.polarbears.capstone.hmsmenu.service.dto.NutriensDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.NutriensMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Nutriens}.
 */
@Service
@Transactional
public class NutriensService {

    private final Logger log = LoggerFactory.getLogger(NutriensService.class);

    private final NutriensRepository nutriensRepository;

    private final NutriensMapper nutriensMapper;

    public NutriensService(NutriensRepository nutriensRepository, NutriensMapper nutriensMapper) {
        this.nutriensRepository = nutriensRepository;
        this.nutriensMapper = nutriensMapper;
    }

    /**
     * Save a nutriens.
     *
     * @param nutriensDTO the entity to save.
     * @return the persisted entity.
     */
    public NutriensDTO save(NutriensDTO nutriensDTO) {
        log.debug("Request to save Nutriens : {}", nutriensDTO);
        Nutriens nutriens = nutriensMapper.toEntity(nutriensDTO);
        nutriens = nutriensRepository.save(nutriens);
        return nutriensMapper.toDto(nutriens);
    }

    /**
     * Update a nutriens.
     *
     * @param nutriensDTO the entity to save.
     * @return the persisted entity.
     */
    public NutriensDTO update(NutriensDTO nutriensDTO) {
        log.debug("Request to update Nutriens : {}", nutriensDTO);
        Nutriens nutriens = nutriensMapper.toEntity(nutriensDTO);
        nutriens = nutriensRepository.save(nutriens);
        return nutriensMapper.toDto(nutriens);
    }

    /**
     * Partially update a nutriens.
     *
     * @param nutriensDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NutriensDTO> partialUpdate(NutriensDTO nutriensDTO) {
        log.debug("Request to partially update Nutriens : {}", nutriensDTO);

        return nutriensRepository
            .findById(nutriensDTO.getId())
            .map(existingNutriens -> {
                nutriensMapper.partialUpdate(existingNutriens, nutriensDTO);

                return existingNutriens;
            })
            .map(nutriensRepository::save)
            .map(nutriensMapper::toDto);
    }

    /**
     * Get all the nutriens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NutriensDTO> findAll() {
        log.debug("Request to get all Nutriens");
        return nutriensRepository.findAll().stream().map(nutriensMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one nutriens by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NutriensDTO> findOne(Long id) {
        log.debug("Request to get Nutriens : {}", id);
        return nutriensRepository.findById(id).map(nutriensMapper::toDto);
    }

    /**
     * Delete the nutriens by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Nutriens : {}", id);
        nutriensRepository.deleteById(id);
    }
}
