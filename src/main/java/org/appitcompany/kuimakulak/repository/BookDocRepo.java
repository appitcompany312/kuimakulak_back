package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;


@EnableElasticsearchRepositories
public interface BookDocRepo extends ElasticsearchRepository<BookDocument,Long> {
    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["bookName", "description", "authors", "genres", "translators", "narrators", "chapterNames", "publisher"]
            }
          }
        ],
        "must_not": [
          {
            "term": {
              "isSoon": true
            }
          }
        ]
      }
    }
    """)
    Page<BookDocument> globalSearch(String keyword, Pageable pageable);

    List<BookDocument> findByGenres(List<String> genres);

    List<BookDocument> findByIsBestseller(boolean bestseller);

    List<BookDocument> findByIsNew(boolean aNew);


    List<BookDocument> findByIsSoon(boolean b);

    List<BookDocument> findByIsSanat(boolean b);
}
