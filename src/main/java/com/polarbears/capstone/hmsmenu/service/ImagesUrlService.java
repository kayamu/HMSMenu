package com.polarbears.capstone.hmsmenu.service;

import com.polarbears.capstone.hmsmenu.domain.ImagesUrl;
import com.polarbears.capstone.hmsmenu.repository.ImagesUrlRepository;
import com.polarbears.capstone.hmsmenu.service.dto.ImagesUrlDTO;
import com.polarbears.capstone.hmsmenu.service.mapper.ImagesUrlMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImagesUrl}.
 */
@Service
@Transactional
public class ImagesUrlService {

    private final Logger log = LoggerFactory.getLogger(ImagesUrlService.class);

    private final ImagesUrlRepository imagesUrlRepository;

    private final ImagesUrlMapper imagesUrlMapper;

    public ImagesUrlService(ImagesUrlRepository imagesUrlRepository, ImagesUrlMapper imagesUrlMapper) {
        this.imagesUrlRepository = imagesUrlRepository;
        this.imagesUrlMapper = imagesUrlMapper;
    }

    /**
     * Save a imagesUrl.
     *
     * @param imagesUrlDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesUrlDTO save(ImagesUrlDTO imagesUrlDTO) {
        log.debug("Request to save ImagesUrl : {}", imagesUrlDTO);
        ImagesUrl imagesUrl = imagesUrlMapper.toEntity(imagesUrlDTO);
        imagesUrl = imagesUrlRepository.save(imagesUrl);
        return imagesUrlMapper.toDto(imagesUrl);
    }

    /**
     * Update a imagesUrl.
     *
     * @param imagesUrlDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesUrlDTO update(ImagesUrlDTO imagesUrlDTO) {
        log.debug("Request to update ImagesUrl : {}", imagesUrlDTO);
        ImagesUrl imagesUrl = imagesUrlMapper.toEntity(imagesUrlDTO);
        imagesUrl = imagesUrlRepository.save(imagesUrl);
        return imagesUrlMapper.toDto(imagesUrl);
    }

    /**
     * Partially update a imagesUrl.
     *
     * @param imagesUrlDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImagesUrlDTO> partialUpdate(ImagesUrlDTO imagesUrlDTO) {
        log.debug("Request to partially update ImagesUrl : {}", imagesUrlDTO);

        return imagesUrlRepository
            .findById(imagesUrlDTO.getId())
            .map(existingImagesUrl -> {
                imagesUrlMapper.partialUpdate(existingImagesUrl, imagesUrlDTO);

                return existingImagesUrl;
            })
            .map(imagesUrlRepository::save)
            .map(imagesUrlMapper::toDto);
    }

    /**
     * Get all the imagesUrls.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ImagesUrlDTO> findAll() {
        log.debug("Request to get all ImagesUrls");
        return imagesUrlRepository.findAll().stream().map(imagesUrlMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one imagesUrl by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImagesUrlDTO> findOne(Long id) {
        log.debug("Request to get ImagesUrl : {}", id);
        return imagesUrlRepository.findById(id).map(imagesUrlMapper::toDto);
    }

    /**
     * Delete the imagesUrl by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ImagesUrl : {}", id);
        imagesUrlRepository.deleteById(id);
    }
}
