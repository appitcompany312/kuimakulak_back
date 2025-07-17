package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
   private final BookService bookService;
//   @Secured("ADMIN")
    @Operation(summary = "save book",description = "only admins can add books")
   @PostMapping("/save")
    public ResponseEntity<?> saveBook(BookRequest bookRequest){
        return bookService.saveBook(bookRequest);
    }
}
