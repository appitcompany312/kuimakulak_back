package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenresNameResponse;
import org.appitcompany.kuimakulak.entity.Genre;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenreService {
    ResponseEntity<?> saveGenre(GenreRequest genreRequest);


    List<String> getAllGenres();

    GenresNameResponse getGenresByAuthor(String author);

    ResponseEntity<?> updateGenre(Long id,GenreRequest genreRequest);

    ResponseEntity<?> deleteGenre(Long genreId);
}
