package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenreResponse;
import org.appitcompany.kuimakulak.dto.genreDto.UpdatedGenreRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenreService {
    ResponseEntity<?> saveGenre(GenreRequest genreRequest);
    List<GenreResponse> getAllGenre();
    ResponseEntity<?> updateGenre(Long genreId, UpdatedGenreRequest genreRequest);
    ResponseEntity<?> deleteGenre(Long genreId);

}
