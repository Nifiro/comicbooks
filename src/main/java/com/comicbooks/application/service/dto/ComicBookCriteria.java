package com.comicbooks.application.service.dto;

import java.io.Serializable;
import com.comicbooks.application.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the ComicBook entity. This class is used in ComicBookResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /comic-books?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComicBookCriteria implements Serializable {
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private IntegerFilter chapters;

    private StringFilter publisher;

    private InstantFilter serializedFrom;

    private InstantFilter serializedTo;

    private StringFilter imagePath;

    private StringFilter coverPath;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private StatusFilter status;

    private LongFilter authorId;

    public ComicBookCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getChapters() {
        return chapters;
    }

    public void setChapters(IntegerFilter chapters) {
        this.chapters = chapters;
    }

    public StringFilter getPublisher() {
        return publisher;
    }

    public void setPublisher(StringFilter publisher) {
        this.publisher = publisher;
    }

    public InstantFilter getSerializedFrom() {
        return serializedFrom;
    }

    public void setSerializedFrom(InstantFilter serializedFrom) {
        this.serializedFrom = serializedFrom;
    }

    public InstantFilter getSerializedTo() {
        return serializedTo;
    }

    public void setSerializedTo(InstantFilter serializedTo) {
        this.serializedTo = serializedTo;
    }

    public StringFilter getImagePath() {
        return imagePath;
    }

    public void setImagePath(StringFilter imagePath) {
        this.imagePath = imagePath;
    }

    public StringFilter getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(StringFilter coverPath) {
        this.coverPath = coverPath;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "ComicBookCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (chapters != null ? "chapters=" + chapters + ", " : "") +
                (publisher != null ? "publisher=" + publisher + ", " : "") +
                (serializedFrom != null ? "serializedFrom=" + serializedFrom + ", " : "") +
                (serializedTo != null ? "serializedTo=" + serializedTo + ", " : "") +
                (imagePath != null ? "imagePath=" + imagePath + ", " : "") +
                (coverPath != null ? "coverPath=" + coverPath + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (authorId != null ? "authorId=" + authorId + ", " : "") +
            "}";
    }

}
