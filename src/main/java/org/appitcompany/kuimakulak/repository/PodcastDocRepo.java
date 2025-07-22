package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface PodcastDocRepo extends ElasticsearchRepository<PodcastDocument,Long> {
    List<PodcastDocument> findByChannelName(String channelName);
}
