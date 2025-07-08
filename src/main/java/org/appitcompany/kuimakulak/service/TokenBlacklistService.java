package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.component.JwtUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredTokens() {
        blacklistedTokens.removeIf(token -> {
            try {
                return jwtUtil.extractExpiration(token).before(new Date());
            } catch (Exception e) {
                return true;
            }
        });
    }
}