package com.comicbooks.application.web.rest;

import com.comicbooks.application.ComicbooksApp;

import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.domain.Author;
import com.comicbooks.application.repository.ComicBookRepository;
import com.comicbooks.application.service.ComicBookService;
import com.comicbooks.application.service.dto.ComicBookDTO;
import com.comicbooks.application.service.mapper.ComicBookMapper;
import com.comicbooks.application.web.rest.errors.ExceptionTranslator;
import com.comicbooks.application.service.dto.ComicBookCriteria;
import com.comicbooks.application.service.ComicBookQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.comicbooks.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comicbooks.application.domain.enumeration.Status;
/**
 * Test class for the ComicBookResource REST controller.
 *
 * @see ComicBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicbooksApp.class)
public class ComicBookResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHAPTERS = 1;
    private static final Integer UPDATED_CHAPTERS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SERIALIZED_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SERIALIZED_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SERIALIZED_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SERIALIZED_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.ONGOING;
    private static final Status UPDATED_STATUS = Status.COMPLETED;

    @Autowired
    private ComicBookRepository comicBookRepository;

    @Autowired
    private ComicBookMapper comicBookMapper;

    @Autowired
    private ComicBookService comicBookService;

    @Autowired
    private ComicBookQueryService comicBookQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComicBookMockMvc;

    private ComicBook comicBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComicBookResource comicBookResource = new ComicBookResource(comicBookService, comicBookQueryService);
        this.restComicBookMockMvc = MockMvcBuilders.standaloneSetup(comicBookResource)
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
    public static ComicBook createEntity(EntityManager em) {
        ComicBook comicBook = new ComicBook()
            .title(DEFAULT_TITLE)
            .chapters(DEFAULT_CHAPTERS)
            .description(DEFAULT_DESCRIPTION)
            .publisher(DEFAULT_PUBLISHER)
            .serializedFrom(DEFAULT_SERIALIZED_FROM)
            .serializedTo(DEFAULT_SERIALIZED_TO)
            .imagePath(DEFAULT_IMAGE_PATH)
            .coverPath(DEFAULT_COVER_PATH)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .status(DEFAULT_STATUS);
        return comicBook;
    }

    @Before
    public void initTest() {
        comicBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createComicBook() throws Exception {
        int databaseSizeBeforeCreate = comicBookRepository.findAll().size();

        // Create the ComicBook
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);
        restComicBookMockMvc.perform(post("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isCreated());

        // Validate the ComicBook in the database
        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeCreate + 1);
        ComicBook testComicBook = comicBookList.get(comicBookList.size() - 1);
        assertThat(testComicBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testComicBook.getChapters()).isEqualTo(DEFAULT_CHAPTERS);
        assertThat(testComicBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComicBook.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testComicBook.getSerializedFrom()).isEqualTo(DEFAULT_SERIALIZED_FROM);
        assertThat(testComicBook.getSerializedTo()).isEqualTo(DEFAULT_SERIALIZED_TO);
        assertThat(testComicBook.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testComicBook.getCoverPath()).isEqualTo(DEFAULT_COVER_PATH);
        assertThat(testComicBook.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testComicBook.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testComicBook.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testComicBook.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testComicBook.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createComicBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = comicBookRepository.findAll().size();

        // Create the ComicBook with an existing ID
        comicBook.setId(1L);
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComicBookMockMvc.perform(post("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ComicBook in the database
        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicBookRepository.findAll().size();
        // set the field null
        comicBook.setTitle(null);

        // Create the ComicBook, which fails.
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isBadRequest());

        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChaptersIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicBookRepository.findAll().size();
        // set the field null
        comicBook.setChapters(null);

        // Create the ComicBook, which fails.
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isBadRequest());

        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicBookRepository.findAll().size();
        // set the field null
        comicBook.setDescription(null);

        // Create the ComicBook, which fails.
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isBadRequest());

        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComicBooks() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList
        restComicBookMockMvc.perform(get("/api/comic-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comicBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].chapters").value(hasItem(DEFAULT_CHAPTERS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER.toString())))
            .andExpect(jsonPath("$.[*].serializedFrom").value(hasItem(DEFAULT_SERIALIZED_FROM.toString())))
            .andExpect(jsonPath("$.[*].serializedTo").value(hasItem(DEFAULT_SERIALIZED_TO.toString())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH.toString())))
            .andExpect(jsonPath("$.[*].coverPath").value(hasItem(DEFAULT_COVER_PATH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get the comicBook
        restComicBookMockMvc.perform(get("/api/comic-books/{id}", comicBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comicBook.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.chapters").value(DEFAULT_CHAPTERS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER.toString()))
            .andExpect(jsonPath("$.serializedFrom").value(DEFAULT_SERIALIZED_FROM.toString()))
            .andExpect(jsonPath("$.serializedTo").value(DEFAULT_SERIALIZED_TO.toString()))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH.toString()))
            .andExpect(jsonPath("$.coverPath").value(DEFAULT_COVER_PATH.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllComicBooksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where title equals to DEFAULT_TITLE
        defaultComicBookShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the comicBookList where title equals to UPDATED_TITLE
        defaultComicBookShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultComicBookShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the comicBookList where title equals to UPDATED_TITLE
        defaultComicBookShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where title is not null
        defaultComicBookShouldBeFound("title.specified=true");

        // Get all the comicBookList where title is null
        defaultComicBookShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByChaptersIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where chapters equals to DEFAULT_CHAPTERS
        defaultComicBookShouldBeFound("chapters.equals=" + DEFAULT_CHAPTERS);

        // Get all the comicBookList where chapters equals to UPDATED_CHAPTERS
        defaultComicBookShouldNotBeFound("chapters.equals=" + UPDATED_CHAPTERS);
    }

    @Test
    @Transactional
    public void getAllComicBooksByChaptersIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where chapters in DEFAULT_CHAPTERS or UPDATED_CHAPTERS
        defaultComicBookShouldBeFound("chapters.in=" + DEFAULT_CHAPTERS + "," + UPDATED_CHAPTERS);

        // Get all the comicBookList where chapters equals to UPDATED_CHAPTERS
        defaultComicBookShouldNotBeFound("chapters.in=" + UPDATED_CHAPTERS);
    }

    @Test
    @Transactional
    public void getAllComicBooksByChaptersIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where chapters is not null
        defaultComicBookShouldBeFound("chapters.specified=true");

        // Get all the comicBookList where chapters is null
        defaultComicBookShouldNotBeFound("chapters.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByChaptersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where chapters greater than or equals to DEFAULT_CHAPTERS
        defaultComicBookShouldBeFound("chapters.greaterOrEqualThan=" + DEFAULT_CHAPTERS);

        // Get all the comicBookList where chapters greater than or equals to UPDATED_CHAPTERS
        defaultComicBookShouldNotBeFound("chapters.greaterOrEqualThan=" + UPDATED_CHAPTERS);
    }

    @Test
    @Transactional
    public void getAllComicBooksByChaptersIsLessThanSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where chapters less than or equals to DEFAULT_CHAPTERS
        defaultComicBookShouldNotBeFound("chapters.lessThan=" + DEFAULT_CHAPTERS);

        // Get all the comicBookList where chapters less than or equals to UPDATED_CHAPTERS
        defaultComicBookShouldBeFound("chapters.lessThan=" + UPDATED_CHAPTERS);
    }


    @Test
    @Transactional
    public void getAllComicBooksByPublisherIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where publisher equals to DEFAULT_PUBLISHER
        defaultComicBookShouldBeFound("publisher.equals=" + DEFAULT_PUBLISHER);

        // Get all the comicBookList where publisher equals to UPDATED_PUBLISHER
        defaultComicBookShouldNotBeFound("publisher.equals=" + UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    public void getAllComicBooksByPublisherIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where publisher in DEFAULT_PUBLISHER or UPDATED_PUBLISHER
        defaultComicBookShouldBeFound("publisher.in=" + DEFAULT_PUBLISHER + "," + UPDATED_PUBLISHER);

        // Get all the comicBookList where publisher equals to UPDATED_PUBLISHER
        defaultComicBookShouldNotBeFound("publisher.in=" + UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    public void getAllComicBooksByPublisherIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where publisher is not null
        defaultComicBookShouldBeFound("publisher.specified=true");

        // Get all the comicBookList where publisher is null
        defaultComicBookShouldNotBeFound("publisher.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedFromIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedFrom equals to DEFAULT_SERIALIZED_FROM
        defaultComicBookShouldBeFound("serializedFrom.equals=" + DEFAULT_SERIALIZED_FROM);

        // Get all the comicBookList where serializedFrom equals to UPDATED_SERIALIZED_FROM
        defaultComicBookShouldNotBeFound("serializedFrom.equals=" + UPDATED_SERIALIZED_FROM);
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedFromIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedFrom in DEFAULT_SERIALIZED_FROM or UPDATED_SERIALIZED_FROM
        defaultComicBookShouldBeFound("serializedFrom.in=" + DEFAULT_SERIALIZED_FROM + "," + UPDATED_SERIALIZED_FROM);

        // Get all the comicBookList where serializedFrom equals to UPDATED_SERIALIZED_FROM
        defaultComicBookShouldNotBeFound("serializedFrom.in=" + UPDATED_SERIALIZED_FROM);
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedFrom is not null
        defaultComicBookShouldBeFound("serializedFrom.specified=true");

        // Get all the comicBookList where serializedFrom is null
        defaultComicBookShouldNotBeFound("serializedFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedToIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedTo equals to DEFAULT_SERIALIZED_TO
        defaultComicBookShouldBeFound("serializedTo.equals=" + DEFAULT_SERIALIZED_TO);

        // Get all the comicBookList where serializedTo equals to UPDATED_SERIALIZED_TO
        defaultComicBookShouldNotBeFound("serializedTo.equals=" + UPDATED_SERIALIZED_TO);
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedToIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedTo in DEFAULT_SERIALIZED_TO or UPDATED_SERIALIZED_TO
        defaultComicBookShouldBeFound("serializedTo.in=" + DEFAULT_SERIALIZED_TO + "," + UPDATED_SERIALIZED_TO);

        // Get all the comicBookList where serializedTo equals to UPDATED_SERIALIZED_TO
        defaultComicBookShouldNotBeFound("serializedTo.in=" + UPDATED_SERIALIZED_TO);
    }

    @Test
    @Transactional
    public void getAllComicBooksBySerializedToIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where serializedTo is not null
        defaultComicBookShouldBeFound("serializedTo.specified=true");

        // Get all the comicBookList where serializedTo is null
        defaultComicBookShouldNotBeFound("serializedTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByImagePathIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where imagePath equals to DEFAULT_IMAGE_PATH
        defaultComicBookShouldBeFound("imagePath.equals=" + DEFAULT_IMAGE_PATH);

        // Get all the comicBookList where imagePath equals to UPDATED_IMAGE_PATH
        defaultComicBookShouldNotBeFound("imagePath.equals=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllComicBooksByImagePathIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where imagePath in DEFAULT_IMAGE_PATH or UPDATED_IMAGE_PATH
        defaultComicBookShouldBeFound("imagePath.in=" + DEFAULT_IMAGE_PATH + "," + UPDATED_IMAGE_PATH);

        // Get all the comicBookList where imagePath equals to UPDATED_IMAGE_PATH
        defaultComicBookShouldNotBeFound("imagePath.in=" + UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllComicBooksByImagePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where imagePath is not null
        defaultComicBookShouldBeFound("imagePath.specified=true");

        // Get all the comicBookList where imagePath is null
        defaultComicBookShouldNotBeFound("imagePath.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByCoverPathIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where coverPath equals to DEFAULT_COVER_PATH
        defaultComicBookShouldBeFound("coverPath.equals=" + DEFAULT_COVER_PATH);

        // Get all the comicBookList where coverPath equals to UPDATED_COVER_PATH
        defaultComicBookShouldNotBeFound("coverPath.equals=" + UPDATED_COVER_PATH);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCoverPathIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where coverPath in DEFAULT_COVER_PATH or UPDATED_COVER_PATH
        defaultComicBookShouldBeFound("coverPath.in=" + DEFAULT_COVER_PATH + "," + UPDATED_COVER_PATH);

        // Get all the comicBookList where coverPath equals to UPDATED_COVER_PATH
        defaultComicBookShouldNotBeFound("coverPath.in=" + UPDATED_COVER_PATH);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCoverPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where coverPath is not null
        defaultComicBookShouldBeFound("coverPath.specified=true");

        // Get all the comicBookList where coverPath is null
        defaultComicBookShouldNotBeFound("coverPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdBy equals to DEFAULT_CREATED_BY
        defaultComicBookShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the comicBookList where createdBy equals to UPDATED_CREATED_BY
        defaultComicBookShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultComicBookShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the comicBookList where createdBy equals to UPDATED_CREATED_BY
        defaultComicBookShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdBy is not null
        defaultComicBookShouldBeFound("createdBy.specified=true");

        // Get all the comicBookList where createdBy is null
        defaultComicBookShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdDate equals to DEFAULT_CREATED_DATE
        defaultComicBookShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the comicBookList where createdDate equals to UPDATED_CREATED_DATE
        defaultComicBookShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultComicBookShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the comicBookList where createdDate equals to UPDATED_CREATED_DATE
        defaultComicBookShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where createdDate is not null
        defaultComicBookShouldBeFound("createdDate.specified=true");

        // Get all the comicBookList where createdDate is null
        defaultComicBookShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultComicBookShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the comicBookList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultComicBookShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultComicBookShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the comicBookList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultComicBookShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedBy is not null
        defaultComicBookShouldBeFound("lastModifiedBy.specified=true");

        // Get all the comicBookList where lastModifiedBy is null
        defaultComicBookShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultComicBookShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the comicBookList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultComicBookShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultComicBookShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the comicBookList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultComicBookShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllComicBooksByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where lastModifiedDate is not null
        defaultComicBookShouldBeFound("lastModifiedDate.specified=true");

        // Get all the comicBookList where lastModifiedDate is null
        defaultComicBookShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where status equals to DEFAULT_STATUS
        defaultComicBookShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the comicBookList where status equals to UPDATED_STATUS
        defaultComicBookShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllComicBooksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultComicBookShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the comicBookList where status equals to UPDATED_STATUS
        defaultComicBookShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllComicBooksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBookList where status is not null
        defaultComicBookShouldBeFound("status.specified=true");

        // Get all the comicBookList where status is null
        defaultComicBookShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllComicBooksByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        Author author = AuthorResourceIntTest.createEntity(em);
        em.persist(author);
        em.flush();
        comicBook.setAuthor(author);
        comicBookRepository.saveAndFlush(comicBook);
        Long authorId = author.getId();

        // Get all the comicBookList where author equals to authorId
        defaultComicBookShouldBeFound("authorId.equals=" + authorId);

        // Get all the comicBookList where author equals to authorId + 1
        defaultComicBookShouldNotBeFound("authorId.equals=" + (authorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultComicBookShouldBeFound(String filter) throws Exception {
        restComicBookMockMvc.perform(get("/api/comic-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comicBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].chapters").value(hasItem(DEFAULT_CHAPTERS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER.toString())))
            .andExpect(jsonPath("$.[*].serializedFrom").value(hasItem(DEFAULT_SERIALIZED_FROM.toString())))
            .andExpect(jsonPath("$.[*].serializedTo").value(hasItem(DEFAULT_SERIALIZED_TO.toString())))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH.toString())))
            .andExpect(jsonPath("$.[*].coverPath").value(hasItem(DEFAULT_COVER_PATH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultComicBookShouldNotBeFound(String filter) throws Exception {
        restComicBookMockMvc.perform(get("/api/comic-books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingComicBook() throws Exception {
        // Get the comicBook
        restComicBookMockMvc.perform(get("/api/comic-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);
        int databaseSizeBeforeUpdate = comicBookRepository.findAll().size();

        // Update the comicBook
        ComicBook updatedComicBook = comicBookRepository.findOne(comicBook.getId());
        // Disconnect from session so that the updates on updatedComicBook are not directly saved in db
        em.detach(updatedComicBook);
        updatedComicBook
            .title(UPDATED_TITLE)
            .chapters(UPDATED_CHAPTERS)
            .description(UPDATED_DESCRIPTION)
            .publisher(UPDATED_PUBLISHER)
            .serializedFrom(UPDATED_SERIALIZED_FROM)
            .serializedTo(UPDATED_SERIALIZED_TO)
            .imagePath(UPDATED_IMAGE_PATH)
            .coverPath(UPDATED_COVER_PATH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .status(UPDATED_STATUS);
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(updatedComicBook);

        restComicBookMockMvc.perform(put("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isOk());

        // Validate the ComicBook in the database
        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeUpdate);
        ComicBook testComicBook = comicBookList.get(comicBookList.size() - 1);
        assertThat(testComicBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testComicBook.getChapters()).isEqualTo(UPDATED_CHAPTERS);
        assertThat(testComicBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComicBook.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testComicBook.getSerializedFrom()).isEqualTo(UPDATED_SERIALIZED_FROM);
        assertThat(testComicBook.getSerializedTo()).isEqualTo(UPDATED_SERIALIZED_TO);
        assertThat(testComicBook.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testComicBook.getCoverPath()).isEqualTo(UPDATED_COVER_PATH);
        assertThat(testComicBook.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testComicBook.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testComicBook.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testComicBook.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testComicBook.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingComicBook() throws Exception {
        int databaseSizeBeforeUpdate = comicBookRepository.findAll().size();

        // Create the ComicBook
        ComicBookDTO comicBookDTO = comicBookMapper.toDto(comicBook);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComicBookMockMvc.perform(put("/api/comic-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
            .andExpect(status().isCreated());

        // Validate the ComicBook in the database
        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);
        int databaseSizeBeforeDelete = comicBookRepository.findAll().size();

        // Get the comicBook
        restComicBookMockMvc.perform(delete("/api/comic-books/{id}", comicBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComicBook> comicBookList = comicBookRepository.findAll();
        assertThat(comicBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComicBook.class);
        ComicBook comicBook1 = new ComicBook();
        comicBook1.setId(1L);
        ComicBook comicBook2 = new ComicBook();
        comicBook2.setId(comicBook1.getId());
        assertThat(comicBook1).isEqualTo(comicBook2);
        comicBook2.setId(2L);
        assertThat(comicBook1).isNotEqualTo(comicBook2);
        comicBook1.setId(null);
        assertThat(comicBook1).isNotEqualTo(comicBook2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComicBookDTO.class);
        ComicBookDTO comicBookDTO1 = new ComicBookDTO();
        comicBookDTO1.setId(1L);
        ComicBookDTO comicBookDTO2 = new ComicBookDTO();
        assertThat(comicBookDTO1).isNotEqualTo(comicBookDTO2);
        comicBookDTO2.setId(comicBookDTO1.getId());
        assertThat(comicBookDTO1).isEqualTo(comicBookDTO2);
        comicBookDTO2.setId(2L);
        assertThat(comicBookDTO1).isNotEqualTo(comicBookDTO2);
        comicBookDTO1.setId(null);
        assertThat(comicBookDTO1).isNotEqualTo(comicBookDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(comicBookMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(comicBookMapper.fromId(null)).isNull();
    }
}
