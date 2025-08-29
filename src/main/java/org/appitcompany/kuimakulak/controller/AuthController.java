package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.auth.AppleLoginRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.dto.auth.TokenRefreshRequest;
import org.appitcompany.kuimakulak.service.OAuth2Service;
import org.appitcompany.kuimakulak.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private static final String GOOGLE_PROVIDER = "google";
    private static final String APPLE_PROVIDER = "apple";
    private final RefreshTokenService refreshTokenService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/login/google")
    @Operation(summary = "Login with Google account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned AuthResponse"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired")


    })
    public ResponseEntity<AuthResponse> googleLogin(
            @RequestParam("googleAccessToken") String googleAccessToken) {
        return oAuth2Service.handleGoogleLogin(googleAccessToken, GOOGLE_PROVIDER);
    }

    @PostMapping("/login/apple")
    @Operation(summary = "Login with Apple account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned AuthResponse"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired")


    })
    public ResponseEntity<AuthResponse> googleApple(@RequestBody @Valid AppleLoginRequest request) {
        return oAuth2Service.appleLogin(request, APPLE_PROVIDER);
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
