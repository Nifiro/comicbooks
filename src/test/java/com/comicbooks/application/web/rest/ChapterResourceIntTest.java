package com.comicbooks.application.web.rest;

import com.comicbooks.application.ComicbooksApp;

import com.comicbooks.application.domain.Chapter;
import com.comicbooks.application.repository.ChapterRepository;
import com.comicbooks.application.service.ChapterService;
import com.comicbooks.application.service.dto.ChapterDTO;
import com.comicbooks.application.service.mapper.ChapterMapper;
import com.comicbooks.application.web.rest.errors.ExceptionTranslator;
import com.comicbooks.application.service.dto.ChapterCriteria;
import com.comicbooks.application.service.ChapterQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.comicbooks.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChapterResource REST controller.
 *
 * @see ChapterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicbooksApp.class)
public class ChapterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_VOLUME = "AAAAAAAAAA";
    private static final String UPDATED_VOLUME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGES = 1;
    private static final Integer UPDATED_PAGES = 2;

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ChapterQueryService chapterQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChapterMockMvc;

    private Chapter chapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChapterResource chapterResource = new ChapterResource(chapterService, chapterQueryService);
        this.restChapterMockMvc = MockMvcBuilders.standaloneSetup(chapterResource)
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
    public static Chapter createEntity(EntityManager em) {
        Chapter chapter = new Chapter()
            .name(DEFAULT_NAME)
            .number(DEFAULT_NUMBER)
            .volume(DEFAULT_VOLUME)
            .filePath(DEFAULT_FILE_PATH)
            .pages(DEFAULT_PAGES)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return chapter;
    }

    @Before
    public void initTest() {
        chapter = createEntity(em);
    }

    @Test
    @Transactional
    public void createChapter() throws Exception {
        int databaseSizeBeforeCreate = chapterRepository.findAll().size();

        // Create the Chapter
        ChapterDTO chapterDTO = chapterMapper.toDto(chapter);
        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isCreated());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeCreate + 1);
        Chapter testChapter = chapterList.get(chapterList.size() - 1);
        assertThat(testChapter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChapter.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testChapter.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testChapter.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testChapter.getPages()).isEqualTo(DEFAULT_PAGES);
        assertThat(testChapter.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testChapter.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testChapter.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testChapter.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testChapter.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createChapterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chapterRepository.findAll().size();

        // Create the Chapter with an existing ID
        chapter.setId(1L);
        ChapterDTO chapterDTO = chapterMapper.toDto(chapter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setName(null);

        // Create the Chapter, which fails.
        ChapterDTO chapterDTO = chapterMapper.toDto(chapter);

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setNumber(null);

        // Create the Chapter, which fails.
        ChapterDTO chapterDTO = chapterMapper.toDto(chapter);

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChapters() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList
        restChapterMockMvc.perform(get("/api/chapters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getChapter() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get the chapter
        restChapterMockMvc.perform(get("/api/chapters/{id}", chapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chapter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.pages").value(DEFAULT_PAGES))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllChaptersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where name equals to DEFAULT_NAME
        defaultChapterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the chapterList where name equals to UPDATED_NAME
        defaultChapterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChaptersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChapterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the chapterList where name equals to UPDATED_NAME
        defaultChapterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChaptersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where name is not null
        defaultChapterShouldBeFound("name.specified=true");

        // Get all the chapterList where name is null
        defaultChapterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where number equals to DEFAULT_NUMBER
        defaultChapterShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the chapterList where number equals to UPDATED_NUMBER
        defaultChapterShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChaptersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultChapterShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the chapterList where number equals to UPDATED_NUMBER
        defaultChapterShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChaptersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where number is not null
        defaultChapterShouldBeFound("number.specified=true");

        // Get all the chapterList where number is null
        defaultChapterShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where number greater than or equals to DEFAULT_NUMBER
        defaultChapterShouldBeFound("number.greaterOrEqualThan=" + DEFAULT_NUMBER);

        // Get all the chapterList where number greater than or equals to UPDATED_NUMBER
        defaultChapterShouldNotBeFound("number.greaterOrEqualThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChaptersByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where number less than or equals to DEFAULT_NUMBER
        defaultChapterShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the chapterList where number less than or equals to UPDATED_NUMBER
        defaultChapterShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllChaptersByVolumeIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where volume equals to DEFAULT_VOLUME
        defaultChapterShouldBeFound("volume.equals=" + DEFAULT_VOLUME);

        // Get all the chapterList where volume equals to UPDATED_VOLUME
        defaultChapterShouldNotBeFound("volume.equals=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void getAllChaptersByVolumeIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where volume in DEFAULT_VOLUME or UPDATED_VOLUME
        defaultChapterShouldBeFound("volume.in=" + DEFAULT_VOLUME + "," + UPDATED_VOLUME);

        // Get all the chapterList where volume equals to UPDATED_VOLUME
        defaultChapterShouldNotBeFound("volume.in=" + UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void getAllChaptersByVolumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where volume is not null
        defaultChapterShouldBeFound("volume.specified=true");

        // Get all the chapterList where volume is null
        defaultChapterShouldNotBeFound("volume.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where filePath equals to DEFAULT_FILE_PATH
        defaultChapterShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the chapterList where filePath equals to UPDATED_FILE_PATH
        defaultChapterShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllChaptersByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultChapterShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the chapterList where filePath equals to UPDATED_FILE_PATH
        defaultChapterShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllChaptersByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where filePath is not null
        defaultChapterShouldBeFound("filePath.specified=true");

        // Get all the chapterList where filePath is null
        defaultChapterShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where pages equals to DEFAULT_PAGES
        defaultChapterShouldBeFound("pages.equals=" + DEFAULT_PAGES);

        // Get all the chapterList where pages equals to UPDATED_PAGES
        defaultChapterShouldNotBeFound("pages.equals=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllChaptersByPagesIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where pages in DEFAULT_PAGES or UPDATED_PAGES
        defaultChapterShouldBeFound("pages.in=" + DEFAULT_PAGES + "," + UPDATED_PAGES);

        // Get all the chapterList where pages equals to UPDATED_PAGES
        defaultChapterShouldNotBeFound("pages.in=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllChaptersByPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where pages is not null
        defaultChapterShouldBeFound("pages.specified=true");

        // Get all the chapterList where pages is null
        defaultChapterShouldNotBeFound("pages.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where pages greater than or equals to DEFAULT_PAGES
        defaultChapterShouldBeFound("pages.greaterOrEqualThan=" + DEFAULT_PAGES);

        // Get all the chapterList where pages greater than or equals to UPDATED_PAGES
        defaultChapterShouldNotBeFound("pages.greaterOrEqualThan=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllChaptersByPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where pages less than or equals to DEFAULT_PAGES
        defaultChapterShouldNotBeFound("pages.lessThan=" + DEFAULT_PAGES);

        // Get all the chapterList where pages less than or equals to UPDATED_PAGES
        defaultChapterShouldBeFound("pages.lessThan=" + UPDATED_PAGES);
    }


    @Test
    @Transactional
    public void getAllChaptersByReleaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where releaseDate equals to DEFAULT_RELEASE_DATE
        defaultChapterShouldBeFound("releaseDate.equals=" + DEFAULT_RELEASE_DATE);

        // Get all the chapterList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultChapterShouldNotBeFound("releaseDate.equals=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByReleaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where releaseDate in DEFAULT_RELEASE_DATE or UPDATED_RELEASE_DATE
        defaultChapterShouldBeFound("releaseDate.in=" + DEFAULT_RELEASE_DATE + "," + UPDATED_RELEASE_DATE);

        // Get all the chapterList where releaseDate equals to UPDATED_RELEASE_DATE
        defaultChapterShouldNotBeFound("releaseDate.in=" + UPDATED_RELEASE_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByReleaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where releaseDate is not null
        defaultChapterShouldBeFound("releaseDate.specified=true");

        // Get all the chapterList where releaseDate is null
        defaultChapterShouldNotBeFound("releaseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdBy equals to DEFAULT_CREATED_BY
        defaultChapterShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the chapterList where createdBy equals to UPDATED_CREATED_BY
        defaultChapterShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultChapterShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the chapterList where createdBy equals to UPDATED_CREATED_BY
        defaultChapterShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdBy is not null
        defaultChapterShouldBeFound("createdBy.specified=true");

        // Get all the chapterList where createdBy is null
        defaultChapterShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdDate equals to DEFAULT_CREATED_DATE
        defaultChapterShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the chapterList where createdDate equals to UPDATED_CREATED_DATE
        defaultChapterShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultChapterShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the chapterList where createdDate equals to UPDATED_CREATED_DATE
        defaultChapterShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where createdDate is not null
        defaultChapterShouldBeFound("createdDate.specified=true");

        // Get all the chapterList where createdDate is null
        defaultChapterShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultChapterShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the chapterList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultChapterShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultChapterShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the chapterList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultChapterShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedBy is not null
        defaultChapterShouldBeFound("lastModifiedBy.specified=true");

        // Get all the chapterList where lastModifiedBy is null
        defaultChapterShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultChapterShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the chapterList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultChapterShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultChapterShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the chapterList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultChapterShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllChaptersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where lastModifiedDate is not null
        defaultChapterShouldBeFound("lastModifiedDate.specified=true");

        // Get all the chapterList where lastModifiedDate is null
        defaultChapterShouldNotBeFound("lastModifiedDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultChapterShouldBeFound(String filter) throws Exception {
        restChapterMockMvc.perform(get("/api/chapters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultChapterShouldNotBeFound(String filter) throws Exception {
        restChapterMockMvc.perform(get("/api/chapters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingChapter() throws Exception {
        // Get the chapter
        restChapterMockMvc.perform(get("/api/chapters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChapter() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);
        int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

        // Update the chapter
        Chapter updatedChapter = chapterRepository.findOne(chapter.getId());
        // Disconnect from session so that the updates on updatedChapter are not directly saved in db
        em.detach(updatedChapter);
        updatedChapter
            .name(UPDATED_NAME)
            .number(UPDATED_NUMBER)
            .volume(UPDATED_VOLUME)
            .filePath(UPDATED_FILE_PATH)
            .pages(UPDATED_PAGES)
            .releaseDate(UPDATED_RELEASE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ChapterDTO chapterDTO = chapterMapper.toDto(updatedChapter);

        restChapterMockMvc.perform(put("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isOk());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
        Chapter testChapter = chapterList.get(chapterList.size() - 1);
        assertThat(testChapter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChapter.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testChapter.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testChapter.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testChapter.getPages()).isEqualTo(UPDATED_PAGES);
        assertThat(testChapter.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testChapter.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testChapter.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testChapter.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testChapter.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChapter() throws Exception {
        int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

        // Create the Chapter
        ChapterDTO chapterDTO = chapterMapper.toDto(chapter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChapterMockMvc.perform(put("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapterDTO)))
            .andExpect(status().isCreated());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChapter() throws Exception {
        // Initialize the database
        chapterRepository.saveAndFlush(chapter);
        int databaseSizeBeforeDelete = chapterRepository.findAll().size();

        // Get the chapter
        restChapterMockMvc.perform(delete("/api/chapters/{id}", chapter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chapter.class);
        Chapter chapter1 = new Chapter();
        chapter1.setId(1L);
        Chapter chapter2 = new Chapter();
        chapter2.setId(chapter1.getId());
        assertThat(chapter1).isEqualTo(chapter2);
        chapter2.setId(2L);
        assertThat(chapter1).isNotEqualTo(chapter2);
        chapter1.setId(null);
        assertThat(chapter1).isNotEqualTo(chapter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChapterDTO.class);
        ChapterDTO chapterDTO1 = new ChapterDTO();
        chapterDTO1.setId(1L);
        ChapterDTO chapterDTO2 = new ChapterDTO();
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
        chapterDTO2.setId(chapterDTO1.getId());
        assertThat(chapterDTO1).isEqualTo(chapterDTO2);
        chapterDTO2.setId(2L);
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
        chapterDTO1.setId(null);
        assertThat(chapterDTO1).isNotEqualTo(chapterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chapterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chapterMapper.fromId(null)).isNull();
    }
}
