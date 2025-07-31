package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.entity.Contributor;
import org.appitcompany.kuimakulak.jpaRepository.ContributorRepo;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {
    private final ContributorRepo contributorRepo;
    @Override
    public ResponseEntity<?> saveContributor(ContributorRequest contributorRequest) {
        List<Contributor> existingContributors = contributorRepo.findByFullName(contributorRequest.getFullName());

        if (existingContributors.isEmpty()) {
            Contributor newContributor = new Contributor();
            newContributor.setFullName(contributorRequest.getFullName());
            newContributor.setRole(contributorRequest.getRole());
            contributorRepo.save(newContributor);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Contributor has been saved successfully");
        }
        boolean sameRoleExists = existingContributors.stream()
                .anyMatch(contributor -> contributor.getRole().equals(contributorRequest.getRole()));

        if (sameRoleExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Contributor with same full name and role already exists");
        }
        Contributor newContributor = new Contributor();
        newContributor.setFullName(contributorRequest.getFullName());
        newContributor.setRole(contributorRequest.getRole());
        contributorRepo.save(newContributor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Contributor has been saved successfully");
    }

}
