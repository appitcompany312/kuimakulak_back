package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.service.ListenersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/listeners")
@RequiredArgsConstructor
public class ListenersController {
  private final ListenersService listenersService;
    @Secured("USER")
    @Operation(summary = "When the gan player button is pressed, the gan question is answered. ")
    @PostMapping("/add")
    public ResponseEntity<?> addListeners(@RequestParam Long bookId){
     return listenersService.addListeners(bookId);
    }
}
