package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepo extends JpaRepository<Genre, Long> {
   @Query("select g from Genre g where g.genreName= :name")
    Genre findByName(@Param("name") String name);
}
