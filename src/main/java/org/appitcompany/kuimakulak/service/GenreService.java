package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.springframework.http.ResponseEntity;

public interface GenreService {
    ResponseEntity<?> saveGenre(GenreRequest genreRequest);
}
