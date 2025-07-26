package org.appitcompany.kuimakulak.dto.podcastDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PodcastResponseById {
    private Long id;
    private String podcastName;
    private String audioUrl;
    private String bannerUrl;
    private String channelName;
    private String channelAuthor;
    private boolean isHistory;
    private String description;
    private LocalDate publicationDate;
    private Double averageRating;
}
