package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.entity.Genre;
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
    @Operation(summary = "get all ganres")
    @GetMapping("/getAll")
    public List<String> getAllGenres(){
        return genreService.getAllGenres();
    }
}
