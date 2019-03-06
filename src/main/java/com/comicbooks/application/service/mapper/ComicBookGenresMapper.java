package com.comicbooks.application.service.mapper;

import com.comicbooks.application.domain.*;
import com.comicbooks.application.service.dto.ComicBookGenresDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ComicBookGenres and its DTO ComicBookGenresDTO.
 */
@Mapper(componentModel = "spring", uses = {ComicBookMapper.class, GenreMapper.class})
public interface ComicBookGenresMapper extends EntityMapper<ComicBookGenresDTO, ComicBookGenres> {

    @Mapping(source = "comicBook.id", target = "comicBookId")
    @Mapping(source = "genre.id", target = "genreId")
    ComicBookGenresDTO toDto(ComicBookGenres comicBookGenres);

    @Mapping(source = "comicBookId", target = "comicBook")
    @Mapping(source = "genreId", target = "genre")
    ComicBookGenres toEntity(ComicBookGenresDTO comicBookGenresDTO);

    default ComicBookGenres fromId(Long id) {
        if (id == null) {
            return null;
        }
        ComicBookGenres comicBookGenres = new ComicBookGenres();
        comicBookGenres.setId(id);
        return comicBookGenres;
    }
}
