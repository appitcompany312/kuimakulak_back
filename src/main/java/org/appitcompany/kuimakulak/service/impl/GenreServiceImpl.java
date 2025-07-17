package org.appitcompany.kuimakulak.service.impl;

import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.entity.Genre;
import org.appitcompany.kuimakulak.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GenreServiceImpl implements GenreService {
    @Override
    public ResponseEntity<?> saveGenre(GenreRequest genreRequest) {
        Genre genre = new Genre();
        genre.setGenreName(genreRequest.getGenreName());
        genre.setAddedDate(LocalDate.now());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("genre has been saved successfully");
    }
}
