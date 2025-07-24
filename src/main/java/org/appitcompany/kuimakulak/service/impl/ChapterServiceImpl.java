package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.bookDto.ChapterRequest;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.BookChapters;
import org.appitcompany.kuimakulak.exceptions.CustomAlreadyExistsException;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.ChaptersMapper;
import org.appitcompany.kuimakulak.repository.BookChaptersRepo;
import org.appitcompany.kuimakulak.repository.BookDocRepo;
import org.appitcompany.kuimakulak.repository.BookRepo;
import org.appitcompany.kuimakulak.service.ChapterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final BookChaptersRepo bookChaptersRepo;
    private final BookRepo bookRepo;
    private final BookDocRepo bookDocRepo;

    @Override
    public ResponseEntity<?> saveChapter(ChapterRequest chapterRequest, Long bookId) {
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        BookChapters bookChapters1 = book.getChapters()
                .stream()
                .filter(bookChapters -> bookChapters.getChapterNumber().equals(chapterRequest.getChapterNumber()))
                .findFirst()
                .orElse(null);
        if (bookChapters1 != null) {
            throw new CustomAlreadyExistsException("Book Chapter already exists");
        }
        bookChapters1 = new BookChapters();
        bookChapters1.setChapterNumber(chapterRequest.getChapterNumber());
        bookChapters1.setChapterName(chapterRequest.getChapterName());
        bookChapters1.setAudioUrl(chapterRequest.getAudioUrl());
        bookChapters1.setBook(book);
        bookChaptersRepo.save(bookChapters1);
        book.getChapters().add(bookChapters1);
        bookRepo.save(book);
        bookDocument.setChapters(book.getChapters()
                .stream()
                .map(ChaptersMapper::toDoc)
                .toList());
        bookDocRepo.save(bookDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("book chapters saved successfully");
    }
}
