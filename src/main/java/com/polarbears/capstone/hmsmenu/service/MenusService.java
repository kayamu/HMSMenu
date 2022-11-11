package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.Menus;
import com.polarbears.capstone.hmsmenu.repository.MenusRepository;
import com.polarbears.capstone.hmsmenu.service.dto.MenusDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.MenusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Menus}.
 */
@Service
@Transactional
public class MenusService {

    private final Logger log = LoggerFactory.getLogger(MenusService.class);

    private final MenusRepository menusRepository;

    private final MenusMapper menusMapper;

    public MenusService(MenusRepository menusRepository, MenusMapper menusMapper) {
        this.menusRepository = menusRepository;
        this.menusMapper = menusMapper;
    }

    /**
     * Save a menus.
     *
     * @param menusDTO the entity to save.
     * @return the persisted entity.
     */
    public MenusDTO save(MenusDTO menusDTO) {
        log.debug("Request to save Menus : {}", menusDTO);
        Menus menus = menusMapper.toEntity(menusDTO);
        menus = menusRepository.save(menus);
        return menusMapper.toDto(menus);
    }

    /**
     * Update a menus.
     *
     * @param menusDTO the entity to save.
     * @return the persisted entity.
     */
    public MenusDTO update(MenusDTO menusDTO) {
        log.debug("Request to update Menus : {}", menusDTO);
        Menus menus = menusMapper.toEntity(menusDTO);
        menus = menusRepository.save(menus);
        return menusMapper.toDto(menus);
    }

    /**
     * Partially update a menus.
     *
     * @param menusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MenusDTO> partialUpdate(MenusDTO menusDTO) {
        log.debug("Request to partially update Menus : {}", menusDTO);

        return menusRepository
            .findById(menusDTO.getId())
            .map(existingMenus -> {
                menusMapper.partialUpdate(existingMenus, menusDTO);

                return existingMenus;
            })
            .map(menusRepository::save)
            .map(menusMapper::toDto);
    }

    /**
     * Get all the menus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MenusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Menus");
        return menusRepository.findAll(pageable).map(menusMapper::toDto);
    }

    /**
     * Get all the menus with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MenusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return menusRepository.findAllWithEagerRelationships(pageable).map(menusMapper::toDto);
    }

    /**
     * Get one menus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MenusDTO> findOne(Long id) {
        log.debug("Request to get Menus : {}", id);
        return menusRepository.findOneWithEagerRelationships(id).map(menusMapper::toDto);
    }

    /**
     * Delete the menus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Menus : {}", id);
        menusRepository.deleteById(id);
    }
}
