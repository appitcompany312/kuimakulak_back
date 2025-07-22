package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.entity.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastRepo extends JpaRepository<Podcast, Long> {
    List<Podcast> findByPodcastName(String podcastName);
}
