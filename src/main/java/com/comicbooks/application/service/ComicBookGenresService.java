package com.comicbooks.application.service;

import com.comicbooks.application.domain.ComicBookGenres;
import com.comicbooks.application.repository.ComicBookGenresRepository;
import com.comicbooks.application.service.dto.ComicBookGenresDTO;
import com.comicbooks.application.service.mapper.ComicBookGenresMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ComicBookGenres.
 */
@Service
@Transactional
public class ComicBookGenresService {

    private final Logger log = LoggerFactory.getLogger(ComicBookGenresService.class);

    private final ComicBookGenresRepository comicBookGenresRepository;

    private final ComicBookGenresMapper comicBookGenresMapper;

    public ComicBookGenresService(ComicBookGenresRepository comicBookGenresRepository, ComicBookGenresMapper comicBookGenresMapper) {
        this.comicBookGenresRepository = comicBookGenresRepository;
        this.comicBookGenresMapper = comicBookGenresMapper;
    }

    /**
     * Save a comicBookGenres.
     *
     * @param comicBookGenresDTO the entity to save
     * @return the persisted entity
     */
    public ComicBookGenresDTO save(ComicBookGenresDTO comicBookGenresDTO) {
        log.debug("Request to save ComicBookGenres : {}", comicBookGenresDTO);
        ComicBookGenres comicBookGenres = comicBookGenresMapper.toEntity(comicBookGenresDTO);
        comicBookGenres = comicBookGenresRepository.save(comicBookGenres);
        return comicBookGenresMapper.toDto(comicBookGenres);
    }

    /**
     * Get all the comicBookGenres.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComicBookGenresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ComicBookGenres");
        return comicBookGenresRepository.findAll(pageable)
            .map(comicBookGenresMapper::toDto);
    }

    /**
     * Get one comicBookGenres by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ComicBookGenresDTO findOne(Long id) {
        log.debug("Request to get ComicBookGenres : {}", id);
        ComicBookGenres comicBookGenres = comicBookGenresRepository.findOne(id);
        return comicBookGenresMapper.toDto(comicBookGenres);
    }

    /**
     * Delete the comicBookGenres by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ComicBookGenres : {}", id);
        comicBookGenresRepository.delete(id);
    }
}
