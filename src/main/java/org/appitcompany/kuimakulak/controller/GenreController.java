package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenreResponse;
import org.appitcompany.kuimakulak.dto.genreDto.UpdatedGenreRequest;
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
    @Secured("ADMIN")
    @Operation(summary = "save genre(\"ADMIN\")",description = "only admins can add genres")
    @PostMapping("/save")
    public ResponseEntity<?> saveGenre(@Valid @RequestBody GenreRequest genreRequest){
        return genreService.saveGenre(genreRequest);
    }

    @Secured("ADMIN")
    @Operation(summary = "get all genre(\"ADMIN\")",description = "only admins can see all genres")
    @GetMapping("/get/all")
    public List<GenreResponse> getAllGenre(){
        return genreService.getAllGenre();
    }

    @Secured("ADMIN")
    @PutMapping("/update/{genreId}")
    public ResponseEntity<?> updateGenre(@PathVariable Long genreId, @RequestBody UpdatedGenreRequest genreRequest){
        return genreService.updateGenre(genreId,genreRequest);
    }

    @Secured("ADMIN")
    @DeleteMapping("/delete/{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long genreId){
        return genreService.deleteGenre(genreId);
    }
}
