package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Favorite;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.*;
import org.appitcompany.kuimakulak.service.FavoriteService;
import org.appitcompany.kuimakulak.service.UserService;
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
    private final PodcastRepo podcastRepo;
    private final PodcastDocRepo podcastDocRepo;
    @Override
    @Transactional
    public ResponseEntity<?> addFavorite(Long bookId) {

        User user = getUser();


        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
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

    @Override
    public ResponseEntity<?> addPodcastFavorite(Long podcastId) {
        User user = getUser();
        Podcast podcast = podcastRepo.findByIdOrElseThrow(podcastId);

        PodcastDocument bookDocument = podcastDocRepo.findById(podcast.getId())
                .orElseThrow(() -> new NotFoundException("Podcast document not found"));

        Favorite favorite = user.getFavorite();
        if (favorite == null) {
            favorite = new Favorite();
            favorite.setUser(user);
            user.setFavorite(favorite);
        }

        if (favorite.getPodcasts().contains(podcast)) {

            favorite.getPodcasts().remove(podcast);
            podcast.getFavorites().remove(favorite);
            favoriteRepo.save(favorite);
            podcastRepo.save(podcast);

            bookDocument.setUserIds(podcast.getFavorites().stream()
                    .map(Favorite::getUser)
                    .map(User::getId)
                    .collect(Collectors.toSet()));
            podcastDocRepo.save(bookDocument);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Podcast removed from favorites successfully");
        } else {
            favorite.getPodcasts().add(podcast);
            podcast.getFavorites().add(favorite);
            favoriteRepo.save(favorite);
            podcastRepo.save(podcast);


            bookDocument.setUserIds(podcast.getFavorites().stream()
                    .map(Favorite::getUser)
                    .map(User::getId)
                    .collect(Collectors.toSet()));
            podcastDocRepo.save(bookDocument);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Podcast added to favorites successfully");
        }
    }

    private User getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.getUserByEmailOrElseThrow(email);
    }
}
