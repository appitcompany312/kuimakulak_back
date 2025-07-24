package org.appitcompany.kuimakulak.service;

import org.springframework.http.ResponseEntity;

public interface RatingService {
    ResponseEntity<?> addRatingBook(int rating, Long bookId);

    ResponseEntity<?> addRatingPodcast(int rating, Long podcastId);
}
