package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.genreDto.GenreResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;
import org.appitcompany.kuimakulak.service.BookService;
import org.appitcompany.kuimakulak.service.PodcastService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final BookService bookService;
    private final PodcastService podcastService;

    @GetMapping("/by-genres")
    @Operation(summary = "Get list of books by genres", description = "Getting list of books by genres using single endpoint")
    public Map<String, List<GenreResponse>> getCategoryByGenre(){
        return bookService.getBooksByGenres();
    }
    @GetMapping("/podcasts")
    @Operation(summary = "Get list of podcasts", description = "Getting list of podcasts by channel using single endpoint")
    public Map<String, List<PodcastResponse>> getCategoryByPodcast(){
        return podcastService.getPodcastCategory();
    }



}
