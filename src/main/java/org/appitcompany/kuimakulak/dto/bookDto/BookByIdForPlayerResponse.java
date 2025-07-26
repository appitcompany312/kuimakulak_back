package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookByIdForPlayerResponse {
    private Long id;
    private String bookName;
    private String bannerUrl;
    private List<String> author;
    private boolean isHistory;
    private List<ChapterResponse> chapters;
}
