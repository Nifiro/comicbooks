package com.comicbooks.application.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ComicBookGenres entity.
 */
public class ComicBookGenresDTO implements Serializable {

    private Long id;

    private Long comicBookId;

    private Long genreId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComicBookId() {
        return comicBookId;
    }

    public void setComicBookId(Long comicBookId) {
        this.comicBookId = comicBookId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComicBookGenresDTO comicBookGenresDTO = (ComicBookGenresDTO) o;
        if(comicBookGenresDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comicBookGenresDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComicBookGenresDTO{" +
            "id=" + getId() +
            "}";
    }
}
