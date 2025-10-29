package org.appitcompany.kuimakulak.service;

import jakarta.validation.Valid;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface PodcastService {
    ResponseEntity<?> save(PodcastRequest podcastRequest);

    List<PodcastDocument> getAllPodcastDoc(int pageNumber, int pageSize);

    PaginationResponse<PodcastResponse> getPodcastsByChannelName(int pageNumber, int pageSize, String channelName);

    PodcastResponseById findById(Long podcastId);

    PaginationResponse<AllPodcastResponse> getAllPodcast(int pageNumber, int pageSize);

    ResponseEntity<?> deleted(Long podcastId);

    ResponseEntity<?> updated(@Valid UpdatedPodcastRequest updatedRequest, Long podcastId);

    Map<String, List<PodcastResponse>> getPodcastCategory();
}

