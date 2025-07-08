package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.component.JwtUtil;
import org.appitcompany.kuimakulak.dto.AuthResponse;
import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.TokenNotFoundException;
import org.appitcompany.kuimakulak.repository.RefreshTokenRepository;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RefreshToken createRefreshToken(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtUtil.generateRefreshToken(user));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);

    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = findByToken(refreshToken)
                .map(this::verifyExpiration)
                .orElseThrow(() -> new TokenNotFoundException("Refresh token not found in database!"));

        User user = token.getUser();
        String newJwt = jwtUtil.generateAccessToken(user);

        return new AuthResponse(newJwt, refreshToken);
    }


    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
