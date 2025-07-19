package org.appitcompany.kuimakulak.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(indexName = "books")
@Getter
@Setter
public class BookDocument {
    @Id
    private Long id;
    private String bookName;
    private String bannerUrl;
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long publicationDate;
    private Integer pageCount;
    private String description;
    private String publisher;
    private boolean isSoon;
    private boolean isNew;
    private boolean isSanat;
    private boolean isBestseller;
    private List<String> genres = new ArrayList<>();
    private List<String> authors = new ArrayList<>();
    private List<String> translators = new ArrayList<>();
    private List<String> narrators = new ArrayList<>();
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Double averageRating;
    private Integer favoriteCount;
    private Integer listenerCount;
    private List<String> chapterNames = new ArrayList<>();
    private Set<Long> userIds  = new HashSet<>();
}
