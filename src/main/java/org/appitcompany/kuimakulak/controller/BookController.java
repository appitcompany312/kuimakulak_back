package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
   private final BookService bookService;
//   @Secured("ADMIN")
    @Operation(summary = "save book",description = "only admins can add books")
   @PostMapping("/save")
    public ResponseEntity<?> saveBook(@RequestBody BookRequest bookRequest){
        return bookService.saveBook(bookRequest);
    }
//    @Secured("USER")
    @GetMapping("/getBookForGenre")
    public PaginationResponse<BookResponse> getBookForGenre(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam String genreName){
        return bookService.getBookForGenre(genreName,pageNumber,pageSize);
    }
}
