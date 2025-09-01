package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorsResponse;
import org.appitcompany.kuimakulak.dto.pagination.PageResponse;
import org.appitcompany.kuimakulak.entity.Contributor;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.jpaRepository.ContributorRepo;
import org.appitcompany.kuimakulak.mapper.ContributorMapper;
import org.appitcompany.kuimakulak.service.ContributorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {
    private final ContributorRepo contributorRepo;
    private final ContributorMapper contributorMapper;

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

    @Override
    public PageResponse<ContributorsResponse> getAllContributorsByRole(Pageable pageable, ContributorRole role) {
        Page<Contributor> page = contributorRepo.findByRole(role, pageable);
        return PageResponse.fromSpringPage(
                page.map(contributorMapper::toDto)
        );
    }

    @Override
    @Transactional
    public ContributorsResponse updateContributor(long contributorId, String newFullName) {
        Contributor contributor = contributorRepo.findById(contributorId).orElseThrow(() -> new RuntimeException("Contributor with id " + contributorId + " does not exist"));
        contributor.setFullName(newFullName);
        return contributorMapper.toDto(contributor);
    }

    @Override
    public void deleteContributor(long contributorId) {
        Contributor contributor = contributorRepo.findById(contributorId)
                .orElseThrow(() -> new NotFoundException("Contributor with id " + contributorId + " does not exist"));
        contributorRepo.delete(contributor);
    }

}
