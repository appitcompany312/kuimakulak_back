package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorRepo extends JpaRepository<Contributor,Long> {
    Contributor findByFullName(String name);
}
