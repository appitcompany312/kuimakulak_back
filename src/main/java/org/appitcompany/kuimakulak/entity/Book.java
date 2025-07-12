package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(generator = "book_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "book_gen", sequenceName = "book_seq", allocationSize = 1, initialValue = 100)

    private Long id;
    private String bookName;
    private String bannerUrl;
    private LocalDate publicationDate;
    private Integer pageCount;
    private String description;
    private String publisher;
    private boolean isSoon;
    private boolean isNew;
    private boolean isSanat;
    private boolean isBestseller;

    @ManyToMany(mappedBy = "books")
    private List<Genre> genres;

    @ManyToMany(mappedBy = "books")
    private Set<Contributor> contributors;

    @OneToMany(mappedBy = "book")
    private List<Rating> ratings;

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "book")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "book")
    private List<History> historys;

    @OneToMany(mappedBy = "book")
    private List<BookChapters> chapters;

    @OneToOne(mappedBy = "book")
    private Listeners listeners;

}
