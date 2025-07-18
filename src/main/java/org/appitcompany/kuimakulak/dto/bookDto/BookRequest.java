package org.appitcompany.kuimakulak.dto.bookDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class BookRequest {
    @NotEmpty(message = "the bookName field must not be empty")
    private String bookName;
    @NotEmpty(message = "the bannerUrl field must not be empty")
    @Size(max = 1000 , message = "max 100 characters ")
    private String bannerUrl;
    private Integer pageCount;
    private String description;
    private String publisher;
    private boolean isSoon;
    private boolean isNew;
    private boolean isSanat;
    private boolean isBestseller;
    private Set<String> genreName= new HashSet<>();
    @NotEmpty(message = "the authorName field must not be empty")
    private Set<String> authorName = new HashSet<>();
    @NotEmpty(message = "the publisher field must not be empty")
    private Set<String> translatorName= new HashSet<>();
    private Set<String> narratorName= new HashSet<>();
    @NotEmpty(message = "the chapterName field must not be empty")
    private String chapterName;
    @NotEmpty(message = "the chapterNumber field must not be empty")
    private String chapterNumber;
    @NotEmpty(message = "the audioUrl field must not be empty")
    @Size(max = 500 , message = "max 500 characters ")
    private String audioUrl;
}
