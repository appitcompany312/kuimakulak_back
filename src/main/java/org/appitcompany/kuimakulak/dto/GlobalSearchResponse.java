package org.appitcompany.kuimakulak.dto;

import lombok.*;
import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.dto.podcastDto.PodcastResponse;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSearchResponse {
    private List<BookResponse> books;
    private List<PodcastResponse> podcasts;
}
