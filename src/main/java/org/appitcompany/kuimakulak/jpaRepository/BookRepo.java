package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book,Long> {
    default Book getBookByBookIdOrElseThrow(Long bookId) {
        return findById(bookId).orElseThrow(()-> new NotFoundException("Book with id " + bookId + " not found"));
    }

    @Query(value = """
select b.* from books b
join contributor_books cb on b.id = cb.book_id
join contributors c on cb.contributor_id = c.id
join genre_books gb on gb.book_id = cb.book_id
join genre g on g.id = gb.genre_id
where c.full_name = :author
and g.genre_name = :genre
""",
    nativeQuery = true)
    List<Book> getBooksByGenreAndAuthor(@Param("author") String author, @Param("genre") String genre);
}
