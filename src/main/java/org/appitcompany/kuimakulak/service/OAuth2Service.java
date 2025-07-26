package org.appitcompany.kuimakulak.service;

import jakarta.transaction.Transactional;
import org.appitcompany.kuimakulak.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {

    User processOAuth2User(String registrationId, OAuth2User oAuth2User);

    void logout(String authHeader, String refreshToken);
}
