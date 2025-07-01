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
@Table(name = "listeners")
public class Listeners {
    @Id
    @GeneratedValue(generator = "listeners_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "listeners_gen", sequenceName = "listeners_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private int countListeners;

    @OneToOne
    private Book book;

    @OneToMany
    private List<User> users;
}
