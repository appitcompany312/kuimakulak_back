package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByGenreName(String genreName);
}
