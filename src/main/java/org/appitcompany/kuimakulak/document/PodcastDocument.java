package org.appitcompany.kuimakulak.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
@Document(indexName = "podcasts")
@Getter
@Setter
public class PodcastDocument {
    @Id
    private Long podcastId;
    private String podcastName;
    private String description;
    private String audioUrl;
    private String bannerUrl;
    private LocalDate publicationDate;
    private String channelName;
    private Double averageRating;
    private Integer favoriteCount;
}
