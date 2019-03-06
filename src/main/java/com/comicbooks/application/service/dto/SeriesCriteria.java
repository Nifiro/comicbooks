package com.comicbooks.application.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Series entity. This class is used in SeriesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /series?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SeriesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter comicBookId;

    private LongFilter chapterId;

    public SeriesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getComicBookId() {
        return comicBookId;
    }

    public void setComicBookId(LongFilter comicBookId) {
        this.comicBookId = comicBookId;
    }

    public LongFilter getChapterId() {
        return chapterId;
    }

    public void setChapterId(LongFilter chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String toString() {
        return "SeriesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (comicBookId != null ? "comicBookId=" + comicBookId + ", " : "") +
                (chapterId != null ? "chapterId=" + chapterId + ", " : "") +
            "}";
    }

}
