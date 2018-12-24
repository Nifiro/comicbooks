package com.comicbooks.application.web.rest;

import com.comicbooks.application.ComicbooksApp;

import com.comicbooks.application.domain.ComicBookGenres;
import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.domain.Genre;
import com.comicbooks.application.repository.ComicBookGenresRepository;
import com.comicbooks.application.service.ComicBookGenresService;
import com.comicbooks.application.service.dto.ComicBookGenresDTO;
import com.comicbooks.application.service.mapper.ComicBookGenresMapper;
import com.comicbooks.application.web.rest.errors.ExceptionTranslator;
import com.comicbooks.application.service.dto.ComicBookGenresCriteria;
import com.comicbooks.application.service.ComicBookGenresQueryService;

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
 * Test class for the ComicBookGenresResource REST controller.
 *
 * @see ComicBookGenresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicbooksApp.class)
public class ComicBookGenresResourceIntTest {

    @Autowired
    private ComicBookGenresRepository comicBookGenresRepository;

    @Autowired
    private ComicBookGenresMapper comicBookGenresMapper;

    @Autowired
    private ComicBookGenresService comicBookGenresService;

    @Autowired
    private ComicBookGenresQueryService comicBookGenresQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComicBookGenresMockMvc;

    private ComicBookGenres comicBookGenres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComicBookGenresResource comicBookGenresResource = new ComicBookGenresResource(comicBookGenresService, comicBookGenresQueryService);
        this.restComicBookGenresMockMvc = MockMvcBuilders.standaloneSetup(comicBookGenresResource)
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
    public static ComicBookGenres createEntity(EntityManager em) {
        ComicBookGenres comicBookGenres = new ComicBookGenres();
        return comicBookGenres;
    }

    @Before
    public void initTest() {
        comicBookGenres = createEntity(em);
    }

    @Test
    @Transactional
    public void createComicBookGenres() throws Exception {
        int databaseSizeBeforeCreate = comicBookGenresRepository.findAll().size();

        // Create the ComicBookGenres
        ComicBookGenresDTO comicBookGenresDTO = comicBookGenresMapper.toDto(comicBookGenres);
        restComicBookGenresMockMvc.perform(post("/api/comic-book-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookGenresDTO)))
            .andExpect(status().isCreated());

        // Validate the ComicBookGenres in the database
        List<ComicBookGenres> comicBookGenresList = comicBookGenresRepository.findAll();
        assertThat(comicBookGenresList).hasSize(databaseSizeBeforeCreate + 1);
        ComicBookGenres testComicBookGenres = comicBookGenresList.get(comicBookGenresList.size() - 1);
    }

    @Test
    @Transactional
    public void createComicBookGenresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comicBookGenresRepository.findAll().size();

        // Create the ComicBookGenres with an existing ID
        comicBookGenres.setId(1L);
        ComicBookGenresDTO comicBookGenresDTO = comicBookGenresMapper.toDto(comicBookGenres);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComicBookGenresMockMvc.perform(post("/api/comic-book-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookGenresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ComicBookGenres in the database
        List<ComicBookGenres> comicBookGenresList = comicBookGenresRepository.findAll();
        assertThat(comicBookGenresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllComicBookGenres() throws Exception {
        // Initialize the database
        comicBookGenresRepository.saveAndFlush(comicBookGenres);

        // Get all the comicBookGenresList
        restComicBookGenresMockMvc.perform(get("/api/comic-book-genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comicBookGenres.getId().intValue())));
    }

    @Test
    @Transactional
    public void getComicBookGenres() throws Exception {
        // Initialize the database
        comicBookGenresRepository.saveAndFlush(comicBookGenres);

        // Get the comicBookGenres
        restComicBookGenresMockMvc.perform(get("/api/comic-book-genres/{id}", comicBookGenres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comicBookGenres.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllComicBookGenresByComicBookIsEqualToSomething() throws Exception {
        // Initialize the database
        ComicBook comicBook = ComicBookResourceIntTest.createEntity(em);
        em.persist(comicBook);
        em.flush();
        comicBookGenres.setComicBook(comicBook);
        comicBookGenresRepository.saveAndFlush(comicBookGenres);
        Long comicBookId = comicBook.getId();

        // Get all the comicBookGenresList where comicBook equals to comicBookId
        defaultComicBookGenresShouldBeFound("comicBookId.equals=" + comicBookId);

        // Get all the comicBookGenresList where comicBook equals to comicBookId + 1
        defaultComicBookGenresShouldNotBeFound("comicBookId.equals=" + (comicBookId + 1));
    }


    @Test
    @Transactional
    public void getAllComicBookGenresByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        Genre genre = GenreResourceIntTest.createEntity(em);
        em.persist(genre);
        em.flush();
        comicBookGenres.setGenre(genre);
        comicBookGenresRepository.saveAndFlush(comicBookGenres);
        Long genreId = genre.getId();

        // Get all the comicBookGenresList where genre equals to genreId
        defaultComicBookGenresShouldBeFound("genreId.equals=" + genreId);

        // Get all the comicBookGenresList where genre equals to genreId + 1
        defaultComicBookGenresShouldNotBeFound("genreId.equals=" + (genreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultComicBookGenresShouldBeFound(String filter) throws Exception {
        restComicBookGenresMockMvc.perform(get("/api/comic-book-genres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comicBookGenres.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultComicBookGenresShouldNotBeFound(String filter) throws Exception {
        restComicBookGenresMockMvc.perform(get("/api/comic-book-genres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingComicBookGenres() throws Exception {
        // Get the comicBookGenres
        restComicBookGenresMockMvc.perform(get("/api/comic-book-genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComicBookGenres() throws Exception {
        // Initialize the database
        comicBookGenresRepository.saveAndFlush(comicBookGenres);
        int databaseSizeBeforeUpdate = comicBookGenresRepository.findAll().size();

        // Update the comicBookGenres
        ComicBookGenres updatedComicBookGenres = comicBookGenresRepository.findOne(comicBookGenres.getId());
        // Disconnect from session so that the updates on updatedComicBookGenres are not directly saved in db
        em.detach(updatedComicBookGenres);
        ComicBookGenresDTO comicBookGenresDTO = comicBookGenresMapper.toDto(updatedComicBookGenres);

        restComicBookGenresMockMvc.perform(put("/api/comic-book-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookGenresDTO)))
            .andExpect(status().isOk());

        // Validate the ComicBookGenres in the database
        List<ComicBookGenres> comicBookGenresList = comicBookGenresRepository.findAll();
        assertThat(comicBookGenresList).hasSize(databaseSizeBeforeUpdate);
        ComicBookGenres testComicBookGenres = comicBookGenresList.get(comicBookGenresList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingComicBookGenres() throws Exception {
        int databaseSizeBeforeUpdate = comicBookGenresRepository.findAll().size();

        // Create the ComicBookGenres
        ComicBookGenresDTO comicBookGenresDTO = comicBookGenresMapper.toDto(comicBookGenres);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComicBookGenresMockMvc.perform(put("/api/comic-book-genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookGenresDTO)))
            .andExpect(status().isCreated());

        // Validate the ComicBookGenres in the database
        List<ComicBookGenres> comicBookGenresList = comicBookGenresRepository.findAll();
        assertThat(comicBookGenresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComicBookGenres() throws Exception {
        // Initialize the database
        comicBookGenresRepository.saveAndFlush(comicBookGenres);
        int databaseSizeBeforeDelete = comicBookGenresRepository.findAll().size();

        // Get the comicBookGenres
        restComicBookGenresMockMvc.perform(delete("/api/comic-book-genres/{id}", comicBookGenres.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComicBookGenres> comicBookGenresList = comicBookGenresRepository.findAll();
        assertThat(comicBookGenresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComicBookGenres.class);
        ComicBookGenres comicBookGenres1 = new ComicBookGenres();
        comicBookGenres1.setId(1L);
        ComicBookGenres comicBookGenres2 = new ComicBookGenres();
        comicBookGenres2.setId(comicBookGenres1.getId());
        assertThat(comicBookGenres1).isEqualTo(comicBookGenres2);
        comicBookGenres2.setId(2L);
        assertThat(comicBookGenres1).isNotEqualTo(comicBookGenres2);
        comicBookGenres1.setId(null);
        assertThat(comicBookGenres1).isNotEqualTo(comicBookGenres2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComicBookGenresDTO.class);
        ComicBookGenresDTO comicBookGenresDTO1 = new ComicBookGenresDTO();
        comicBookGenresDTO1.setId(1L);
        ComicBookGenresDTO comicBookGenresDTO2 = new ComicBookGenresDTO();
        assertThat(comicBookGenresDTO1).isNotEqualTo(comicBookGenresDTO2);
        comicBookGenresDTO2.setId(comicBookGenresDTO1.getId());
        assertThat(comicBookGenresDTO1).isEqualTo(comicBookGenresDTO2);
        comicBookGenresDTO2.setId(2L);
        assertThat(comicBookGenresDTO1).isNotEqualTo(comicBookGenresDTO2);
        comicBookGenresDTO1.setId(null);
        assertThat(comicBookGenresDTO1).isNotEqualTo(comicBookGenresDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(comicBookGenresMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(comicBookGenresMapper.fromId(null)).isNull();
    }
}
