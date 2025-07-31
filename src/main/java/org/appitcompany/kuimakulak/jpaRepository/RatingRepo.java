package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.entity.Rating;
import org.appitcompany.kuimakulak.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
    List<Rating> findByUserAndBook(User user, Book book);

    List<Rating> findByUserAndPodcast(User user, Podcast podcast);
}
