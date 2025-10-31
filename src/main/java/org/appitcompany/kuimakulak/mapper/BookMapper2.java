package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.dto.bookDto.BookResponse;
import org.appitcompany.kuimakulak.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper2 {
    BookResponse toDTO(Book book);
}
