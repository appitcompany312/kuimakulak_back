package org.appitcompany.kuimakulak.dto.genreDto;

public record GenreResponse(
        String genreName,
        Long bookId,
        String bookName,
        String bookImageUrl

        ) {
}
