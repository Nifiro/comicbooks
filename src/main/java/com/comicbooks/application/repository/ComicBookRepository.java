package com.comicbooks.application.repository;

import com.comicbooks.application.domain.ComicBook;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ComicBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComicBookRepository extends JpaRepository<ComicBook, Long>, JpaSpecificationExecutor<ComicBook> {

}
