package com.comicbooks.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comicbooks.application.service.SeriesService;
import com.comicbooks.application.web.rest.errors.BadRequestAlertException;
import com.comicbooks.application.web.rest.util.HeaderUtil;
import com.comicbooks.application.web.rest.util.PaginationUtil;
import com.comicbooks.application.service.dto.SeriesDTO;
import com.comicbooks.application.service.dto.SeriesCriteria;
import com.comicbooks.application.service.SeriesQueryService;
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
 * REST controller for managing Series.
 */
@RestController
@RequestMapping("/api")
public class SeriesResource {

    private final Logger log = LoggerFactory.getLogger(SeriesResource.class);

    private static final String ENTITY_NAME = "series";

    private final SeriesService seriesService;

    private final SeriesQueryService seriesQueryService;

    public SeriesResource(SeriesService seriesService, SeriesQueryService seriesQueryService) {
        this.seriesService = seriesService;
        this.seriesQueryService = seriesQueryService;
    }

    /**
     * POST  /series : Create a new series.
     *
     * @param seriesDTO the seriesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seriesDTO, or with status 400 (Bad Request) if the series has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/series")
    @Timed
    public ResponseEntity<SeriesDTO> createSeries(@RequestBody SeriesDTO seriesDTO) throws URISyntaxException {
        log.debug("REST request to save Series : {}", seriesDTO);
        if (seriesDTO.getId() != null) {
            throw new BadRequestAlertException("A new series cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeriesDTO result = seriesService.save(seriesDTO);
        return ResponseEntity.created(new URI("/api/series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /series : Updates an existing series.
     *
     * @param seriesDTO the seriesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seriesDTO,
     * or with status 400 (Bad Request) if the seriesDTO is not valid,
     * or with status 500 (Internal Server Error) if the seriesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/series")
    @Timed
    public ResponseEntity<SeriesDTO> updateSeries(@RequestBody SeriesDTO seriesDTO) throws URISyntaxException {
        log.debug("REST request to update Series : {}", seriesDTO);
        if (seriesDTO.getId() == null) {
            return createSeries(seriesDTO);
        }
        SeriesDTO result = seriesService.save(seriesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, seriesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /series : get all the series.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of series in body
     */
    @GetMapping("/series")
    @Timed
    public ResponseEntity<List<SeriesDTO>> getAllSeries(SeriesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Series by criteria: {}", criteria);
        Page<SeriesDTO> page = seriesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/series");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /series/:id : get the "id" series.
     *
     * @param id the id of the seriesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seriesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/series/{id}")
    @Timed
    public ResponseEntity<SeriesDTO> getSeries(@PathVariable Long id) {
        log.debug("REST request to get Series : {}", id);
        SeriesDTO seriesDTO = seriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(seriesDTO));
    }

    /**
     * DELETE  /series/:id : delete the "id" series.
     *
     * @param id the id of the seriesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/series/{id}")
    @Timed
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        log.debug("REST request to delete Series : {}", id);
        seriesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
