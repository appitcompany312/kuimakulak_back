package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.entity.*;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.BookMapper;
import org.appitcompany.kuimakulak.repository.*;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserRepository userRepository;
    private final BookRepo bookRepo;
    private final ContributorRepo contributorRepo;
    private final GenreRepo genreRepo;
    private final BookDocRepo bookDocRepo;
    private final BookChaptersRepo bookChaptersRepo;
    private final ListenersRepo listenersRepo;

    @Override
    @Transactional
    public ResponseEntity<?> saveBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setBookName(bookRequest.getBookName());
        book.setDescription(bookRequest.getDescription());
        book.setBestseller(bookRequest.isBestseller());
        book.setBannerUrl(bookRequest.getBannerUrl());
        book.setNew(bookRequest.isNew());
        book.setSoon(bookRequest.isSoon());
        book.setSanat(bookRequest.isSanat());
        book.setPublisher(bookRequest.getPublisher());
        book.setPublicationDate(LocalDate.now());
        book.setPageCount(bookRequest.getPageCount());
        Book saveBook = bookRepo.save(book);
        for (String author : bookRequest.getAuthorName()) {
            Contributor author1 = contributorRepo.findByName(author);
            if (author1 == null || !author1.getRole().equals(ContributorRole.AUTHOR)) {
                throw new NotFoundException("Author not found! Add author first!!! " + author);
            }
            saveBook.getContributors().add(author1);
            author1.getBooks().add(book);
        }
        bookRequest.getTranslatorName().stream()
                .map(translator -> {
                    Contributor translator1 = contributorRepo.findByName(translator);
                    if (translator1 == null || !translator1.getRole().equals(ContributorRole.TRANSLATOR)) {
                        throw new NotFoundException("Translator not found! Add translator first!!! " + translator1);
                    }
                    saveBook.getContributors().add(translator1);
                    return translator1;
                }).forEach(translator1 -> translator1.getBooks().add(saveBook));
        bookRequest.getNarratorName().stream()
                .map(narrator -> {
                    Contributor narrator1 = contributorRepo.findByName(narrator);
                    if (narrator1 == null || !narrator1.getRole().equals(ContributorRole.NARRATOR)) {
                        throw new NotFoundException("Narrator not found! Add narrator first!!! " + narrator1);
                    }
                    saveBook.getContributors().add(narrator1);
                    return narrator1;
                }).forEach(narrator1 -> narrator1.getBooks().add(saveBook));
        bookRequest.getGenreName().stream()
                .map(genreName -> {
                    Genre genre = genreRepo.findByName(genreName);
                    if (genre == null) {
                        throw new NotFoundException("Genre not found! Add genre first!!! " + genreName);
                    }
                    saveBook.getGenres().add(genre);
                    return genre;
                }).forEach(genre -> genre.getBooks().add(saveBook) );

        Listeners listeners = book.getListeners();
        if (listeners == null) {
            listeners = new Listeners();
            listeners.setCountListeners(0);
            listeners.setBook(book);

        }
        Listeners listener = listenersRepo.save(listeners);
        saveBook.setListeners(listener);
        BookChapters bookChapters = new BookChapters();
        bookChapters.setBook(saveBook);
        bookChapters.setChapterName(bookRequest.getChapterName());
        bookChapters.setChapterNumber(bookRequest.getChapterNumber());
        bookChapters.setAudioUrl(bookRequest.getAudioUrl());
        BookChapters save = bookChaptersRepo.save(bookChapters);
        saveBook.getChapters().add(save);
        Book saveBook1 = bookRepo.save(saveBook);
        BookDocument bookDocument = BookMapper.toBookDocument(saveBook1);
        bookDocRepo.save(bookDocument);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("book has been saved successfully");
    }

    @Override
    public PaginationResponse<BookResponse> getBookForGenre(String genreName, int pageNumber, int pageSize) {
        List<String> genres = new ArrayList<>();
        genres.add(genreName);
        int offset = (pageNumber - 1) * pageSize;
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("User not found!"));
        List<BookDocument> allBooks = bookDocRepo.findByGenres(genres).stream()
                .filter(book -> !book.isSoon())
                .toList();

        int totalElements = allBooks.size();
        int totalPages = (int) Math.ceil((double) totalElements / (double) pageSize);

        List<BookDocument> pagedBooks = allBooks.stream()
                .skip(offset)
                .limit(pageSize)
                .toList();

        List<BookResponse> responses = pagedBooks.stream()
                .map(doc -> {
                    BookResponse response = new BookResponse();
                    response.setId(doc.getId());
                    response.setBookName(doc.getBookName());
                    response.setBanner_url(doc.getBannerUrl());
                    response.setRating(doc.getAverageRating());
                    response.setGenreName(doc.getGenres());
                    response.setAuthor((doc.getContributors() != null && !doc.getContributors().isEmpty())
                            ? doc.getContributors().get(0)
                            : "Автор жок");
                    response.setPublicationDate(Instant.ofEpochMilli(doc.getPublicationDate())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    Long currentUserId =1L;
                    boolean isHistory = doc.getUserIds() != null && doc.getUserIds().contains(currentUserId);
                    response.setHistory(isHistory);
                    return response;
                })
                .toList();

        return PaginationResponse.<BookResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(responses)
                .build();
    }
}
