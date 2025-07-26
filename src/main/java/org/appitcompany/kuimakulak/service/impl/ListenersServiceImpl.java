package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.document.BookDocument;
import org.appitcompany.kuimakulak.entity.Book;
import org.appitcompany.kuimakulak.entity.Listeners;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.NotFoundException;
import org.appitcompany.kuimakulak.repository.BookDocRepo;
import org.appitcompany.kuimakulak.repository.BookRepo;
import org.appitcompany.kuimakulak.repository.ListenersRepo;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.appitcompany.kuimakulak.service.ListenersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenersServiceImpl implements ListenersService {
    private final BookRepo bookRepo;
    private final BookDocRepo bookDocRepo;
    private final ListenersRepo listenersRepo;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public ResponseEntity<?> addListeners(Long bookId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByEmailOrElseThrow(email);
        Book book = bookRepo.getBookByBookIdOrElseThrow(bookId);
        BookDocument bookDocument = bookDocRepo.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Book document not found"));
        Listeners listeners = book.getListeners();
        if(listeners.getUsers().contains(user)) {
            return ResponseEntity.ok("listener already exists");
        }
        listeners.getUsers().add(user);
        listeners.setCountListeners(listeners.getCountListeners() + 1);
        listenersRepo.save(listeners);
        book.setListeners(listeners);
        bookRepo.save(book);
        bookDocument.setListenerCount(book.getListeners().getCountListeners());
        bookDocRepo.save(bookDocument);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("listener added successfully");
    }
}
