package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    @Secured("USER")
    @Operation(summary = "add rating ", description = "add and updated rating for user")
    @PostMapping("/addRatingBook")
    public ResponseEntity<?> addRatingBook(@RequestParam() int rating, @RequestParam Long bookId) {
        return ratingService.addRatingBook(rating,bookId);
    }
    @Secured("USER")
    @Operation(summary = "add rating ", description = "add and updated rating for user")
    @PostMapping("/addRatingPodcast")
    public ResponseEntity<?> addRatingPodcast(@RequestParam() int rating, @RequestParam Long podcastId) {
        return ratingService.addRatingPodcast(rating,podcastId);
    }
}
