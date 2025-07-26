package org.appitcompany.kuimakulak.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface TokenBlacklistService {
    void blacklistToken(String token);

    boolean isTokenBlacklisted(String token);

    @Scheduled(fixedRate = 300000)
    void cleanupExpiredTokens();
}
