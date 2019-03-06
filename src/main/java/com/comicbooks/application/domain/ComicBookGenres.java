package com.comicbooks.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ComicBookGenres.
 */
@Entity
@Table(name = "comic_book_genres")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComicBookGenres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private ComicBook comicBook;

    @ManyToOne
    private Genre genre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComicBook getComicBook() {
        return comicBook;
    }

    public ComicBookGenres comicBook(ComicBook comicBook) {
        this.comicBook = comicBook;
        return this;
    }

    public void setComicBook(ComicBook comicBook) {
        this.comicBook = comicBook;
    }

    public Genre getGenre() {
        return genre;
    }

    public ComicBookGenres genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComicBookGenres comicBookGenres = (ComicBookGenres) o;
        if (comicBookGenres.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comicBookGenres.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComicBookGenres{" +
            "id=" + getId() +
            "}";
    }
}
