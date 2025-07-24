package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.*;
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

    PaginationResponse<AllBookResponse> getAllBook(int pageNumber, int pageSize);

    String updatedIsNew(Long bookId, boolean isNew);

    String updatedIsSanat(Long bookId, boolean isSanat);

    String updatedIsBestseller(Long bookId, boolean isBestseller);

    String updatedIsSoon(Long bookId, boolean isSoon);
}

