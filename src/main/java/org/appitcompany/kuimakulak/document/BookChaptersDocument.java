package org.appitcompany.kuimakulak.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "book_chapters")
@Getter
@Setter
public class BookChaptersDocument {
    @Id
    private Long id;
    private String chapterName;
    private String chapterNumber;
    private String audioUrl;
}
