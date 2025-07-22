package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastRequest;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PodcastService {
    ResponseEntity<?> save(PodcastRequest podcastRequest);


    List<PodcastDocument> getAllPodcastDoc(int pageNumber, int pageSize);

    PaginationResponse<PodcastResponse> getPodcastsByChannelName(int pageNumber, int pageSize, String channelName);

}

