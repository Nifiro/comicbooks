package com.comicbooks.application.web.rest;

import com.comicbooks.application.ComicbooksApp;

import com.comicbooks.application.domain.Series;
import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.domain.Chapter;
import com.comicbooks.application.repository.SeriesRepository;
import com.comicbooks.application.service.SeriesService;
import com.comicbooks.application.service.dto.SeriesDTO;
import com.comicbooks.application.service.mapper.SeriesMapper;
import com.comicbooks.application.web.rest.errors.ExceptionTranslator;
import com.comicbooks.application.service.dto.SeriesCriteria;
import com.comicbooks.application.service.SeriesQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.comicbooks.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SeriesResource REST controller.
 *
 * @see SeriesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicbooksApp.class)
public class SeriesResourceIntTest {

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private SeriesMapper seriesMapper;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesQueryService seriesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSeriesMockMvc;

    private Series series;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SeriesResource seriesResource = new SeriesResource(seriesService, seriesQueryService);
        this.restSeriesMockMvc = MockMvcBuilders.standaloneSetup(seriesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Series createEntity(EntityManager em) {
        Series series = new Series();
        return series;
    }

    @Before
    public void initTest() {
        series = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeries() throws Exception {
        int databaseSizeBeforeCreate = seriesRepository.findAll().size();

        // Create the Series
        SeriesDTO seriesDTO = seriesMapper.toDto(series);
        restSeriesMockMvc.perform(post("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seriesDTO)))
            .andExpect(status().isCreated());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeCreate + 1);
        Series testSeries = seriesList.get(seriesList.size() - 1);
    }

    @Test
    @Transactional
    public void createSeriesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seriesRepository.findAll().size();

        // Create the Series with an existing ID
        series.setId(1L);
        SeriesDTO seriesDTO = seriesMapper.toDto(series);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeriesMockMvc.perform(post("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seriesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);

        // Get all the seriesList
        restSeriesMockMvc.perform(get("/api/series?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(series.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);

        // Get the series
        restSeriesMockMvc.perform(get("/api/series/{id}", series.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(series.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllSeriesByComicBookIsEqualToSomething() throws Exception {
        // Initialize the database
        ComicBook comicBook = ComicBookResourceIntTest.createEntity(em);
        em.persist(comicBook);
        em.flush();
        series.setComicBook(comicBook);
        seriesRepository.saveAndFlush(series);
        Long comicBookId = comicBook.getId();

        // Get all the seriesList where comicBook equals to comicBookId
        defaultSeriesShouldBeFound("comicBookId.equals=" + comicBookId);

        // Get all the seriesList where comicBook equals to comicBookId + 1
        defaultSeriesShouldNotBeFound("comicBookId.equals=" + (comicBookId + 1));
    }


    @Test
    @Transactional
    public void getAllSeriesByChapterIsEqualToSomething() throws Exception {
        // Initialize the database
        Chapter chapter = ChapterResourceIntTest.createEntity(em);
        em.persist(chapter);
        em.flush();
        series.setChapter(chapter);
        seriesRepository.saveAndFlush(series);
        Long chapterId = chapter.getId();

        // Get all the seriesList where chapter equals to chapterId
        defaultSeriesShouldBeFound("chapterId.equals=" + chapterId);

        // Get all the seriesList where chapter equals to chapterId + 1
        defaultSeriesShouldNotBeFound("chapterId.equals=" + (chapterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSeriesShouldBeFound(String filter) throws Exception {
        restSeriesMockMvc.perform(get("/api/series?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(series.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSeriesShouldNotBeFound(String filter) throws Exception {
        restSeriesMockMvc.perform(get("/api/series?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSeries() throws Exception {
        // Get the series
        restSeriesMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);
        int databaseSizeBeforeUpdate = seriesRepository.findAll().size();

        // Update the series
        Series updatedSeries = seriesRepository.findOne(series.getId());
        // Disconnect from session so that the updates on updatedSeries are not directly saved in db
        em.detach(updatedSeries);
        SeriesDTO seriesDTO = seriesMapper.toDto(updatedSeries);

        restSeriesMockMvc.perform(put("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seriesDTO)))
            .andExpect(status().isOk());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeUpdate);
        Series testSeries = seriesList.get(seriesList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSeries() throws Exception {
        int databaseSizeBeforeUpdate = seriesRepository.findAll().size();

        // Create the Series
        SeriesDTO seriesDTO = seriesMapper.toDto(series);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSeriesMockMvc.perform(put("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seriesDTO)))
            .andExpect(status().isCreated());

        // Validate the Series in the database
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSeries() throws Exception {
        // Initialize the database
        seriesRepository.saveAndFlush(series);
        int databaseSizeBeforeDelete = seriesRepository.findAll().size();

        // Get the series
        restSeriesMockMvc.perform(delete("/api/series/{id}", series.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Series> seriesList = seriesRepository.findAll();
        assertThat(seriesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Series.class);
        Series series1 = new Series();
        series1.setId(1L);
        Series series2 = new Series();
        series2.setId(series1.getId());
        assertThat(series1).isEqualTo(series2);
        series2.setId(2L);
        assertThat(series1).isNotEqualTo(series2);
        series1.setId(null);
        assertThat(series1).isNotEqualTo(series2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeriesDTO.class);
        SeriesDTO seriesDTO1 = new SeriesDTO();
        seriesDTO1.setId(1L);
        SeriesDTO seriesDTO2 = new SeriesDTO();
        assertThat(seriesDTO1).isNotEqualTo(seriesDTO2);
        seriesDTO2.setId(seriesDTO1.getId());
        assertThat(seriesDTO1).isEqualTo(seriesDTO2);
        seriesDTO2.setId(2L);
        assertThat(seriesDTO1).isNotEqualTo(seriesDTO2);
        seriesDTO1.setId(null);
        assertThat(seriesDTO1).isNotEqualTo(seriesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(seriesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(seriesMapper.fromId(null)).isNull();
    }
}
