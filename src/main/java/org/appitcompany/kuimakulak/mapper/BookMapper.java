package org.appitcompany.kuimakulak.mapper;


import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.entity.*;
import org.appitcompany.kuimakulak.enums.ContributorRole;

import java.time.ZoneId;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDocument toBookDocument(Book book) {
        BookDocument bookDoc = new BookDocument();
        bookDoc.setBookName(book.getBookName());
        bookDoc.setBestseller(book.isBestseller());
        bookDoc.setNew(book.isNew());
        bookDoc.setSanat(book.isSanat());
        bookDoc.setSoon(book.isSoon());
        bookDoc.setPageCount(book.getPageCount());
        bookDoc.setPublicationDate(book.getPublicationDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        bookDoc.setId(book.getId());
        bookDoc.setBannerUrl(book.getBannerUrl());
        bookDoc.setBookName(book.getBookName());
        bookDoc.setDescription(book.getDescription());
        bookDoc.setPublisher(book.getPublisher());
       bookDoc.setRatingCount(book.getRatings().size());

        bookDoc.setListenerCount(book.getListeners().getCountListeners());

        bookDoc.setGenres(book.getGenres().stream()
                .map(Genre::getGenreName).toList());

        bookDoc.setAuthors(
                book.getContributors()
                        .stream()
                        .filter(contributor -> contributor.getRole() == ContributorRole.AUTHOR)
                        .map(Contributor::getFullName)
                        .collect(Collectors.toList())
        );
        bookDoc.setTranslators(
                book.getContributors()
                        .stream()
                        .filter(contributor -> contributor.getRole() == ContributorRole.TRANSLATOR)
                        .map(Contributor::getFullName)
                        .collect(Collectors.toList())
        );
        bookDoc.setNarrators(
                book.getContributors()
                        .stream()
                        .filter(contributor -> contributor.getRole() == ContributorRole.NARRATOR)
                        .map(Contributor::getFullName)
                        .collect(Collectors.toList())
        );

        bookDoc.setChapterNames(book.getChapters().stream()
                .map(BookChapters::getChapterName).toList());

        bookDoc.setUserIds(book.getFavorites().stream().map(Favorite::getUser)
                        .map(User::getId)
        .collect(Collectors.toSet()));


        if (book.getRatings() != null && !book.getRatings().isEmpty()) {
            double avg = book.getRatings().stream()
                    .filter(r -> r.getRating() != null)
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
            bookDoc.setAverageRating(avg);
        } else {
            bookDoc.setAverageRating(0.0);
        }

        return bookDoc;
    }
}
