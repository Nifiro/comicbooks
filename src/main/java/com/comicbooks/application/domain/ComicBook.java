package com.comicbooks.application.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.comicbooks.application.domain.enumeration.Status;

/**
 * A ComicBook.
 */
@Entity
@Table(name = "comic_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComicBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "chapters", nullable = false)
    private Integer chapters;

    @NotNull
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "serialized_from")
    private Instant serializedFrom;

    @Column(name = "serialized_to")
    private Instant serializedTo;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    private Author author;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ComicBook title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getChapters() {
        return chapters;
    }

    public ComicBook chapters(Integer chapters) {
        this.chapters = chapters;
        return this;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public String getDescription() {
        return description;
    }

    public ComicBook description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public ComicBook publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Instant getSerializedFrom() {
        return serializedFrom;
    }

    public ComicBook serializedFrom(Instant serializedFrom) {
        this.serializedFrom = serializedFrom;
        return this;
    }

    public void setSerializedFrom(Instant serializedFrom) {
        this.serializedFrom = serializedFrom;
    }

    public Instant getSerializedTo() {
        return serializedTo;
    }

    public ComicBook serializedTo(Instant serializedTo) {
        this.serializedTo = serializedTo;
        return this;
    }

    public void setSerializedTo(Instant serializedTo) {
        this.serializedTo = serializedTo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ComicBook imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public ComicBook coverPath(String coverPath) {
        this.coverPath = coverPath;
        return this;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ComicBook createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public ComicBook createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ComicBook lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ComicBook lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Status getStatus() {
        return status;
    }

    public ComicBook status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Author getAuthor() {
        return author;
    }

    public ComicBook author(Author author) {
        this.author = author;
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
        ComicBook comicBook = (ComicBook) o;
        if (comicBook.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comicBook.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComicBook{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", chapters=" + getChapters() +
            ", description='" + getDescription() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", serializedFrom='" + getSerializedFrom() + "'" +
            ", serializedTo='" + getSerializedTo() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", coverPath='" + getCoverPath() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
