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

import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.domain.*; // for static metamodels
import com.comicbooks.application.repository.ComicBookRepository;
import com.comicbooks.application.service.dto.ComicBookCriteria;

import com.comicbooks.application.service.dto.ComicBookDTO;
import com.comicbooks.application.service.mapper.ComicBookMapper;
import com.comicbooks.application.domain.enumeration.Status;

/**
 * Service for executing complex queries for ComicBook entities in the database.
 * The main input is a {@link ComicBookCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComicBookDTO} or a {@link Page} of {@link ComicBookDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComicBookQueryService extends QueryService<ComicBook> {

    private final Logger log = LoggerFactory.getLogger(ComicBookQueryService.class);


    private final ComicBookRepository comicBookRepository;

    private final ComicBookMapper comicBookMapper;

    public ComicBookQueryService(ComicBookRepository comicBookRepository, ComicBookMapper comicBookMapper) {
        this.comicBookRepository = comicBookRepository;
        this.comicBookMapper = comicBookMapper;
    }

    /**
     * Return a {@link List} of {@link ComicBookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComicBookDTO> findByCriteria(ComicBookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ComicBook> specification = createSpecification(criteria);
        return comicBookMapper.toDto(comicBookRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComicBookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComicBookDTO> findByCriteria(ComicBookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ComicBook> specification = createSpecification(criteria);
        final Page<ComicBook> result = comicBookRepository.findAll(specification, page);
        return result.map(comicBookMapper::toDto);
    }

    /**
     * Function to convert ComicBookCriteria to a {@link Specifications}
     */
    private Specifications<ComicBook> createSpecification(ComicBookCriteria criteria) {
        Specifications<ComicBook> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ComicBook_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ComicBook_.title));
            }
            if (criteria.getChapters() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getChapters(), ComicBook_.chapters));
            }
            if (criteria.getPublisher() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPublisher(), ComicBook_.publisher));
            }
            if (criteria.getSerializedFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSerializedFrom(), ComicBook_.serializedFrom));
            }
            if (criteria.getSerializedTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSerializedTo(), ComicBook_.serializedTo));
            }
            if (criteria.getImagePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagePath(), ComicBook_.imagePath));
            }
            if (criteria.getCoverPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverPath(), ComicBook_.coverPath));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ComicBook_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ComicBook_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ComicBook_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ComicBook_.lastModifiedDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ComicBook_.status));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAuthorId(), ComicBook_.author, Author_.id));
            }
        }
        return specification;
    }

}
