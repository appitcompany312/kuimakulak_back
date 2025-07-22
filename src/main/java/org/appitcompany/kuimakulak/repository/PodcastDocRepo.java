package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.document.PodcastDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface PodcastDocRepo extends ElasticsearchRepository<PodcastDocument,Long> {
    @Query("""
    {
      "multi_match": {
        "query": "?0",
        "fields": ["podcastName", "description", "channelName", "channelAuthor"]
      }
    }
    """)
    Page<PodcastDocument> globalSearch(String keyword, Pageable pageable);
    List<PodcastDocument> findByChannelName(String channelName);
}
