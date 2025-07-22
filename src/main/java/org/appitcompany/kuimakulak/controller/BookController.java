package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponseById;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
   private final BookService bookService;

    @GetMapping("getAllBookDoc")
    @Operation(summary = "Get all BookDoc", description = "Fetches all Book documents from Elasticsearch")
    public List<BookDocument> getAllPodcastDoc(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        return bookService.getAllBookDoc(pageNumber,pageSize);
    }

//   @Secured("ADMIN")
    @Operation(summary = "save book",description = "only admins can add books")
   @PostMapping("/save")
    public ResponseEntity<?> saveBook(@RequestBody BookRequest bookRequest){
        return bookService.saveBook(bookRequest);
    }
  @Secured("USER")
    @GetMapping("/getBookByGenre")
    @Operation(summary = "get books by genre",description = "only user can search")
    public PaginationResponse<BookResponse> getBookByGenre(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam String genreName){
        return bookService.getBookForGenre(genreName,pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBookIsSoon")
    @Operation(summary = "get books by is soon",description = "only user can search")
    public PaginationResponse<BookResponse> getBookIsSoon(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.getBookIsSoon(pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBookIsNew")
    @Operation(summary = "get books by is new",description = "only user can search")
    public PaginationResponse<BookResponse> getBookIsNew(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.getBookIsNew(pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBookIsBestseller")
    @Operation(summary = "get books by is bestseller",description = "only user can search")
    public PaginationResponse<BookResponse> getBookIsBestseller(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.getBookIsBestseller(pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBooksRecommendation")
    @Operation(summary = "get books recommendation",description = "only user can search")
    public PaginationResponse<BookResponse> getBooksRecommendation(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.getBooksRecommendation(pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBookMostRead")
    @Operation(summary = "get books most read",description = "only user can search")
    public PaginationResponse<BookResponse> getBookMostRead(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.getBookMostRead(pageNumber,pageSize);
    }
     @Secured("USER")
    @GetMapping("/getBookBySanat")
    @Operation(summary = "get books by sanat",description = "only user can search")
    public PaginationResponse<BookResponse> etBookBySanat(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize){
        return bookService.etBookBySanat(pageNumber,pageSize);
    }
    @Secured("USER")
    @GetMapping("/findById")
    @Operation(summary = "find by id book", description = "only user can find by id")
    public BookResponseById findById(@RequestParam Long bookId){
     return bookService.findById(bookId);
    }
}
