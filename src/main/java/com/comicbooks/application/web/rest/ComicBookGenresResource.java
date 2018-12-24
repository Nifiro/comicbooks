package com.comicbooks.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comicbooks.application.service.ComicBookGenresService;
import com.comicbooks.application.web.rest.errors.BadRequestAlertException;
import com.comicbooks.application.web.rest.util.HeaderUtil;
import com.comicbooks.application.web.rest.util.PaginationUtil;
import com.comicbooks.application.service.dto.ComicBookGenresDTO;
import com.comicbooks.application.service.dto.ComicBookGenresCriteria;
import com.comicbooks.application.service.ComicBookGenresQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ComicBookGenres.
 */
@RestController
@RequestMapping("/api")
public class ComicBookGenresResource {

    private final Logger log = LoggerFactory.getLogger(ComicBookGenresResource.class);

    private static final String ENTITY_NAME = "comicBookGenres";

    private final ComicBookGenresService comicBookGenresService;

    private final ComicBookGenresQueryService comicBookGenresQueryService;

    public ComicBookGenresResource(ComicBookGenresService comicBookGenresService, ComicBookGenresQueryService comicBookGenresQueryService) {
        this.comicBookGenresService = comicBookGenresService;
        this.comicBookGenresQueryService = comicBookGenresQueryService;
    }

    /**
     * POST  /comic-book-genres : Create a new comicBookGenres.
     *
     * @param comicBookGenresDTO the comicBookGenresDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comicBookGenresDTO, or with status 400 (Bad Request) if the comicBookGenres has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comic-book-genres")
    @Timed
    public ResponseEntity<ComicBookGenresDTO> createComicBookGenres(@RequestBody ComicBookGenresDTO comicBookGenresDTO) throws URISyntaxException {
        log.debug("REST request to save ComicBookGenres : {}", comicBookGenresDTO);
        if (comicBookGenresDTO.getId() != null) {
            throw new BadRequestAlertException("A new comicBookGenres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComicBookGenresDTO result = comicBookGenresService.save(comicBookGenresDTO);
        return ResponseEntity.created(new URI("/api/comic-book-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comic-book-genres : Updates an existing comicBookGenres.
     *
     * @param comicBookGenresDTO the comicBookGenresDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comicBookGenresDTO,
     * or with status 400 (Bad Request) if the comicBookGenresDTO is not valid,
     * or with status 500 (Internal Server Error) if the comicBookGenresDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comic-book-genres")
    @Timed
    public ResponseEntity<ComicBookGenresDTO> updateComicBookGenres(@RequestBody ComicBookGenresDTO comicBookGenresDTO) throws URISyntaxException {
        log.debug("REST request to update ComicBookGenres : {}", comicBookGenresDTO);
        if (comicBookGenresDTO.getId() == null) {
            return createComicBookGenres(comicBookGenresDTO);
        }
        ComicBookGenresDTO result = comicBookGenresService.save(comicBookGenresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comicBookGenresDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comic-book-genres : get all the comicBookGenres.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of comicBookGenres in body
     */
    @GetMapping("/comic-book-genres")
    @Timed
    public ResponseEntity<List<ComicBookGenresDTO>> getAllComicBookGenres(ComicBookGenresCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ComicBookGenres by criteria: {}", criteria);
        Page<ComicBookGenresDTO> page = comicBookGenresQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comic-book-genres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /comic-book-genres/:id : get the "id" comicBookGenres.
     *
     * @param id the id of the comicBookGenresDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comicBookGenresDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comic-book-genres/{id}")
    @Timed
    public ResponseEntity<ComicBookGenresDTO> getComicBookGenres(@PathVariable Long id) {
        log.debug("REST request to get ComicBookGenres : {}", id);
        ComicBookGenresDTO comicBookGenresDTO = comicBookGenresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(comicBookGenresDTO));
    }

    /**
     * DELETE  /comic-book-genres/:id : delete the "id" comicBookGenres.
     *
     * @param id the id of the comicBookGenresDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comic-book-genres/{id}")
    @Timed
    public ResponseEntity<Void> deleteComicBookGenres(@PathVariable Long id) {
        log.debug("REST request to delete ComicBookGenres : {}", id);
        comicBookGenresService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
