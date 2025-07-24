package org.appitcompany.kuimakulak.service;

import jakarta.validation.Valid;
import org.appitcompany.kuimakulak.dto.bookDto.ChapterRequest;
import org.springframework.http.ResponseEntity;

public interface ChapterService {
    ResponseEntity<?> saveChapter(@Valid ChapterRequest chapterRequest, Long bookId);
}
