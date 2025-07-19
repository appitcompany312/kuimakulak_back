package org.appitcompany.kuimakulak.repository;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;


@EnableElasticsearchRepositories
public interface BookDocRepo extends ElasticsearchRepository<BookDocument,Long> {

    List<BookDocument> findByGenres(List<String> genres);

    List<BookDocument> findByIsBestseller(boolean bestseller);

    List<BookDocument> findByIsNew(boolean aNew);


    List<BookDocument> findByIsSoon(boolean b);
}
