package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenService {
    @Transactional
    RefreshToken createRefreshToken(String userEmail);

    @Transactional
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    AuthResponse refreshAccessToken(String refreshToken);

    @Transactional
    void deleteByToken(String token);
}
