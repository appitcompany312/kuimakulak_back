package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.entity.*;

public class BookMapper {
    public static BookDocument toBookDocument(Book book) {
        BookDocument bookDoc = new BookDocument();
        bookDoc.setBookName(book.getBookName());
        bookDoc.setBestseller(book.isBestseller());
        bookDoc.setNew(book.isNew());
        bookDoc.setSanat(book.isSanat());
        bookDoc.setPageCount(book.getPageCount());
        bookDoc.setPublicationDate(book.getPublicationDate());
        bookDoc.setBookId(book.getId());
        bookDoc.setBannerUrl(book.getBannerUrl());
        bookDoc.setBookName(book.getBookName());
        bookDoc.setDescription(book.getDescription());
        bookDoc.setPublisher(book.getPublisher());

        bookDoc.setListenerCount(book.getListeners().getCountListeners());

        bookDoc.setGenres(book.getGenres().stream()
                .map(Genre::getGenreName).toList());

        bookDoc.setContributors(book.getContributors().stream()
                .map(Contributor::getFullName).toList());

        bookDoc.setChapterNames(book.getChapters().stream()
                .map(BookChapters::getChapterName).toList());

        bookDoc.setFavoriteCount(book.getFavorites().size());

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
