package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreRequest;
import org.appitcompany.kuimakulak.dto.genreDto.GenresNameResponse;
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
import java.util.stream.Collectors;

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
    public List<String> getAllGenres() {
        List<Genre> all = genreRepo.findAll();
        return all.stream().map(Genre::getGenreName).collect(Collectors.toList());
    }

    @Override
    public GenresNameResponse getGenresByAuthor(String author) {
        List<String> byAuthor = genreRepo.findByAuthor(author);
        return GenresNameResponse.builder()
                .genres(byAuthor)
                .build();
    }

    @Override
    public ResponseEntity<?> updateGenre(Long id, GenreRequest genreRequest) {
        Genre genre = genreRepo.findById(id).orElseThrow(
                ()-> new NotFoundException("No genre found with id: " + id));
        genre.setGenreName(genreRequest.getGenreName());
        genreRepo.save(genre);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Жанр успешно обнавлен на "+genreRequest.getGenreName());
    }

    @Override
    public ResponseEntity<?> deleteGenre(Long genreId) {
        Genre genre = genreRepo.findById(genreId).orElseThrow(
                ()-> new NotFoundException("No genre found with id: " + genreId));
        genreRepo.delete(genre);
        return ResponseEntity.status(HttpStatus.OK)
                .body("genre has been deleted successfully");
    }
}
