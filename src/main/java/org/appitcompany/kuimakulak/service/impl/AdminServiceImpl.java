package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.component.JwtUtil;
import org.appitcompany.kuimakulak.dto.admin.AdminLoginRequest;
import org.appitcompany.kuimakulak.dto.admin.AdminPasswordChangeRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.enums.Role;
import org.appitcompany.kuimakulak.exceptions.TokenNotFoundException;
import org.appitcompany.kuimakulak.exceptions.UnauthorizedException;
import org.appitcompany.kuimakulak.jpaRepository.UserRepository;
import org.appitcompany.kuimakulak.service.AdminService;
import org.appitcompany.kuimakulak.service.RefreshTokenService;
import org.appitcompany.kuimakulak.service.TokenBlacklistService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse adminLogin(AdminLoginRequest adminLoginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            adminLoginRequest.email(),
                            adminLoginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException e) {
            throw new DisabledException("User account is disabled");
        } catch (LockedException e) {
            throw new LockedException("User account is locked");
        }

        var user = userRepository.findByEmail(adminLoginRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied: Admin privileges required");
        }

        var jwtToken = jwtUtil.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(jwtToken, refreshToken.getToken());
    }


    @Override
    public void logout(String authHeader, String refreshToken) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Invalid Authorization header");
            }
            String accessToken = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(accessToken);
            refreshTokenService.deleteByToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Logout failed: " + e.getMessage());
        }
    }

    @Override
    public void changePassword(Authentication authentication, AdminPasswordChangeRequest request) {
        User user = getUserFromAuthentication(authentication);

        User currentUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());

        currentUser.setPassword(encodedNewPassword);
        userRepository.save(currentUser);


    }

    @Override
    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .map(this.refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found in database!"));

        User user = token.getUser();
        String newJwt = jwtUtil.generateAccessToken(user);

        return new AuthResponse(newJwt, refreshToken);
    }

    private User getUserFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User appUser) {
                return appUser;
            } else {
                throw new IllegalArgumentException("Principal is not an instance of AppUser");
            }
        }
        throw new UnauthorizedException("Authentication required!");
    }
}
