package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.entity.*;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.BookMapper;
import org.appitcompany.kuimakulak.repository.*;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
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
            author1.getBooks().add(saveBook);
    }
        bookRequest.getTranslatorName().stream()
                .map(translator -> {
                    Contributor translator1 = contributorRepo.findByName(translator);
                    if (translator1 == null || !translator1.getRole().equals(ContributorRole.TRANSLATOR)) {
                        throw new NotFoundException("Translator not found! Add translator first!!! " + translator1);
                    }
                    return translator1;
                }).forEach(translator1 -> translator1.getBooks().add(saveBook));
        bookRequest.getNarratorName().stream()
                .map(narrator -> {
                    Contributor narrator1 = contributorRepo.findByName(narrator);
                    if (narrator1 == null || !narrator1.getRole().equals(ContributorRole.NARRATOR)) {
                        throw new NotFoundException("Narrator not found! Add narrator first!!! " + narrator1);
                    }
                    return narrator1;
                }).forEach(narrator1 -> narrator1.getBooks().add(saveBook));
        bookRequest.getGenreName().stream()
                .map(genreName -> {
                    System.err.println("genreName = " + genreName);
                    Genre genre = genreRepo.findByName(genreName);
                    System.err.println("genre = " + genre);
                    if (genre == null) {
                        throw new NotFoundException("Genre not found! Add genre first!!! " + genreName);
                    }
                    return genre;
                }).forEach(genre -> genre.getBooks().add(saveBook));

        Listeners listeners = book.getListeners();
        if (listeners == null) {
            listeners = new Listeners();
            listeners.setCountListeners(0);
            listeners.setBook(saveBook);

        }
        Listeners listener = listenersRepo.save(listeners);
        book.setListeners(listener);
        BookChapters bookChapters = new BookChapters();
        bookChapters.setBook(saveBook);
        bookChapters.setChapterName(bookRequest.getChapterName());
        bookChapters.setChapterNumber(bookRequest.getChapterNumber());
        bookChapters.setAudioUrl(bookRequest.getAudioUrl());
        bookChaptersRepo.save(bookChapters);

        BookDocument bookDocument = BookMapper.toBookDocument(saveBook);
        bookDocRepo.save(bookDocument);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("book has been saved successfully");
    }
}
