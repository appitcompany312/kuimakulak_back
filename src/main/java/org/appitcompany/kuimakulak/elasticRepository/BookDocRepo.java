package org.appitcompany.kuimakulak.elasticRepository;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookDocRepo extends ElasticsearchRepository<BookDocument,Long> {
    @Query("""
    {
      "bool": {
        "must": [
          {
            "query_string": {
              "query": "*?0*",
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
    @Query("""
{
  "bool": {
    "must": [
      {
        "bool": {
          "should": [
            {"match": {"bookName": "?0"}},
            {"match": {"description": "?0"}},
            {"match": {"authors": "?0"}},
            {"match": {"genres": "?0"}},
            {"match": {"translators": "?0"}},
            {"match": {"narrators": "?0"}},
            {"match": {"chapterNames": "?0"}},
            {"match": {"publisher": "?0"}},
            {"match": {"rating": "?0"}}
          ]
        }
      },
      {
        "term": {"isSoon": false}
      },
      {
        "bool": {
          "must": [
            {{#if ?1}}{"match": {"authors": "?1"}},{{/if}}
            {{#if ?2}}{"match": {"genres": "?2"}},{{/if}}
          ]
        }
      }
    ]
  }
}
""")
    Page<BookDocument> globalSearchV2(String keyword, String author, String genre, String chapter, Pageable pageable);

    List<BookDocument> findByIsBestseller(boolean bestseller);

    List<BookDocument> findByIsNew(boolean aNew);


    List<BookDocument> findByIsSoon(boolean b);

    List<BookDocument> findByIsSanat(boolean b);
    default BookDocument findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new NotFoundException("Book document with id " + id + " not found"));
    }


    Page<BookDocument> findByAuthorsAndGenres(List<String> authors, List<String> genres, Pageable pageable);

    Page<BookDocument> getByGenres(List<String> genres, Pageable pageable);

    Page<BookDocument> getByAuthors(List<String> authors, Pageable pageable);
}
