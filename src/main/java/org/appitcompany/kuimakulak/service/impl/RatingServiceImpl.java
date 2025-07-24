package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.entity.Rating;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.mapper.BookMapper;
import org.appitcompany.kuimakulak.mapper.PodcastMapper;
import org.appitcompany.kuimakulak.repository.*;
import org.appitcompany.kuimakulak.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final UserRepository userRepository;
    private final BookRepo bookRepo;
    private final BookDocRepo bookDocRepo;
    private final PodcastRepo podcastRepo;
    private final PodcastDocRepo podcastDocRepo;
    private final RatingRepo ratingRepo;

    @Override
    @Transactional
    public ResponseEntity<?> addRatingBook(int rating, Long bookId) {
        User user = getCurrentUser();
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findById(bookId)
                .orElse(null);
        if (bookDocument == null) {
            bookDocument= BookMapper.toBookDocument(book);
        }

        Rating rating1 = ratingRepo.findByUserAndBook(user, book)
                .stream()
                .findFirst()
                .orElse(null);
        if (rating1 == null) {
             rating1 = new Rating();
            rating1.setUser(user);
            rating1.setBook(book);
            rating1.setRating(rating);
             ratingRepo.save(rating1);
            book.getRatings().add(rating1);
            bookRepo.save(book);
            double avg = book.getRatings().stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            bookDocument.setAverageRating(avg);
            bookDocRepo.save(bookDocument);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("rating has been added");
        }
        book.getRatings().remove(rating1);
        ratingRepo.delete(rating1);
        Rating rating2 = new Rating();
        rating2.setUser(user);
        rating2.setBook(book);
        rating2.setRating(rating);
        ratingRepo.save(rating2);
        book.getRatings().add(rating2);
        bookRepo.save(book);
        double avg = book.getRatings().stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
        bookDocument.setAverageRating(avg);
        bookDocRepo.save(bookDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("rating has been updated");
    }

    @Override
    public ResponseEntity<?> addRatingPodcast(int rating, Long podcastId) {
        User user = getCurrentUser();
        Podcast podcast = podcastRepo.findByIdOrElseThrow(podcastId);
        PodcastDocument podcastDocument = podcastDocRepo.findById(podcastId)
                .orElse(null);
        if (podcastDocument == null) {
            podcastDocument= PodcastMapper.toPodcastDocument(podcast);
        }
        Rating rating1 = ratingRepo.findByUserAndPodcast(user, podcast)
                .stream()
                .findFirst()
                .orElse(null);
        if (rating1 == null) {
            rating1 = new Rating();
            rating1.setUser(user);
            rating1.setPodcast(podcast);
            rating1.setRating(rating);
            ratingRepo.save(rating1);
            podcast.getRatings().add(rating1);
            podcastRepo.save(podcast);
            double avg = podcast.getRatings().stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            podcastDocument.setAverageRating(avg);
            podcastDocRepo.save(podcastDocument);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("rating has been added");
        }
        podcast.getRatings().remove(rating1);
        ratingRepo.delete(rating1);
        Rating rating2 = new Rating();
        rating2.setUser(user);
        rating2.setPodcast(podcast);
        rating2.setRating(rating);
        ratingRepo.save(rating2);
        podcast.getRatings().add(rating2);
        podcastRepo.save(podcast);
        double avg = podcast.getRatings().stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
        podcastDocument.setAverageRating(avg);
        podcastDocRepo.save(podcastDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("rating has been updated");
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByEmailOrElseThrow(email);
    }
}
