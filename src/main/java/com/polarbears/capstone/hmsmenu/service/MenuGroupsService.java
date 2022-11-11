package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.MenuGroups;
import com.polarbears.capstone.hmsmenu.repository.MenuGroupsRepository;
import com.polarbears.capstone.hmsmenu.service.dto.MenuGroupsDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenuGroupsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuGroups}.
 */
@Service
@Transactional
public class MenuGroupsService {

    private final Logger log = LoggerFactory.getLogger(MenuGroupsService.class);

    private final MenuGroupsRepository menuGroupsRepository;

    private final MenuGroupsMapper menuGroupsMapper;

    public MenuGroupsService(MenuGroupsRepository menuGroupsRepository, MenuGroupsMapper menuGroupsMapper) {
        this.menuGroupsRepository = menuGroupsRepository;
        this.menuGroupsMapper = menuGroupsMapper;
    }

    /**
     * Save a menuGroups.
     *
     * @param menuGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuGroupsDTO save(MenuGroupsDTO menuGroupsDTO) {
        log.debug("Request to save MenuGroups : {}", menuGroupsDTO);
        MenuGroups menuGroups = menuGroupsMapper.toEntity(menuGroupsDTO);
        menuGroups = menuGroupsRepository.save(menuGroups);
        return menuGroupsMapper.toDto(menuGroups);
    }

    /**
     * Update a menuGroups.
     *
     * @param menuGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    public MenuGroupsDTO update(MenuGroupsDTO menuGroupsDTO) {
        log.debug("Request to update MenuGroups : {}", menuGroupsDTO);
        MenuGroups menuGroups = menuGroupsMapper.toEntity(menuGroupsDTO);
        menuGroups = menuGroupsRepository.save(menuGroups);
        return menuGroupsMapper.toDto(menuGroups);
    }

    /**
     * Partially update a menuGroups.
     *
     * @param menuGroupsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MenuGroupsDTO> partialUpdate(MenuGroupsDTO menuGroupsDTO) {
        log.debug("Request to partially update MenuGroups : {}", menuGroupsDTO);

        return menuGroupsRepository
            .findById(menuGroupsDTO.getId())
            .map(existingMenuGroups -> {
                menuGroupsMapper.partialUpdate(existingMenuGroups, menuGroupsDTO);

                return existingMenuGroups;
            })
            .map(menuGroupsRepository::save)
            .map(menuGroupsMapper::toDto);
    }

    /**
     * Get all the menuGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuGroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MenuGroups");
        return menuGroupsRepository.findAll(pageable).map(menuGroupsMapper::toDto);
    }

    /**
     * Get all the menuGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MenuGroupsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return menuGroupsRepository.findAllWithEagerRelationships(pageable).map(menuGroupsMapper::toDto);
    }

    /**
     * Get one menuGroups by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MenuGroupsDTO> findOne(Long id) {
        log.debug("Request to get MenuGroups : {}", id);
        return menuGroupsRepository.findOneWithEagerRelationships(id).map(menuGroupsMapper::toDto);
    }

    /**
     * Delete the menuGroups by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MenuGroups : {}", id);
        menuGroupsRepository.deleteById(id);
    }
}
