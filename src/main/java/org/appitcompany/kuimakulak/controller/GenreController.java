package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenresNameResponse;
import org.appitcompany.kuimakulak.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    @Secured("ROLE_ADMIN")
    @Operation(summary = "save genre(\"ADMIN\")",description = "only admins can add genres")
    @PostMapping("/save")
    public ResponseEntity<?> saveGenre(@Valid @RequestBody GenreRequest genreRequest){
        return genreService.saveGenre(genreRequest);
    }
    @Operation(summary = "get all ganres")
    @GetMapping("/getAll")
    public List<String> getAllGenres(){
        return genreService.getAllGenres();
    }

    @Operation(summary = "get all genres by author")
    @GetMapping("/getByAuthor")
    public ResponseEntity<GenresNameResponse> getAllGenresByAuthor(String author){
        return ResponseEntity.ok(genreService.getGenresByAuthor(author));
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "update genre by id (\"ADMIN\")",description = "only admins can update genres")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreRequest genreRequest){
        return genreService.updateGenre(id, genreRequest);
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "delete genre by id(\"ADMIN\")",description = "only admins can delete genres")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id){
        return genreService.deleteGenre(id);
    }
}
