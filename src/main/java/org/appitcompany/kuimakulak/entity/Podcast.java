package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private Long id;
    private String description;
    private String podcastName;
    private String audioUrl;
    private String bannerUrl;
    private LocalDate publicationDate;
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "podcast")
    private List<Rating> ratings  = new ArrayList<>();

    @OneToMany(mappedBy = "podcast")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "podcast")
    private  List<History> history  = new ArrayList<>();
}
