package com.comicbooks.application.service.mapper;

import com.comicbooks.application.domain.*;
import com.comicbooks.application.service.dto.SeriesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Series and its DTO SeriesDTO.
 */
@Mapper(componentModel = "spring", uses = {ComicBookMapper.class, ChapterMapper.class})
public interface SeriesMapper extends EntityMapper<SeriesDTO, Series> {

    @Mapping(source = "comicBook.id", target = "comicBookId")
    @Mapping(source = "chapter.id", target = "chapterId")
    SeriesDTO toDto(Series series);

    @Mapping(source = "comicBookId", target = "comicBook")
    @Mapping(source = "chapterId", target = "chapter")
    Series toEntity(SeriesDTO seriesDTO);

    default Series fromId(Long id) {
        if (id == null) {
            return null;
        }
        Series series = new Series();
        series.setId(id);
        return series;
    }
}
