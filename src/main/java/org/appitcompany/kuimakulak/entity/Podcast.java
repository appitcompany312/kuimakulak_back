package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "podcasts")
public class Podcast {
    @Id
    @GeneratedValue(generator = "podcast_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "podcast_gen", sequenceName = "podcast_seq", allocationSize = 1, initialValue = 100)
    private Long podcastId;
    private String description;
    private String podcastName;
    private String audioUrl;
    private String bannerUrl;
    private LocalDate publicationDate;
    @ManyToOne
    private Channel channel;

    @OneToMany(mappedBy = "podcast")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "podcast")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "podcast")
    private  List<History> history;
}
