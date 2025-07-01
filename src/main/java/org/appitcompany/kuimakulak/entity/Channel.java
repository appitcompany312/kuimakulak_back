package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channel")
public class Channel {
    @Id
    @GeneratedValue(generator = "channel_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "channel_gen", sequenceName = "channel_seq", allocationSize = 1, initialValue = 100)
    private Long channelId;
    private String channelName;
    private String channelAuthor;
    @OneToMany(mappedBy ="channel")
    private List<Podcast> podcasts;
}
