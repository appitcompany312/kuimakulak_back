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
        BookDocument bookDocument = bookDocRepo.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Book document not found"));


        Favorite favorite = user.getFavorite();
        if (favorite == null) {
            favorite = new Favorite();
            favorite.setUser(user);
            user.setFavorite(favorite);
        }


        if (favorite.getBooks().contains(book)) {

            favorite.getBooks().remove(book);
            book.getFavorites().remove(favorite);
            favoriteRepo.save(favorite);
            bookRepo.save(book);

            bookDocument.setUserIds(book.getFavorites().stream()
                    .map(Favorite::getUser)
                    .map(User::getId)
                    .collect(Collectors.toSet()));
            bookDocRepo.save(bookDocument);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Book removed from favorites successfully");
        } else {
            favorite.getBooks().add(book);
            book.getFavorites().add(favorite);
            favoriteRepo.save(favorite);
            bookRepo.save(book);


            bookDocument.setUserIds(book.getFavorites().stream()
                    .map(Favorite::getUser)
                    .map(User::getId)
                    .collect(Collectors.toSet()));
            bookDocRepo.save(bookDocument);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Book added to favorites successfully");
        }
    }

    private User getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
