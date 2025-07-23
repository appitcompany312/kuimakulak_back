package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite")
public class Favorite {
    @Id
    @GeneratedValue(generator = "favorite_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "favorite_gen", sequenceName = "favorite_seq", allocationSize = 1, initialValue = 100)
    private Long id;

    @ManyToMany
    private List<Podcast> podcasts = new ArrayList<>();

    @ManyToMany
    private List<Book> books  = new ArrayList<>();

    @OneToOne
    private User user;
}
