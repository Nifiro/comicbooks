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

import com.comicbooks.application.domain.Chapter;
import com.comicbooks.application.domain.*; // for static metamodels
import com.comicbooks.application.repository.ChapterRepository;
import com.comicbooks.application.service.dto.ChapterCriteria;

import com.comicbooks.application.service.dto.ChapterDTO;
import com.comicbooks.application.service.mapper.ChapterMapper;

/**
 * Service for executing complex queries for Chapter entities in the database.
 * The main input is a {@link ChapterCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChapterDTO} or a {@link Page} of {@link ChapterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChapterQueryService extends QueryService<Chapter> {

    private final Logger log = LoggerFactory.getLogger(ChapterQueryService.class);


    private final ChapterRepository chapterRepository;

    private final ChapterMapper chapterMapper;

    public ChapterQueryService(ChapterRepository chapterRepository, ChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterMapper = chapterMapper;
    }

    /**
     * Return a {@link List} of {@link ChapterDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChapterDTO> findByCriteria(ChapterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Chapter> specification = createSpecification(criteria);
        return chapterMapper.toDto(chapterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChapterDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChapterDTO> findByCriteria(ChapterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Chapter> specification = createSpecification(criteria);
        final Page<Chapter> result = chapterRepository.findAll(specification, page);
        return result.map(chapterMapper::toDto);
    }

    /**
     * Function to convert ChapterCriteria to a {@link Specifications}
     */
    private Specifications<Chapter> createSpecification(ChapterCriteria criteria) {
        Specifications<Chapter> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Chapter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Chapter_.name));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Chapter_.number));
            }
            if (criteria.getVolume() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVolume(), Chapter_.volume));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), Chapter_.filePath));
            }
            if (criteria.getPages() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPages(), Chapter_.pages));
            }
            if (criteria.getReleaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReleaseDate(), Chapter_.releaseDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Chapter_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Chapter_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Chapter_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Chapter_.lastModifiedDate));
            }
        }
        return specification;
    }

}
