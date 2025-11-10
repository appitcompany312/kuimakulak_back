package org.appitcompany.kuimakulak.dto.genreDto;

import java.time.LocalDate;

public record GenreResponse(
        Long id,
        String name,
        int bookCount,
        LocalDate addedDate
) {
}
