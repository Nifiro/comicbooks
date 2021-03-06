package com.comicbooks.application.service.mapper;

import com.comicbooks.application.domain.*;
import com.comicbooks.application.service.dto.ChapterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chapter and its DTO ChapterDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChapterMapper extends EntityMapper<ChapterDTO, Chapter> {

    @Mapping(source = "comicBookId", target = "comicBook.id")
    Chapter toEntity(ChapterDTO chapterDTO);

    @Mapping(source = "comicBook.id", target = "comicBookId")
    ChapterDTO toDto(Chapter chapter);

    default Chapter fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chapter chapter = new Chapter();
        chapter.setId(id);
        return chapter;
    }
}
