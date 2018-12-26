package com.comicbooks.application.service;

import com.comicbooks.application.config.StorageProperties;
import com.comicbooks.application.domain.ComicBook;
import com.comicbooks.application.repository.ComicBookRepository;
import com.comicbooks.application.service.dto.ChapterDTO;
import com.comicbooks.application.service.dto.ComicBookDTO;
import com.comicbooks.application.service.mapper.ComicBookMapper;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Service Implementation for managing ComicBook.
 */
@Service
@Transactional
public class ComicBookService {

    private final Logger log = LoggerFactory.getLogger(ComicBookService.class);

    private final Path storageLocation;

    private final ComicBookRepository comicBookRepository;

    private final ChapterService chapterService;

    private final ComicBookMapper comicBookMapper;

    public ComicBookService(ComicBookRepository comicBookRepository, StorageProperties storageProperties,
                            ChapterService chapterService, ComicBookMapper comicBookMapper) {
        this.comicBookRepository = comicBookRepository;
        this.chapterService = chapterService;
        this.comicBookMapper = comicBookMapper;
        this.storageLocation = Paths.get(storageProperties.getUploadDir());
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            log.error("Could not create the directory where uploaded comic books will be stored");
        }
    }

    /**
     * Save a comicBook.
     *
     * @param comicBookDTO the entity to save
     * @return the persisted entity
     */
    public ComicBookDTO save(ComicBookDTO comicBookDTO) {
        log.debug("Request to save ComicBook : {}", comicBookDTO);
        ComicBook comicBook = comicBookMapper.toEntity(comicBookDTO);
        comicBook = comicBookRepository.save(comicBook);
        return comicBookMapper.toDto(comicBook);
    }

    /**
     * Get all the comicBooks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ComicBookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ComicBooks");
        return comicBookRepository.findAll(pageable)
            .map(comicBookMapper::toDto);
    }

    /**
     * Get one comicBook by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ComicBookDTO findOne(Long id) {
        log.debug("Request to get ComicBook : {}", id);
        ComicBook comicBook = comicBookRepository.findOne(id);
        return comicBookMapper.toDto(comicBook);
    }

    /**
     * Delete the comicBook by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ComicBook : {}", id);
        comicBookRepository.delete(id);
    }

    public ChapterDTO uploadChapter(MultipartFile file, Long id, Long chapterId) throws FileSystemException {
        ComicBookDTO comicBookDTO = findOne(id);
        ChapterDTO chapterDTO = chapterService.findOne(chapterId);
        Path comicBookDir = storageLocation.resolve(comicBookDTO.getId().toString());
        Path targetLocation = comicBookDir.resolve(chapterId.toString());
        if (!Files.exists(targetLocation)) {
            try {
                Files.createDirectories(targetLocation);
            } catch (IOException e) {
                throw new FileSystemException("Could not create chapter directory: "
                    + targetLocation.getFileName());
            }
        }
        try {
            byte[] buffer = new byte[1024];
            File convertedFile = new File(targetLocation.resolve(file.getOriginalFilename()).toString());
            if(convertedFile.createNewFile())
            {
                FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
                fileOutputStream.write(file.getBytes());
                fileOutputStream.close();
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(convertedFile));
            int page = 1;
            ZipEntry entry = zipInputStream.getNextEntry();
            String extension = "";
            while (entry != null) {
                String name = entry.getName();
                extension = name.substring(name.lastIndexOf('.') + 1);
                File pageFile = new File(targetLocation.toString(), String.valueOf(page) + "." + extension);
                FileOutputStream outputStream = new FileOutputStream(pageFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                entry = zipInputStream.getNextEntry();
                page++;
            }
            chapterDTO.setFilePath(targetLocation.toString() + File.separator + "." + extension);
            zipInputStream.closeEntry();
            zipInputStream.close();
            if(convertedFile.delete())
                log.debug("Temporary file was successfully deleted");
        } catch (IOException e) {
            throw new FileSystemException("Could not store file " + file.getName());
        }
        return chapterService.save(chapterDTO);
    }

    public ComicBookDTO uploadComicBook(MultipartFile file, Long id, String type) throws BadRequestException, FileSystemException {
        ComicBookDTO comicBookDTO = findOne(id);
        String extension = "";
        int start = file.getOriginalFilename().lastIndexOf('.');
        if (start != -1)
            extension = file.getOriginalFilename().substring(start).trim();
        String fileName;
        Path targetLocation;
        Path comicBookDir = storageLocation.resolve(comicBookDTO.getId().toString());
        if (!Files.exists(comicBookDir)) {
            try {
                Files.createDirectories(comicBookDir);
            } catch (IOException e) {
                throw new FileSystemException("Could not create comic book directory: "
                    + comicBookDir.getFileName());
            }
        }
        switch (type) {
            case "background": {
                fileName = comicBookDTO.getId() + "-background" + extension;
                targetLocation = comicBookDir.resolve(fileName);
                comicBookDTO.setImagePath(targetLocation.toString());
                break;
            }
            case "cover": {
                fileName = comicBookDTO.getId() + "-cover" + extension;
                targetLocation = comicBookDir.resolve(fileName);
                comicBookDTO.setCoverPath(targetLocation.toString());
                break;
            }
            default:
                throw new BadRequestException("Unknown parameter: " + type);
        }
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            byte[] bytes = file.getBytes();

            Files.write(targetLocation, bytes);
        } catch (IOException e) {
            throw new FileSystemException("Could not store file " + fileName);
        }
        return save(comicBookDTO);
    }

    public Resource loadFileAsResource(String path) throws MalformedURLException {
        Path filePath = Paths.get(path);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists())
            return resource;
        else
            throw new MalformedURLException("File not found: " + path);
    }
}
