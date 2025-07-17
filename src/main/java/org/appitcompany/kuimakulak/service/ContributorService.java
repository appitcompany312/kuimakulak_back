package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.contributorDto.ContributorRequest;
import org.springframework.http.ResponseEntity;

public interface ContributorService {
    ResponseEntity<?> saveContributor(ContributorRequest contributorRequest);
}
