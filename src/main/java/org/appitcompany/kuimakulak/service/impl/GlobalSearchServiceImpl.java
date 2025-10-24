package org.appitcompany.kuimakulak.service.impl;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.appitcompany.kuimakulak.dto.ClientRequest;
import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.elasticRepository.BookDocRepo;
import org.appitcompany.kuimakulak.elasticRepository.PodcastDocRepo;
import org.appitcompany.kuimakulak.jpaRepository.UserRepository;
import org.appitcompany.kuimakulak.service.GlobalSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl implements GlobalSearchService {

    private final BookDocRepo bookDocRepo;
    private final PodcastDocRepo podcastDocRepo;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<GlobalSearchResponse> globalSearch(String keyword, int pageSize, int pageNumber) {
        User user = getCurrentUser();
        int size = getHalfPageSize(pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, size);

        Page<BookDocument> bookPage = bookDocRepo.globalSearch(keyword, pageable);
        Page<PodcastDocument> podcastPage = podcastDocRepo.globalSearch(keyword, pageable);

        List<BookResponse> books = mapBooks(bookPage.getContent(), user.getId());
        List<PodcastResponse> podcasts = mapPodcasts(podcastPage.getContent(), user.getId());

        return buildPaginationResponse(pageNumber, pageSize, books, podcasts,
                bookPage.getTotalElements(), podcastPage.getTotalElements());
    }

    @Override
    public PaginationResponse<GlobalSearchResponse> globalSearchFilter(
            ClientRequest clientRequest, int pageSize, int pageNumber) {

        User user = getCurrentUser();
        int size = getHalfPageSize(pageSize);
        Pageable pageable = PageRequest.of(pageNumber - 1, size);

        String keyword = clientRequest.getKeyword();
        List<String> authors = clientRequest.getAuthor();
        List<String> genders = clientRequest.getGender();
        System.err.println(authors.isEmpty() && genders.isEmpty());

        if (authors.isEmpty() && genders.isEmpty()) {

            Page<BookDocument> bookPage = bookDocRepo.globalSearch(keyword, pageable);
            Page<PodcastDocument> podcastPage = podcastDocRepo.globalSearch(keyword, pageable);
            List<BookResponse> books = mapBooks(bookPage.getContent(), user.getId());
            List<PodcastResponse> podcasts = mapPodcasts(podcastPage.getContent(), user.getId());

            return buildPaginationResponse(pageNumber, pageSize, books, podcasts,
                    bookPage.getTotalElements(), podcastPage.getTotalElements());
        }

        if (keyword == null) {
            Page<BookDocument> books = bookDocRepo.findByAuthorsAndGenres(authors, genders, pageable);
            return buildBookOnlyResponse(books, pageNumber, pageSize, user);
        }
        if (authors.isEmpty()) {
            Page<BookDocument> books = bookDocRepo.getByGenres(genders, pageable);
            return buildBookOnlyResponse(books, pageNumber, pageSize, user);
        }
        if (genders.isEmpty()) {
            Page<BookDocument> books = bookDocRepo.getByAuthors(authors, pageable);
            return buildBookOnlyResponse(books, pageNumber, pageSize, user);
        }


        Page<BookDocument> books = bookDocRepo.findByAuthorsAndGenres(authors, genders, pageable);
        Page<PodcastDocument> podcasts = podcastDocRepo.globalSearch(keyword, pageable);

        List<BookResponse> bookResponses = mapBooks(books.getContent(), user.getId());
        List<PodcastResponse> podcastResponses = mapPodcasts(podcasts.getContent(), user.getId());

        return buildPaginationResponse(pageNumber, pageSize, bookResponses, podcastResponses,
                books.getTotalElements(), podcasts.getTotalElements());
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByEmailOrElseThrow(email);
    }

    private int getHalfPageSize(int pageSize) {
        int size = pageSize / 2;
        return size == 0 ? 1 : size;
    }

    private List<BookResponse> mapBooks(List<BookDocument> docs, Long userId) {
        return docs.stream().map(doc -> mapToBookResponse(doc, userId)).toList();
    }

    private List<PodcastResponse> mapPodcasts(List<PodcastDocument> docs, Long userId) {
        return docs.stream().map(doc -> mapToPodcastResponse(doc, userId)).toList();
    }

    private PaginationResponse<GlobalSearchResponse> buildBookOnlyResponse(
            Page<BookDocument> page, int pageNumber, int pageSize, User user) {

        List<BookResponse> books = mapBooks(page.getContent(), user.getId());
        GlobalSearchResponse response = new GlobalSearchResponse();
        response.setBooks(books);

        long total = page.getTotalElements();
        long totalPages = (long) Math.ceil((double) total / pageSize);

        return PaginationResponse.<GlobalSearchResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(total)
                .totalPages(totalPages)
                .content(List.of(response))
                .build();
    }

    private PaginationResponse<GlobalSearchResponse> buildPaginationResponse(
            int pageNumber, int pageSize,
            List<BookResponse> books, List<PodcastResponse> podcasts,
            long totalBooks, long totalPodcasts) {

        long totalElements = totalBooks + totalPodcasts;
        long totalPages = (long) Math.ceil((double) totalElements / pageSize);

        GlobalSearchResponse response = new GlobalSearchResponse();
        response.setBooks(books);
        response.setPodcasts(podcasts);

        return PaginationResponse.<GlobalSearchResponse>builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .content(List.of(response))
                .build();
    }

    private BookResponse mapToBookResponse(BookDocument doc, Long userId) {
        return BookResponse.builder()
                .id(doc.getId())
                .bookName(doc.getBookName())
                .bannerUrl(doc.getBannerUrl())
                .genreName(doc.getGenres())
                .author(doc.getAuthors())
                .rating(doc.getAverageRating())
                .publicationDate(doc.getPublicationDate())
                .isHistory(doc.getUserIds() != null && doc.getUserIds().contains(userId))
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
