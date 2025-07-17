package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.entity.Contributor;
import org.appitcompany.kuimakulak.repository.ContributorRepo;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {
    private final ContributorRepo contributorRepo;
    @Override
    public ResponseEntity<?> saveContributor(ContributorRequest contributorRequest) {
        Contributor contributor = new Contributor();
        contributor.setFullName(contributorRequest.getFullName());
        contributor.setRole(contributorRequest.getRole());
        contributorRepo.save(contributor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Contributor has been saved successfully");
    }
}
