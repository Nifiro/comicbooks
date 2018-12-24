package com.comicbooks.application.service;

import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.repository.ComicBookRepository;
import com.comicbooks.application.service.dto.ComicBookDTO;
import com.comicbooks.application.service.mapper.ComicBookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ComicBook.
 */
@Service
@Transactional
public class ComicBookService {

    private final Logger log = LoggerFactory.getLogger(ComicBookService.class);

    private final ComicBookRepository comicBookRepository;

    private final ComicBookMapper comicBookMapper;

    public ComicBookService(ComicBookRepository comicBookRepository, ComicBookMapper comicBookMapper) {
        this.comicBookRepository = comicBookRepository;
        this.comicBookMapper = comicBookMapper;
    }

    /**
     * Save a comicBook.
     *
     * @param comicBookDTO the entity to save
     * @return the persisted entity
     */
    public ComicBookDTO save(ComicBookDTO comicBookDTO) {
        log.debug("Request to save ComicBook : {}", comicBookDTO);
        ComicBook comicBook = comicBookMapper.toEntity(comicBookDTO);
        comicBook = comicBookRepository.save(comicBook);
        return comicBookMapper.toDto(comicBook);
    }

    /**
     * Get all the comicBooks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComicBookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ComicBooks");
        return comicBookRepository.findAll(pageable)
            .map(comicBookMapper::toDto);
    }

    /**
     * Get one comicBook by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ComicBookDTO findOne(Long id) {
        log.debug("Request to get ComicBook : {}", id);
        ComicBook comicBook = comicBookRepository.findOne(id);
        return comicBookMapper.toDto(comicBook);
    }

    /**
     * Delete the comicBook by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ComicBook : {}", id);
        comicBookRepository.delete(id);
    }
}
