package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.bookDto.BookRequest;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Contributor;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.BookRepo;
import org.appitcompany.kuimakulak.repository.ContributorRepo;
import org.appitcompany.kuimakulak.service.BookService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
 private final BookRepo bookRepo;
 private final ContributorRepo contributorRepo;

    @Override
    @Transactional
    public ResponseEntity<?> saveBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setBookName(bookRequest.getBookName());
        book.setDescription(bookRequest.getDescription());
        book.setBestseller(bookRequest.isBestseller());
        book.setBannerUrl(bookRequest.getBannerUrl());
        book.setNew(bookRequest.isNew());
        book.setSoon(bookRequest.isSoon());
        book.setSanat(bookRequest.isSanat());
        book.setPublisher(bookRequest.getPublisher());
        book.setPublicationDate(LocalDate.now());
        book.setPageCount(bookRequest.getPageCount());
        Book saveBook = bookRepo.save(book);
        for (String name : bookRequest.getContributors()) {
         Contributor contributor =contributorRepo.findByFullName(name);
         if(contributor==null){throw new NotFoundException("Contributor not found! Add contributor first!!!");
         }
         contributor.getBooks().add(saveBook);
        }
//        private List<String> genreName;
//        private Set<String> contributors;
//        private String chapterName;
//        private String chapterNumber;
//        private String audioUrl;

        return null;
    }
}
