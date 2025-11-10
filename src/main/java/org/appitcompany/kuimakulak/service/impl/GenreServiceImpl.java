package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenreResponse;
import org.appitcompany.kuimakulak.dto.genreDto.UpdatedGenreRequest;
import org.appitcompany.kuimakulak.entity.Genre;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.jpaRepository.GenreRepo;
import org.appitcompany.kuimakulak.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepo genreRepo;
    @Override
    public ResponseEntity<?> saveGenre(GenreRequest genreRequest) {
        Genre byGenreName = genreRepo.findByName(genreRequest.getGenreName());
        if (byGenreName != null) {
            throw new CustomAlreadyExistsException(" This genre already exists in the database!");}
        Genre genre = new Genre();
        genre.setGenreName(genreRequest.getGenreName());
        genre.setAddedDate(LocalDate.now());
        genreRepo.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("genre has been saved successfully");
    }

    @Override
    public List<GenreResponse> getAllGenre() {
        List<Genre> genres = genreRepo.findAll();
        return genres.stream()
                .map(genre -> new GenreResponse(
                        genre.getId(),
                        genre.getGenreName(),
                        genre.getBooks() != null ? genre.getBooks().size() : 0,
                        genre.getAddedDate()
                ))
                .toList();
    }

    @Override
    public ResponseEntity<?> updateGenre(Long genreId, UpdatedGenreRequest genreRequest) {
        Genre genre = genreRepo.findById(genreId).orElseThrow(
                ()-> new NotFoundException("Genre not found"));
        genre.setGenreName(genreRequest.name());
        genreRepo.save(genre);
        return ResponseEntity.status(HttpStatus.OK)
                .body("genre has been updated successfully");
    }

    @Override
    public ResponseEntity<?> deleteGenre(Long genreId) {
        Genre genre = genreRepo.findById(genreId).orElseThrow(
                ()-> new NotFoundException("Genre not found"));
        genreRepo.deleteById(genre.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body("genre has been deleted successfully");
    }
}
