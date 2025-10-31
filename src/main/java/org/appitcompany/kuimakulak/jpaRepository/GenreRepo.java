package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepo extends JpaRepository<Genre, Long> {
   @Query("select g from Genre g where g.genreName= :name")
    Genre findByName(@Param("name") String name);

    List<Genre> findByGenreName(String genreName);

    @Query("SELECT DISTINCT g FROM Genre g JOIN FETCH g.books")
    List<Genre> findAllWithBooks();

    @Query(value = "select distinct g.genre_name from genre g " +
            "join genre_books gb on g.id = gb.genre_id " +
            "join contributor_books cb on gb.book_id = cb.book_id " +
            "join contributors c on cb.contributor_id = c.id " +
            "where c.full_name = :author ;",
    nativeQuery = true)
    List<String> findByAuthor(@Param ("author")String author);
}
