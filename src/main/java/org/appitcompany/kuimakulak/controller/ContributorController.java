package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contributor")
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService contributorService;
       @Secured("ADMIN")
    @Operation(summary = "save  contributor(\"ADMIN\")",description = "only admins can add  contributors")
    @PostMapping("/save")
    public ResponseEntity<?> saveContributor(@Valid @RequestBody ContributorRequest contributorRequest) {
        return contributorService.saveContributor(contributorRequest);
    }
}
