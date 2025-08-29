package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.bookDto.ChapterRequest;
import org.appitcompany.kuimakulak.service.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @Secured("ADMIN")
    @Operation(summary = "save book chapter(\"ADMIN\")")
    @PostMapping("/save")
    public ResponseEntity<?> saveChapter(@Valid @RequestBody ChapterRequest chapterRequest,
                                         @RequestParam Long bookId){
        return chapterService.saveChapter(chapterRequest,bookId);
    }
}
