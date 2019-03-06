package com.comicbooks.application.service;

import com.comicbooks.application.domain.Series;
import com.comicbooks.application.repository.SeriesRepository;
import com.comicbooks.application.service.dto.SeriesDTO;
import com.comicbooks.application.service.mapper.SeriesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Series.
 */
@Service
@Transactional
public class SeriesService {

    private final Logger log = LoggerFactory.getLogger(SeriesService.class);

    private final SeriesRepository seriesRepository;

    private final SeriesMapper seriesMapper;

    public SeriesService(SeriesRepository seriesRepository, SeriesMapper seriesMapper) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
    }

    /**
     * Save a series.
     *
     * @param seriesDTO the entity to save
     * @return the persisted entity
     */
    public SeriesDTO save(SeriesDTO seriesDTO) {
        log.debug("Request to save Series : {}", seriesDTO);
        Series series = seriesMapper.toEntity(seriesDTO);
        series = seriesRepository.save(series);
        return seriesMapper.toDto(series);
    }

    /**
     * Get all the series.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SeriesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Series");
        return seriesRepository.findAll(pageable)
            .map(seriesMapper::toDto);
    }

    /**
     * Get one series by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SeriesDTO findOne(Long id) {
        log.debug("Request to get Series : {}", id);
        Series series = seriesRepository.findOne(id);
        return seriesMapper.toDto(series);
    }

    /**
     * Delete the series by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Series : {}", id);
        seriesRepository.delete(id);
    }
}
