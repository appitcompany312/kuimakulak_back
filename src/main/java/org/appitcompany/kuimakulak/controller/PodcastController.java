package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.*;
import org.appitcompany.kuimakulak.service.PodcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/podcast")
@RequiredArgsConstructor
public class PodcastController {
    private final PodcastService podcastService;

    @GetMapping("getAllPodcastDoc")
    @Operation(summary = "Get all podcasts", description = "Fetches all podcast documents from Elasticsearch")
    public List<PodcastDocument> getAllPodcastDoc(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        return podcastService.getAllPodcastDoc(pageNumber, pageSize);
    }

    @Secured("ADMIN")
    @GetMapping("getAllPodcast")
    @Operation(summary = "Get all podcasts")
    public PaginationResponse<AllPodcastResponse> getAllPodcast(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        return podcastService.getAllPodcast(pageNumber, pageSize);
    }

    @Secured("ADMIN")
    @Operation(summary = "save podcast", description = "only admins can add podcast")
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody PodcastRequest podcastRequest) {
        return podcastService.save(podcastRequest);
    }

    @Secured("ADMIN")
    @Operation(summary = "updated podcast", description = "only admins can updated podcast")
    @PostMapping("/updated")
    public ResponseEntity<?> updated(@Valid @RequestBody UpdatedPodcastRequest updatedRequest,
                                     @RequestParam Long podcastId) {
        return podcastService.updated(updatedRequest,podcastId);
    }

    @Secured("ADMIN")
    @Operation(summary = "deleted podcast", description = "only admins can deleted podcast")
    @PostMapping("/deleted")
    public ResponseEntity<?> deleted(@RequestParam Long podcastId) {
        return podcastService.deleted(podcastId);
    }

    @Secured("USER")
    @Operation(summary = "get podcasts by channel name", description = "only admins can add books")
    @GetMapping("/getPodcastsByChannelName")
    public PaginationResponse<PodcastResponse> getPodcastsByChannelName(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam String channelName) {
        return podcastService.getPodcastsByChannelName(pageNumber, pageSize, channelName);
    }

    @Secured("USER")
    @GetMapping("/findById")
    @Operation(summary = "find by id podcast", description = "only user can find by id")
    public PodcastResponseById findById(@RequestParam Long podcastId) {
        return podcastService.findById(podcastId);
    }
}
