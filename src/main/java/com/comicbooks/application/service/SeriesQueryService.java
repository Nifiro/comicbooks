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

import com.comicbooks.application.domain.Series;
import com.comicbooks.application.domain.*; // for static metamodels
import com.comicbooks.application.repository.SeriesRepository;
import com.comicbooks.application.service.dto.SeriesCriteria;

import com.comicbooks.application.service.dto.SeriesDTO;
import com.comicbooks.application.service.mapper.SeriesMapper;

/**
 * Service for executing complex queries for Series entities in the database.
 * The main input is a {@link SeriesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SeriesDTO} or a {@link Page} of {@link SeriesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SeriesQueryService extends QueryService<Series> {

    private final Logger log = LoggerFactory.getLogger(SeriesQueryService.class);


    private final SeriesRepository seriesRepository;

    private final SeriesMapper seriesMapper;

    public SeriesQueryService(SeriesRepository seriesRepository, SeriesMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    /**
     * Return a {@link List} of {@link SeriesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SeriesDTO> findByCriteria(SeriesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Series> specification = createSpecification(criteria);
        return seriesMapper.toDto(seriesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SeriesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SeriesDTO> findByCriteria(SeriesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Series> specification = createSpecification(criteria);
        final Page<Series> result = seriesRepository.findAll(specification, page);
        return result.map(seriesMapper::toDto);
    }

    /**
     * Function to convert SeriesCriteria to a {@link Specifications}
     */
    private Specifications<Series> createSpecification(SeriesCriteria criteria) {
        Specifications<Series> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Series_.id));
            }
            if (criteria.getComicBookId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getComicBookId(), Series_.comicBook, ComicBook_.id));
            }
            if (criteria.getChapterId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getChapterId(), Series_.chapter, Chapter_.id));
            }
        }
        return specification;
    }

}
