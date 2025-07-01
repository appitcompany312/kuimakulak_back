package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.*;
import org.appitcompany.kuimakulak.enums.ContributorRole;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contributors")
public class Contributor {
    @Id
    @GeneratedValue(generator = "contributor_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "contributor_gen", sequenceName = "contributor_seq", allocationSize = 1, initialValue = 100)
    private Long id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @ManyToMany(mappedBy = "contributors")
    private Set<Book> audios;
}
