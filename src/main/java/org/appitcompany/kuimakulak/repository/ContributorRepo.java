package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContributorRepo extends JpaRepository<Contributor,Long> {
    @Query("SELECT c FROM Contributor c WHERE c.fullName = :name")
    Contributor findByName(@Param("name") String name);
}
