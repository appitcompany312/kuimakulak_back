package org.appitcompany.kuimakulak.controller;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contributor")
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService contributorService;
    public ResponseEntity<?> saveContributor(ContributorRequest contributorRequest) {
        return contributorService.saveContributor(contributorRequest);
    }
}
