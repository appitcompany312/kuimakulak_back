package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.BookDocRepo;
import org.appitcompany.kuimakulak.repository.PodcastDocRepo;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.appitcompany.kuimakulak.service.GlobalSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl implements GlobalSearchService {
    private final BookDocRepo bookDocRepo;
    private final PodcastDocRepo podcastDocRepo;
    private final UserRepository userRepository;
    @Override
    public PaginationResponse<GlobalSearchResponse> globalSearch(String keyword, int pageSize, int pageNumber) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new NotFoundException("User not found"));
        int size =(int) pageSize/2;
        if (size==0){size=1;}
        Pageable pageable = PageRequest.of(pageNumber - 1, size);

        Page<BookDocument> bookPage = bookDocRepo.globalSearch(keyword, pageable);
        Page<PodcastDocument> podcastPage = podcastDocRepo.globalSearch(keyword, pageable);

        List<BookResponse> books = bookPage.getContent().stream()
                .map(bookDocument -> mapToBookResponse(bookDocument, user.getId()))
                .toList();
        List<PodcastResponse> podcasts = podcastPage.getContent().stream()
                .map(podcastDocument -> mapToPodcastResponse(podcastDocument, user.getId()))
                .toList();

        long totalBookHits = bookPage.getTotalElements();
        long totalPodcastHits = podcastPage.getTotalElements();
        long totalElements = totalBookHits + totalPodcastHits;
        long totalPages = (long) Math.ceil((double) totalElements / pageSize);


        GlobalSearchResponse globalSearchResponse = new GlobalSearchResponse();
        globalSearchResponse.setBooks(books);
        globalSearchResponse.setPodcasts(podcasts);

        return PaginationResponse.<GlobalSearchResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(List.of(globalSearchResponse))
                .build();
    }


    private BookResponse mapToBookResponse(BookDocument doc,Long currentUserId) {
        return BookResponse.builder()
                .id(doc.getId())
                .bookName(doc.getBookName())
                .bannerUrl(doc.getBannerUrl())
                .genreName(doc.getGenres())
                .author(doc.getAuthors())
                .rating(doc.getAverageRating())
                .publicationDate(doc.getPublicationDate())
                .isHistory(doc.getUserIds() != null && doc.getUserIds().contains(currentUserId))
                .build();
    }

    private PodcastResponse mapToPodcastResponse(PodcastDocument doc, Long currentUserId) {
        PodcastResponse response = new PodcastResponse();
        response.setId(doc.getId());
        response.setPodcastName(doc.getPodcastName());
        response.setAudioUrl(doc.getAudioUrl());
        response.setBannerUrl(doc.getBannerUrl());
        response.setChannelName(doc.getChannelName());
        response.setChannelAuthor(doc.getChannelAuthor());
        response.setHistory(doc.getUserIds() != null && doc.getUserIds().contains(currentUserId));
        return response;
    }
}
