package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.*;
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
    public List<BookDocument> getAllBookDoc(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        return bookService.getAllBookDoc(pageNumber,pageSize);
    }
    @Secured("ADMIN")
    @GetMapping("getAllBook")
    @Operation(summary = "Get all Book(\"ADMIN\")", description = "only admins can get all books")
    public PaginationResponse<AllBookResponse> getAllBook(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        return bookService.getAllBook(pageNumber,pageSize);
    }

   @Secured("ADMIN")
    @Operation(summary = "save book(\"ADMIN\")",description = "only admins can add books")
   @PostMapping("/save")
    public ResponseEntity<?> saveBook(@Valid @RequestBody BookRequest bookRequest){
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
    @Secured("USER")
    @GetMapping("/findByIdPlayer")
    @Operation(summary = "find by id for player", description = "only user can find by id")
    public BookByIdForPlayerResponse findByIdPlayer(@RequestParam Long bookId){
        return bookService.findByIdPlayer(bookId);
    }
    @Secured("ADMIN")
    @Operation(summary = "updated isNew in book(\"ADMIN\")",description = "only admins can")
    @PostMapping("/updatedIsNew")
    public String updatedIsNew(@RequestParam Long bookId,
                               @RequestParam boolean isNew ){
        return bookService.updatedIsNew(bookId,isNew);
    }
    @Secured("ADMIN")
    @Operation(summary = "updated isSanat in book(\"ADMIN\")" ,description = "only admins can")
    @PostMapping("/updatedIsSanat")
    public String updatedIsSanat(@RequestParam Long bookId,
                               @RequestParam boolean isSanat ){
        return bookService.updatedIsSanat(bookId,isSanat);
    }
    @Secured("ADMIN")
    @Operation(summary = "updated isBestseller in book(\"ADMIN\")",description = "only admins can")
    @PostMapping("/updatedIsBestseller")
    public String updatedIsBestseller(@RequestParam Long bookId,
                               @RequestParam boolean isBestseller ){
        return bookService.updatedIsBestseller(bookId,isBestseller);
    }
    @Secured("ADMIN")
    @Operation(summary = "updated isSoon in book",description = "only admins can")
    @PostMapping("/updatedIsSoon")
    public String updatedIsSoon(@RequestParam Long bookId,
                               @RequestParam boolean isSoon ){
        return bookService.updatedIsSoon(bookId,isSoon);
    }
    @Operation(summary = "get books by filtering by genre and author")
    @PostMapping("/filterByGenreAndAuthor")
    public  ResponseEntity<List<BookResponse>> getBooksByGenreAndAuthor(@RequestBody BookFilterRequest bookFilterRequest){
        return ResponseEntity.ok(bookService.filterByGenreAndAuthor(bookFilterRequest));
    }

}
