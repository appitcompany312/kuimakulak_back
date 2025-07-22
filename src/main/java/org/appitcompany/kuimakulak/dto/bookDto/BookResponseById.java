package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseById {
    private Long id;
    private String bookName;
    private String bannerUrl;
    private List<String> author;
    private Double rating;
    private boolean isHistory;
    private LocalDate publicationDate;
    private Integer pageCount;
    private String description;
    private String publisher;
    private int ratingCount;
}
