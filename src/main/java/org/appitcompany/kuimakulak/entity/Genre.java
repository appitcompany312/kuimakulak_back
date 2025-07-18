package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(generator = "genre_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "genre_gen", sequenceName = "genre_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private String genreName;
    private LocalDate addedDate;

    @ManyToMany
    @JoinTable(
            name = "genre_books",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books  = new ArrayList<>();
}
