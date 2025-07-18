package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookResponse {
    private Long id;
    private List<String> genreName;
    private String bookName;
    private String banner_url;
    private String author;
    private Double rating;
    private boolean isHistory;
    private LocalDate publicationDate;
}
