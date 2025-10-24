package org.appitcompany.kuimakulak.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.appitcompany.kuimakulak.dto.contributorDto.ContributorsResponse;
import org.appitcompany.kuimakulak.dto.pagination.PageResponse;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.appitcompany.kuimakulak.enums.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContributorService {
    ResponseEntity<?> saveContributor(ContributorRequest contributorRequest);

    PageResponse<ContributorsResponse> getAllContributorsByRole(Pageable pageable, ContributorRole role);

    ContributorsResponse updateContributor(long contributorId, String newFullName);

    void deleteContributor(@Min(1) long contributorId);

    List<String> getAllAuthors();

}
