package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllBookResponse {
    private Long id;
    private String bookName;
    private String bannerUrl;
    private LocalDate publicationDate;
    private boolean isSoon;
    private boolean isNew;
    private boolean isSanat;
    private boolean isBestseller;
    private List<String> genres = new ArrayList<>();
    private List<String> authors = new ArrayList<>();
}
