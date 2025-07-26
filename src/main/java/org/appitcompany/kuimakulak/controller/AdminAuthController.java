package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.admin.AdminLoginRequest;
import org.appitcompany.kuimakulak.dto.admin.AdminPasswordChangeRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.dto.auth.TokenRefreshRequest;
import org.appitcompany.kuimakulak.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/admin")
public class AdminAuthController {

    private final AdminService adminService;

    @PostMapping("/login")
    @Operation(summary = "Login endpoint for the admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged in and returned the token"),
            @ApiResponse(responseCode = "401", description = "Invalid login credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthResponse> adminLogin(
            @Valid @RequestBody AdminLoginRequest adminLoginRequest
    ) {
        return ResponseEntity.ok(adminService.adminLogin(adminLoginRequest));
    }

    @PostMapping("/token/refresh")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Refresh JWT access token using a refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed token"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token request"),
            @ApiResponse(responseCode = "403", description = "Refresh token is invalid or expired")
    })
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        AuthResponse response = adminService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Logout admin by blacklisting access token and deleting refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Logout failed due to bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader,
                                       @Valid @RequestBody TokenRefreshRequest request) {
        adminService.logout(authHeader, request.refreshToken());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change admin password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully changed the password"),
            @ApiResponse(responseCode = "400", description = "Logout failed due to bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody AdminPasswordChangeRequest request) {
        adminService.changePassword(authentication, request);
        return ResponseEntity.ok().build();
    }


}
