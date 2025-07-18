package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity<?> saveBook(BookRequest bookRequest);

}
