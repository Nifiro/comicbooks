package com.comicbooks.application.repository;

import com.comicbooks.application.domain.ComicBookGenres;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ComicBookGenres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComicBookGenresRepository extends JpaRepository<ComicBookGenres, Long>, JpaSpecificationExecutor<ComicBookGenres> {

}
