package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BookRequest {
    private String bookName;
    private String bannerUrl;
    private Integer pageCount;
    private String description;
    private String publisher;
    private boolean isSoon;
    private boolean isNew;
    private boolean isSanat;
    private boolean isBestseller;
    private List<String> genreName;
    private Set<String> contributors;
    private String chapterName;
    private String chapterNumber;
    private String audioUrl;
}
