package org.appitcompany.kuimakulak.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.enums.Role;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.appitcompany.kuimakulak.service.RefreshTokenService;
import org.appitcompany.kuimakulak.service.TokenBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements org.appitcompany.kuimakulak.service.OAuth2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public User processOAuth2User(String registrationId, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String oauthId = oAuth2User.getName();
        String pictureUrl = (String) attributes.get("picture");

        Optional<User> existingUserOptional;

        if ("google".equalsIgnoreCase(registrationId)) {
            existingUserOptional = userRepository.findByGoogleId(oauthId);
            if (existingUserOptional.isEmpty() && email != null) {
                existingUserOptional = userRepository.findByEmail(email);
            }
        } else if ("apple".equalsIgnoreCase(registrationId)) {
            existingUserOptional = userRepository.findByAppleId(oauthId);
            if (existingUserOptional.isEmpty() && email != null) {
                existingUserOptional = userRepository.findByEmail(email);
            }
        } else {
            LOGGER.warn("Unsupported OAuth2 registrationId: {}", registrationId);
            existingUserOptional = (email != null) ? userRepository.findByEmail(email) : Optional.empty();
        }

        User user;
        if (existingUserOptional.isPresent()) {
            user = existingUserOptional.get();
            LOGGER.info("Updating existing user: {} from provider: {}", email, registrationId);

            if (firstName != null) user.setFirstName(firstName);
            if (lastName != null) user.setLastName(lastName);

        } else {
            LOGGER.info("Creating new user: {} from provider: {}", email, registrationId);
            user = new User();

            user.setEmail(Objects.requireNonNullElseGet(email, () -> registrationId + "-" + oauthId + "@placeholder.local"));

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(pictureUrl);
            user.setJoinedDate(LocalDate.now());
            user.setRole(Role.USER);

            if ("google".equalsIgnoreCase(registrationId)) {
                user.setGoogleId(oauthId);
            } else if ("apple".equalsIgnoreCase(registrationId)) {
                user.setAppleId(oauthId);
            }
        }

        return userRepository.save(user);
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
}
