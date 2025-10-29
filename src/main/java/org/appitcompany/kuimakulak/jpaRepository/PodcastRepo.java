package org.appitcompany.kuimakulak.jpaRepository;

import jakarta.validation.constraints.NotEmpty;
import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PodcastRepo extends JpaRepository<Podcast, Long> {

    @Query("SELECT p FROM Podcast p JOIN FETCH p.channel")
    List<Podcast> findAllWithChannel();
    List<Podcast> findByPodcastName(String podcastName);
    default Podcast findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new NotFoundException("Podcast with id " + id + " not found"));
    }

    List<Podcast> findByChannel(Channel channel);

    boolean existsByPodcastNameAndIdNot(@NotEmpty(message = "the podcastName field must not be empty") String podcastName, Long podcastId);
}
