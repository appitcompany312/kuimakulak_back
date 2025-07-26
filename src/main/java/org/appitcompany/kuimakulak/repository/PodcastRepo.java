package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Channel;
import org.appitcompany.kuimakulak.entity.Podcast;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastRepo extends JpaRepository<Podcast, Long> {
    List<Podcast> findByPodcastName(String podcastName);
    default Podcast findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new NotFoundException("Podcast with id " + id + " not found"));
    }

    List<Podcast> findByChannel(Channel channel);
}
