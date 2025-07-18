package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(mappedBy = "books")
    private Set<Contributor> contributors = new HashSet<>();

    @OneToMany(mappedBy = "book")
    private List<Rating> ratings  = new ArrayList<>();

    @ManyToMany
    private List<User> users  = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Favorite> favorites   = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<History> historys   = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookChapters> chapters   = new ArrayList<>();

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Listeners listeners;

}
