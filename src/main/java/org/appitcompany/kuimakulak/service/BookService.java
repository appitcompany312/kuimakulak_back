package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookByIdForPlayerResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponseById;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    ResponseEntity<?> saveBook(BookRequest bookRequest);

    PaginationResponse<BookResponse> getBookForGenre(String genreName, int pageNumber, int pageSize);

    PaginationResponse<BookResponse> getBookIsSoon(int pageNumber, int pageSize);

    PaginationResponse<BookResponse> getBookIsNew(int pageNumber, int pageSize);

    PaginationResponse<BookResponse> getBookIsBestseller(int pageNumber, int pageSize);

    PaginationResponse<BookResponse> getBooksRecommendation(int pageNumber, int pageSize);

    PaginationResponse<BookResponse> getBookMostRead(int pageNumber, int pageSize);

    PaginationResponse<BookResponse> etBookBySanat(int pageNumber, int pageSize);

    List<BookDocument> getAllBookDoc(int pageNumber, int pageSize);

    BookResponseById findById(Long bookId);

    BookByIdForPlayerResponse findByIdPlayer(Long bookId);
}
