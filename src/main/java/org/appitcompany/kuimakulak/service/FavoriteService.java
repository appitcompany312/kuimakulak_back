package org.appitcompany.kuimakulak.service;


import org.springframework.http.ResponseEntity;

public interface FavoriteService {
    ResponseEntity<?> addFavorite(Long bookId);

    ResponseEntity<?> addPodcastFavorite(Long podcastId);

}
