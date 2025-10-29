package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepo extends JpaRepository<Book,Long> {
    default Book getBookByBookIdOrElseThrow(Long bookId) {
        return findById(bookId).orElseThrow(()-> new NotFoundException("Book with id " + bookId + " not found"));
    }


}
