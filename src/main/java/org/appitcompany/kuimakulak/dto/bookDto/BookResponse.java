package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private List<String> genreName;
    private String bookName;
    private String bannerUrl;
    private List<String> author;
    private Double rating;
    private boolean isHistory;
    private LocalDate publicationDate;
}
