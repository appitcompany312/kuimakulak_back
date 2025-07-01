package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "audio_id"})})
public class Rating {
    @Id
    @GeneratedValue(generator = "rating_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "rating_gen", sequenceName = "rating_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private Integer rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book audio;

    @ManyToOne
    private Podcast podcast;
}
