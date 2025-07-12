package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historys")
public class History {

    @Id
    @GeneratedValue(generator = "history_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "history_gen", sequenceName = "history_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "podcast_id")
    private Podcast podcast;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    private User user;
}
