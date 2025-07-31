package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookChaptersDocument;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.*;
import org.appitcompany.kuimakulak.entity.*;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.BookMapper;
import org.appitcompany.kuimakulak.jpaRepository.*;
import org.appitcompany.kuimakulak.elasticRepository.BookDocRepo;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

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
            Contributor author1 = contributorRepo.findByFullNameAndRole(author, ContributorRole.AUTHOR);
            if (author1 == null) {
                throw new NotFoundException("Author not found! Add author first!!! " + author);
            }
            saveBook.getContributors().add(author1);
            author1.getBooks().add(book);
        }
        bookRequest.getTranslatorName().stream()
                .map(translator -> {
                    Contributor translator1 = contributorRepo.findByFullNameAndRole(translator, ContributorRole.TRANSLATOR);
                    if (translator1 == null) {
                        throw new NotFoundException("Translator not found! Add translator first!!! " + translator1);
                    }
                    saveBook.getContributors().add(translator1);
                    return translator1;
                }).forEach(translator1 -> translator1.getBooks().add(saveBook));
        bookRequest.getNarratorName().stream()
                .map(narrator -> {
                    Contributor narrator1 = contributorRepo.findByFullNameAndRole(narrator, ContributorRole.NARRATOR);
                    if (narrator1 == null) {
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
                }).forEach(genre -> genre.getBooks().add(saveBook));

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
        User user = getCurrentUser();
        List<BookDocument> allBooks = bookDocRepo.findByGenres(genres).stream()
                .filter(book -> !book.isSoon())
                .sorted(Comparator.comparing(BookDocument::getPublicationDate).reversed())
                .toList();
        if (allBooks.isEmpty()) {
            throw new NotFoundException("Soon document not found!");
        }
        return buildPaginationResponse(allBooks, pageNumber, pageSize, user.getId());
    }

    @Override
    public PaginationResponse<BookResponse> getBookIsSoon(int pageNumber, int pageSize) {
        List<BookDocument> list = bookDocRepo.findByIsSoon(true).stream()
                .sorted(Comparator.comparing(BookDocument::getPublicationDate).reversed())
                .toList();

        if (list.isEmpty()) {
            throw new NotFoundException("Soon document not found!");
        }
        return buildPaginationResponse(list, pageNumber, pageSize, 0L);
    }

    @Override
    public PaginationResponse<BookResponse> getBookIsNew(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        List<BookDocument> list = bookDocRepo.findByIsNew(true).stream()
                .filter(book -> !book.isSoon())
                .sorted(Comparator.comparing(BookDocument::getPublicationDate).reversed())
                .toList();
        if (list.isEmpty()) {
            throw new NotFoundException("New document not found!");
        }
        return buildPaginationResponse(list, pageNumber, pageSize, user.getId());
    }

    @Override
    public PaginationResponse<BookResponse> getBookIsBestseller(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        List<BookDocument> list = bookDocRepo.findByIsBestseller(true).stream()
                .filter(book -> !book.isSoon())
                .sorted(Comparator.comparing(BookDocument::getPublicationDate).reversed())
                .toList();
        if (list.isEmpty()) {
            throw new NotFoundException("Soon document not found!");
        }
        return buildPaginationResponse(list, pageNumber, pageSize, user.getId());
    }

    @Override
    public PaginationResponse<BookResponse> getBooksRecommendation(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        List<BookDocument> list = bookDocRepo.findByIsSoon(false).stream()
                .sorted(Comparator.comparingDouble(BookDocument::getAverageRating).reversed())
                .toList();

        if (list.isEmpty()) {
            throw new NotFoundException("recommendation document not found!");
        }

        return buildPaginationResponse(list, pageNumber, pageSize, user.getId());
    }

    @Override
    public PaginationResponse<BookResponse> getBookMostRead(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        List<BookDocument> list = bookDocRepo.findByIsSoon(false).stream()
                .sorted(Comparator.comparingInt(BookDocument::getListenerCount).reversed())
                .toList();

        if (list.isEmpty()) {
            throw new NotFoundException("recommendation document not found!");
        }

        return buildPaginationResponse(list, pageNumber, pageSize, user.getId());
    }

    @Override
    public PaginationResponse<BookResponse> etBookBySanat(int pageNumber, int pageSize) {
        User user = getCurrentUser();
        List<BookDocument> list = bookDocRepo.findByIsSanat(true).stream()
                .filter(doc -> !doc.isSoon())
                .sorted(Comparator.comparingInt(BookDocument::getListenerCount).reversed())
                .toList();
        if (list.isEmpty()) {
            throw new NotFoundException("recommendation document not found!");
        }
        return buildPaginationResponse(list, pageNumber, pageSize, user.getId());
    }

    @Override
    public List<BookDocument> getAllBookDoc(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return bookDocRepo.findAll(pageable).stream().toList();
    }

    @Override
    public BookResponseById findById(Long bookId) {
        User user = getCurrentUser();
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo
                .findById(book.getId()).orElse(null);
        if (bookDocument == null) {
            BookDocument bookDoc = BookMapper.toBookDocument(book);
            return getBookResponseById(bookDoc, user.getId());
        }
        return getBookResponseById(bookDocument, user.getId());
    }

    @Override
    public BookByIdForPlayerResponse findByIdPlayer(Long bookId) {
        User user = getCurrentUser();
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo
                .findById(book.getId()).orElse(null);
        if (bookDocument == null) {
            BookDocument bookDoc = BookMapper.toBookDocument(book);
            return toResponse(bookDoc, user.getId());
        }
        return toResponse(bookDocument, user.getId());
    }

    @Override
    public PaginationResponse<AllBookResponse> getAllBook(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        Iterable<BookDocument> all = bookDocRepo.findAll();

        List<BookDocument> bookList = StreamSupport
                .stream(all.spliterator(), false)
                .sorted(Comparator.comparing(BookDocument::getPublicationDate).reversed())
                .toList();

        int totalElements = bookList.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        List<AllBookResponse> allBookResponses = bookList.stream()
                .skip(offset)
                .limit(pageSize)
                .map(doc -> {
                    AllBookResponse response = new AllBookResponse();
                    response.setId(doc.getId());
                    response.setAuthors(doc.getAuthors());
                    response.setNew(doc.isNew());
                    response.setGenres(doc.getGenres());
                    response.setPublicationDate(doc.getPublicationDate());
                    response.setBestseller(doc.isBestseller());
                    response.setBookName(doc.getBookName());
                    response.setBannerUrl(doc.getBannerUrl());
                    response.setSanat(doc.isSanat());
                    response.setSoon(doc.isSoon());
                    return response;
                })
                .toList();
        return PaginationResponse.<AllBookResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(allBookResponses)
                .build();
    }

    @Override
    @Transactional
    public String updatedIsNew(Long bookId, boolean isNew) {
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findByIdOrElseThrow(bookId);
        book.setNew(isNew);
        bookRepo.save(book);
        bookDocument.setNew(book.isNew());
        bookDocRepo.save(bookDocument);
        return "success";
    }

    @Override
    @Transactional
    public String updatedIsSanat(Long bookId, boolean isSanat) {
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findByIdOrElseThrow(bookId);
        book.setNew(isSanat);
        bookRepo.save(book);
        bookDocument.setNew(book.isSanat());
        bookDocRepo.save(bookDocument);
        return "success";
    }

    @Override
    @Transactional
    public String updatedIsBestseller(Long bookId, boolean isBestseller) {
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findByIdOrElseThrow(bookId);
        book.setNew(isBestseller);
        bookRepo.save(book);
        bookDocument.setNew(book.isBestseller());
        bookDocRepo.save(bookDocument);
        return "success";
    }

    @Override
    @Transactional
    public String updatedIsSoon(Long bookId, boolean isSoon) {
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findByIdOrElseThrow(bookId);
        book.setNew(isSoon);
        bookRepo.save(book);
        bookDocument.setNew(book.isSoon());
        bookDocRepo.save(bookDocument);
        return "success";
    }

    private BookResponseById getBookResponseById(BookDocument bookDoc, Long userId) {
        return BookResponseById.builder()
                .id(bookDoc.getId())
                .bookName(bookDoc.getBookName())
                .description(bookDoc.getDescription())
                .rating(bookDoc.getAverageRating())
                .publisher(bookDoc.getPublisher())
                .bannerUrl(bookDoc.getBannerUrl())
                .pageCount(bookDoc.getPageCount())
                .publicationDate(bookDoc.getPublicationDate())
                .ratingCount(bookDoc.getRatingCount())
                .author(bookDoc.getAuthors())
                .isHistory(bookDoc.getUserIds() != null && bookDoc.getUserIds().contains(userId))
                .build();
    }

    private BookByIdForPlayerResponse toResponse(BookDocument bookDoc, Long userId) {
        return BookByIdForPlayerResponse.builder()
                .id(bookDoc.getId())
                .bookName(bookDoc.getBookName())
                .bannerUrl(bookDoc.getBannerUrl())
                .author(bookDoc.getAuthors())
                .isHistory(bookDoc.getUserIds() != null && bookDoc.getUserIds().contains(userId))
                .chapters(bookDoc.getChapters().stream().map(this::toResponse).toList())
                .build();
    }

    private ChapterResponse toResponse(BookChaptersDocument bookChaptersDocument) {
        return ChapterResponse.builder()
                .id(bookChaptersDocument.getId())
                .audioUrl(bookChaptersDocument.getAudioUrl())
                .chapterName(bookChaptersDocument.getChapterName())
                .chapterNumber(bookChaptersDocument.getChapterNumber())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByEmailOrElseThrow(email);
    }

    private PaginationResponse<BookResponse> buildPaginationResponse(
            List<BookDocument> documents,
            int pageNumber,
            int pageSize,
            Long currentUserId
    ) {
        int offset = (pageNumber - 1) * pageSize;
        int totalElements = documents.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        List<BookResponse> content = documents.stream()
                .skip(offset)
                .limit(pageSize)
                .map(doc -> {
                    BookResponse response = new BookResponse();
                    response.setId(doc.getId());
                    response.setBookName(doc.getBookName());
                    response.setBannerUrl(doc.getBannerUrl());
                    response.setRating(doc.getAverageRating());
                    response.setGenreName(doc.getGenres());
                    response.setAuthor(doc.getAuthors());
                    response.setPublicationDate(doc.getPublicationDate());
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
                .content(content)
                .build();
    }
}
