package org.appitcompany.kuimakulak.dto.bookDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookFilterRequest {
    String author;
    String genre;
}
