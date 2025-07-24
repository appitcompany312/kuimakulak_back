package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterResponse {
    private Long id;
    private String chapterName;
    private String chapterNumber;
    private String audioUrl;
}
