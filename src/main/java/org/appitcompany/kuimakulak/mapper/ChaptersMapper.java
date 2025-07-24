package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.document.BookChaptersDocument;
import org.appitcompany.kuimakulak.entity.BookChapters;

public class ChaptersMapper {
    public static BookChaptersDocument toDoc(BookChapters bookChapters) {
        BookChaptersDocument doc = new BookChaptersDocument();
        doc.setId(bookChapters.getId());
        doc.setChapterName(bookChapters.getChapterName());
        doc.setChapterNumber(bookChapters.getChapterNumber());
        doc.setAudioUrl(bookChapters.getAudioUrl());
        return doc;
    }
}
