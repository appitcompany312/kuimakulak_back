package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
     private final FavoriteService favoriteService;
     @Secured("USER")
     @Operation(summary = "add book favorite and deleted ")
     @PostMapping("/addBookFavorite")
    public ResponseEntity<?> addBookFavorite(@RequestParam Long bookId) {
        return favoriteService.addFavorite(bookId);
     }
    @Secured("USER")
    @Operation(summary = "add book favorite and deleted ")
    @PostMapping("/addPodcastFavorite")
    public ResponseEntity<?> addPodcastFavorite(@RequestParam Long podcastId) {
        return favoriteService.addPodcastFavorite(podcastId);
    }
}
