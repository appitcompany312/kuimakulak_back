package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity<?> saveBook(BookRequest bookRequest);

    PaginationResponse<BookResponse> getBookForGenre(String genreName, int pageNumber, int pageSize);
}
