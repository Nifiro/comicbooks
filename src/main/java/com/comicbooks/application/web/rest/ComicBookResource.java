package com.comicbooks.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comicbooks.application.service.ComicBookQueryService;
import com.comicbooks.application.service.ComicBookService;
import com.comicbooks.application.service.dto.ComicBookCriteria;
import com.comicbooks.application.service.dto.ComicBookDTO;
import com.comicbooks.application.web.rest.errors.BadRequestAlertException;
import com.comicbooks.application.web.rest.util.HeaderUtil;
import com.comicbooks.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ComicBook.
 */
@RestController
@RequestMapping("/api")
public class ComicBookResource {

    private final Logger log = LoggerFactory.getLogger(ComicBookResource.class);

    private static final String ENTITY_NAME = "comicBook";

    private final ComicBookService comicBookService;

    private final ComicBookQueryService comicBookQueryService;

    public ComicBookResource(ComicBookService comicBookService, ComicBookQueryService comicBookQueryService) {
        this.comicBookService = comicBookService;
        this.comicBookQueryService = comicBookQueryService;
    }

    /**
     * POST  /comic-books : Create a new comicBook.
     *
     * @param comicBookDTO the comicBookDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comicBookDTO, or with status 400 (Bad Request) if the comicBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/comic-books")
    @Timed
    public ResponseEntity<ComicBookDTO> createComicBook(@Valid @RequestBody ComicBookDTO comicBookDTO) throws URISyntaxException {
        log.debug("REST request to save ComicBook : {}", comicBookDTO);
        if (comicBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new comicBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComicBookDTO result = comicBookService.save(comicBookDTO);
        return ResponseEntity.created(new URI("/api/comic-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comic-books : Updates an existing comicBook.
     *
     * @param comicBookDTO the comicBookDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comicBookDTO,
     * or with status 400 (Bad Request) if the comicBookDTO is not valid,
     * or with status 500 (Internal Server Error) if the comicBookDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/comic-books")
    @Timed
    public ResponseEntity<ComicBookDTO> updateComicBook(@Valid @RequestBody ComicBookDTO comicBookDTO) throws URISyntaxException {
        log.debug("REST request to update ComicBook : {}", comicBookDTO);
        if (comicBookDTO.getId() == null) {
            return createComicBook(comicBookDTO);
        }
        ComicBookDTO result = comicBookService.save(comicBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, comicBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comic-books : get all the comicBooks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of comicBooks in body
     */
    @GetMapping("/comic-books")
    @Timed
    public ResponseEntity<List<ComicBookDTO>> getAllComicBooks(ComicBookCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ComicBooks by criteria: {}", criteria);
        Page<ComicBookDTO> page = comicBookQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comic-books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /comic-books/:id : get the "id" comicBook.
     *
     * @param id the id of the comicBookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comicBookDTO, or with status 404 (Not Found)
     */
    @GetMapping("/comic-books/{id}")
    @Timed
    public ResponseEntity<ComicBookDTO> getComicBook(@PathVariable Long id) {
        log.debug("REST request to get ComicBook : {}", id);
        ComicBookDTO comicBookDTO = comicBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(comicBookDTO));
    }

    /**
     * DELETE  /comic-books/:id : delete the "id" comicBook.
     *
     * @param id the id of the comicBookDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/comic-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteComicBook(@PathVariable Long id) {
        log.debug("REST request to delete ComicBook : {}", id);
        comicBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/comic-books/upload")
    public ResponseEntity<ComicBookDTO> uploadComicBook(@RequestParam MultipartFile file, @RequestParam Long id,
                                                        @RequestParam String type) {
        log.debug("REST request to upload ComicBook: {}", id);
        ComicBookDTO comicBookDTO;
        log.debug("REST request to upload Book : {}:", id);
        try {
            comicBookDTO = comicBookService.uploadComicBook(file, id, type);
        } catch (FileSystemException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(comicBookDTO);
    }

    @GetMapping("/comic-books/{id}/cover")
    public ResponseEntity<Resource>/*HttpEntity<?>*/ downloadCover(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to download comic book cover : {}", id);
        ComicBookDTO dto = comicBookService.findOne(id);

        Resource coverResource;
        try {
            coverResource = comicBookService.loadFileAsResource(dto.getCoverPath());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }

        return constructResponseEntity(request, coverResource);
/*
        log.debug("REST request to get Book : {}", id);
        File cover = comicBookService.getCoverImage(id);

        byte[] image;

        try {
            image = FileCopyUtils.copyToByteArray(cover);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "octet-stream"));
        header.set("Content-Disposition", "inline; filename=" + cover.getName());
        header.setContentLength(image.length);

        return new HttpEntity<>(image, header);
*/
    }

    @GetMapping("/comic-books/{id}/background")
    public ResponseEntity<Resource> downloadBackground(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to download comic book background : {}", id);
        ComicBookDTO dto = comicBookService.findOne(id);

        Resource backgroundResource;
        try {
            backgroundResource = comicBookService.loadFileAsResource(dto.getImagePath());
        } catch (MalformedURLException | NullPointerException e) {
            return ResponseEntity.notFound().build();
        }

        return constructResponseEntity(request, backgroundResource);
    }

    private ResponseEntity<Resource> constructResponseEntity(HttpServletRequest request, Resource resource) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.info("Could not determine file type.");
        }
        if (contentType == null)
            contentType = "application/octet-stream";
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                + resource.getFilename() + "\"")
            .body(resource);
    }
}
