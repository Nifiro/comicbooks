package com.comicbooks.application.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.comicbooks.application.domain.enumeration.Status;

/**
 * A DTO for the ComicBook entity.
 */
public class ComicBookDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Integer chapters;

    @NotNull
    @Lob
    private String description;

    private String publisher;

    private Instant serializedFrom;

    private Instant serializedTo;

    private String imagePath;

    private String coverPath;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Status status;

    private Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getChapters() {
        return chapters;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Instant getSerializedFrom() {
        return serializedFrom;
    }

    public void setSerializedFrom(Instant serializedFrom) {
        this.serializedFrom = serializedFrom;
    }

    public Instant getSerializedTo() {
        return serializedTo;
    }

    public void setSerializedTo(Instant serializedTo) {
        this.serializedTo = serializedTo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComicBookDTO comicBookDTO = (ComicBookDTO) o;
        if(comicBookDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comicBookDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComicBookDTO{" +
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
