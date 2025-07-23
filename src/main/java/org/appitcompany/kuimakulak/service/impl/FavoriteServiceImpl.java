package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Favorite;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.BookDocRepo;
import org.appitcompany.kuimakulak.repository.BookRepo;
import org.appitcompany.kuimakulak.repository.FavoriteRepo;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.appitcompany.kuimakulak.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final BookRepo bookRepo;
    private final BookDocRepo bookDocRepo;
    private final UserRepository userRepo;
    private final FavoriteRepo favoriteRepo;
    @Override
    @Transactional
    public ResponseEntity<?> addFavorite(Long bookId) {
        User user = getUser();
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        BookDocument bookDocument = bookDocRepo
                .findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Book document not found"));
        Favorite favorite = user.getFavorite();
        if (favorite == null) {
            favorite = new Favorite();
            favorite.setUser(user);
            user.setFavorite(favorite);
        }
        System.err.println("favorite.getBooks().contains(book) = " + favorite.getBooks().contains(book));
        if (favorite.getBooks().contains(book)) {
            favorite.getBooks().remove(book);
            Favorite save1 = favoriteRepo.save(favorite);
            book.getFavorites().add(save1);
            Book save = bookRepo.save(book);
            System.err.println("book.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()) = " + book.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()));
            bookDocument.setUserIds(save.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()));
            bookDocRepo.save(bookDocument);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("book deleted to  favorites");
        }
        favorite.getBooks().add(0,book);
        Favorite save1 = favoriteRepo.save(favorite);
        book.getFavorites().add(save1);
        Book save = bookRepo.save(book);
        System.err.println("book.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()) = " + book.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()));
        bookDocument.setUserIds(save.getFavorites().stream().map(Favorite::getUser).map(User::getId).collect(Collectors.toSet()));
        bookDocRepo.save(bookDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("book added to  favorites successfully");
    }
    private User getUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
