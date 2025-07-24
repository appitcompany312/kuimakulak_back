package org.appitcompany.kuimakulak.dto.bookDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChapterRequest {
    @NotEmpty(message = "the chapterName field must not be empty")
    private String chapterName;
    @NotEmpty(message = "the chapterNumber field must not be empty")
    private String chapterNumber;
    @NotEmpty(message = "the audioUrl field must not be empty")
    @Size(max = 500 , message = "max 500 characters ")
    private String audioUrl;
}
