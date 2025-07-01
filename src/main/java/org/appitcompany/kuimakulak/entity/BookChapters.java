package org.appitcompany.kuimakulak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_chapters")
public class BookChapters {
    @Id
    @GeneratedValue(generator = "chapter_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "chapter_gen", sequenceName = "chapter_seq", allocationSize = 1, initialValue = 100)
    private Long chapterId;
    private String chapterName;
    private String chapterNumber;
    private String audioUrl;

    @ManyToOne
    private Book book;
}
