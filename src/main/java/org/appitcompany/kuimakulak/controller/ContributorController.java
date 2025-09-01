package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorsResponse;
import org.appitcompany.kuimakulak.dto.pagination.PageResponse;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contributor")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "contributors-controller",
        description = "CRUD операции для управления авторами, переводчиками и озвучщиками. " +
                "Доступ к методам удаления и обновления ограничен только для ADMIN."
)
public class ContributorController {
    private final ContributorService contributorService;
       @Secured("ADMIN")
    @PostMapping("/save")
    public ResponseEntity<?> saveContributor(@Valid @RequestBody ContributorRequest contributorRequest) {
        return contributorService.saveContributor(contributorRequest);
    }
    /**
     * Получить список участников с пагинацией и фильтрацией
     *
     * @param page номер страницы (0-based)
     * @param size размер страницы
     */
    @GetMapping("/getAll")
    @Secured("ADMIN")
    public PageResponse<ContributorsResponse> getAllContributor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam ContributorRole role
            ) {
        Pageable pageable = PageRequest.of(page, size);
        return contributorService.getAllContributorsByRole(pageable, role);
    }

    @PutMapping("/update")
    @Secured("ADMIN")
    public ResponseEntity<?> updateContributor(@RequestParam @Min(1) long contributorId,
                                               @RequestParam @NotBlank(message = "Это поле не может быть пустым или содержать только пробелы")  String newFullName) {
        ContributorsResponse contributorsResponse = contributorService.updateContributor(contributorId, newFullName);
        return ResponseEntity.ok(contributorsResponse);
    }

    @DeleteMapping("/delete/{contributorId}")
    @Secured("ADMIN")
    public ResponseEntity<?> deleteContributor(@PathVariable @Min(1) long contributorId) {
        contributorService.deleteContributor(contributorId);
        return ResponseEntity.ok("Contributor deleted");
    }
}
