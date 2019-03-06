package com.comicbooks.application.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.comicbooks.application.domain.ComicBookGenres;
import com.comicbooks.application.domain.*; // for static metamodels
import com.comicbooks.application.repository.ComicBookGenresRepository;
import com.comicbooks.application.service.dto.ComicBookGenresCriteria;

import com.comicbooks.application.service.dto.ComicBookGenresDTO;
import com.comicbooks.application.service.mapper.ComicBookGenresMapper;

/**
 * Service for executing complex queries for ComicBookGenres entities in the database.
 * The main input is a {@link ComicBookGenresCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComicBookGenresDTO} or a {@link Page} of {@link ComicBookGenresDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComicBookGenresQueryService extends QueryService<ComicBookGenres> {

    private final Logger log = LoggerFactory.getLogger(ComicBookGenresQueryService.class);


    private final ComicBookGenresRepository comicBookGenresRepository;

    private final ComicBookGenresMapper comicBookGenresMapper;

    public ComicBookGenresQueryService(ComicBookGenresRepository comicBookGenresRepository, ComicBookGenresMapper comicBookGenresMapper) {
        this.comicBookGenresRepository = comicBookGenresRepository;
        this.comicBookGenresMapper = comicBookGenresMapper;
    }

    /**
     * Return a {@link List} of {@link ComicBookGenresDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComicBookGenresDTO> findByCriteria(ComicBookGenresCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ComicBookGenres> specification = createSpecification(criteria);
        return comicBookGenresMapper.toDto(comicBookGenresRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComicBookGenresDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComicBookGenresDTO> findByCriteria(ComicBookGenresCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ComicBookGenres> specification = createSpecification(criteria);
        final Page<ComicBookGenres> result = comicBookGenresRepository.findAll(specification, page);
        return result.map(comicBookGenresMapper::toDto);
    }

    /**
     * Function to convert ComicBookGenresCriteria to a {@link Specifications}
     */
    private Specifications<ComicBookGenres> createSpecification(ComicBookGenresCriteria criteria) {
        Specifications<ComicBookGenres> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ComicBookGenres_.id));
            }
            if (criteria.getComicBookId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getComicBookId(), ComicBookGenres_.comicBook, ComicBook_.id));
            }
            if (criteria.getGenreId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGenreId(), ComicBookGenres_.genre, Genre_.id));
            }
        }
        return specification;
    }

}
