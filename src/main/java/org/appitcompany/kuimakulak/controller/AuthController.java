package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.dto.auth.TokenRefreshRequest;
import org.appitcompany.kuimakulak.service.OAuth2Service;
import org.appitcompany.kuimakulak.service.RefreshTokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final OAuth2Service oAuth2Service;

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

    @PostMapping("/token/refresh")
    @Operation(summary = "Refresh JWT access token using a refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token request"),
            @ApiResponse(responseCode = "403", description = "Refresh token is invalid or expired")
    })
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        AuthResponse response = refreshTokenService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user by blacklisting access token and deleting refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Logout failed due to bad request")
    })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader,
                                       @Valid @RequestBody TokenRefreshRequest request) {
        oAuth2Service.logout(authHeader, request.refreshToken());
        return ResponseEntity.ok().build();
    }

}
