package org.appitcompany.kuimakulak.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "books")
@Getter
@Setter
public class BookDocument {
    @Id
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
    private List<String> genres;
    private List<String> contributors;
    private Double averageRating;
    private Integer favoriteCount;
    private Integer listenerCount;
    private List<String> chapterNames;

}
