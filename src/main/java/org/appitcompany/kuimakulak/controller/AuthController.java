package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login/google")
    @Operation(summary = "Initiate Google Login (Redirects to Google)")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to Google Authentication")
    })
    public ResponseEntity<Void> googleLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/google"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/login/apple")
    @Operation(summary = "Initiate Apple Login (Redirects to Apple)")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Redirect to Apple Authentication")
    })
    public ResponseEntity<Void> initiateAppleLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/apple"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


}
