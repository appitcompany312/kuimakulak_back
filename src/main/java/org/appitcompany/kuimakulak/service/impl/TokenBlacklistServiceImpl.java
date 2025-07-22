package org.appitcompany.kuimakulak.service.impl;

import org.appitcompany.kuimakulak.component.JwtUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistServiceImpl implements org.appitcompany.kuimakulak.service.TokenBlacklistService {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final JwtUtil jwtUtil;

    public TokenBlacklistServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    @Override
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