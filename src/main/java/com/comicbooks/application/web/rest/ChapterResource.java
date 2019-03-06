package com.comicbooks.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comicbooks.application.service.ChapterService;
import com.comicbooks.application.web.rest.errors.BadRequestAlertException;
import com.comicbooks.application.web.rest.util.HeaderUtil;
import com.comicbooks.application.web.rest.util.PaginationUtil;
import com.comicbooks.application.service.dto.ChapterDTO;
import com.comicbooks.application.service.dto.ChapterCriteria;
import com.comicbooks.application.service.ChapterQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Chapter.
 */
@RestController
@RequestMapping("/api")
public class ChapterResource {

    private final Logger log = LoggerFactory.getLogger(ChapterResource.class);

    private static final String ENTITY_NAME = "chapter";

    private final ChapterService chapterService;

    private final ChapterQueryService chapterQueryService;

    public ChapterResource(ChapterService chapterService, ChapterQueryService chapterQueryService) {
        this.chapterService = chapterService;
        this.chapterQueryService = chapterQueryService;
    }

    /**
     * POST  /chapters : Create a new chapter.
     *
     * @param chapterDTO the chapterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chapterDTO, or with status 400 (Bad Request) if the chapter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chapters")
    @Timed
    public ResponseEntity<ChapterDTO> createChapter(@Valid @RequestBody ChapterDTO chapterDTO) throws URISyntaxException {
        log.debug("REST request to save Chapter : {}", chapterDTO);
        if (chapterDTO.getId() != null) {
            throw new BadRequestAlertException("A new chapter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChapterDTO result = chapterService.save(chapterDTO);
        return ResponseEntity.created(new URI("/api/chapters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chapters : Updates an existing chapter.
     *
     * @param chapterDTO the chapterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chapterDTO,
     * or with status 400 (Bad Request) if the chapterDTO is not valid,
     * or with status 500 (Internal Server Error) if the chapterDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chapters")
    @Timed
    public ResponseEntity<ChapterDTO> updateChapter(@Valid @RequestBody ChapterDTO chapterDTO) throws URISyntaxException {
        log.debug("REST request to update Chapter : {}", chapterDTO);
        if (chapterDTO.getId() == null) {
            return createChapter(chapterDTO);
        }
        ChapterDTO result = chapterService.save(chapterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chapterDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chapters : get all the chapters.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of chapters in body
     */
    @GetMapping("/chapters")
    @Timed
    public ResponseEntity<List<ChapterDTO>> getAllChapters(ChapterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Chapters by criteria: {}", criteria);
        Page<ChapterDTO> page = chapterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chapters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chapters/:id : get the "id" chapter.
     *
     * @param id the id of the chapterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chapterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chapters/{id}")
    @Timed
    public ResponseEntity<ChapterDTO> getChapter(@PathVariable Long id) {
        log.debug("REST request to get Chapter : {}", id);
        ChapterDTO chapterDTO = chapterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chapterDTO));
    }

    /**
     * DELETE  /chapters/:id : delete the "id" chapter.
     *
     * @param id the id of the chapterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chapters/{id}")
    @Timed
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        log.debug("REST request to delete Chapter : {}", id);
        chapterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
