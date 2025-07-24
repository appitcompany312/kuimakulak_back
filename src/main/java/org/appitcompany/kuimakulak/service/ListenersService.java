package org.appitcompany.kuimakulak.service;

import org.springframework.http.ResponseEntity;

public interface ListenersService {
    ResponseEntity<?> addListeners(Long bookId);
}
