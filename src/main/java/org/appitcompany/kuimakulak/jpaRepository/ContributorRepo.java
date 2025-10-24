package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Contributor;
import org.appitcompany.kuimakulak.enums.ContributorRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContributorRepo extends JpaRepository<Contributor, Long> {

    List<Contributor> findByFullName(String fullName);

    @Query("SELECT c FROM Contributor c WHERE c.fullName = :translator AND c.role=:contributorRole")
    Contributor findByFullNameAndRole(@Param("translator") String translator, @Param("contributorRole") ContributorRole contributorRole);

    Page<Contributor> findByRole(ContributorRole role, Pageable pageable);

    List<Contributor> getByRole(ContributorRole role);
}


