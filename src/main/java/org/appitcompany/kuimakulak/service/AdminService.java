package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.admin.AdminLoginRequest;
import org.appitcompany.kuimakulak.dto.admin.AdminPasswordChangeRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AdminService {
    AuthResponse adminLogin(AdminLoginRequest adminLoginRequest);

    void logout(String authHeader, String s);

    void changePassword(Authentication authentication, AdminPasswordChangeRequest request);

    AuthResponse refreshAccessToken(String s);
}
