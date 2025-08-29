package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.auth.AppleLoginRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface OAuth2Service {


    void logout(String authHeader, String refreshToken);

    ResponseEntity<AuthResponse> handleGoogleLogin(String googleAccessToken, String googleProvider);

    ResponseEntity<AuthResponse> appleLogin(AppleLoginRequest googleAccessToken, String appleProvider);
}
