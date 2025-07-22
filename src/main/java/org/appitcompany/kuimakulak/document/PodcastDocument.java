package org.appitcompany.kuimakulak.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Document(indexName = "podcasts")
@Getter
@Setter
public class PodcastDocument {
    @Id
    private Long id;
    private String podcastName;
    private String description;
    private String audioUrl;
    private String bannerUrl;
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long publicationDate;
    private String channelName;
    private String channelAuthor;
    private Double averageRating;
    private Set<Long> userIds  = new HashSet<>();
}
