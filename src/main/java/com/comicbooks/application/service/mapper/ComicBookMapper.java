package com.comicbooks.application.service.mapper;

import com.comicbooks.application.domain.*;
import com.comicbooks.application.service.dto.ComicBookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ComicBook and its DTO ComicBookDTO.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface ComicBookMapper extends EntityMapper<ComicBookDTO, ComicBook> {

    @Mapping(source = "author.id", target = "authorId")
    ComicBookDTO toDto(ComicBook comicBook);

    @Mapping(source = "authorId", target = "author")
    ComicBook toEntity(ComicBookDTO comicBookDTO);

    default ComicBook fromId(Long id) {
        if (id == null) {
            return null;
        }
        ComicBook comicBook = new ComicBook();
        comicBook.setId(id);
        return comicBook;
    }
}
