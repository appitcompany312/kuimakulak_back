package org.appitcompany.kuimakulak.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.component.JwtUtil;
import org.appitcompany.kuimakulak.dto.auth.AppleLoginRequest;
import org.appitcompany.kuimakulak.dto.auth.AuthResponse;
import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.enums.Role;
import org.appitcompany.kuimakulak.jpaRepository.UserRepository;
import org.appitcompany.kuimakulak.service.RefreshTokenService;
import org.appitcompany.kuimakulak.service.TokenBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements org.appitcompany.kuimakulak.service.OAuth2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ServiceImpl.class);

    @Value("${user.default.avatar}")
    private String userDefaultImage;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;

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
    public ResponseEntity<AuthResponse> handleGoogleLogin(String googleAccessToken, String registrationId) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + googleAccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> attributes = response.getBody();

        if (attributes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String oauthId = (String) attributes.get("sub");
        String pictureUrl = (String) attributes.get("picture");

        Optional<User> existingUserOptional;

        if ("google".equalsIgnoreCase(registrationId)) {
            existingUserOptional = userRepository.findByGoogleId(oauthId);
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
            user.setGoogleId(oauthId);

        }

        userRepository.save(user);

        AuthResponse authResponse = generateAuthResponse(user);

        return ResponseEntity.ok(authResponse);
    }

    @Override
    public ResponseEntity<AuthResponse> appleLogin(AppleLoginRequest request, String appleProvider) {
        try {
            User appleUser = parseAppleIdToken(request.idToken());

            String oauthId = appleUser.getAppleId();
            String email = appleUser.getEmail();
            String firstName = request.firstName();
            String lastName = request.lastName();

            Optional<User> userOpt = userRepository.findByAppleId(oauthId);
            if (userOpt.isEmpty() && email != null) {
                userOpt = userRepository.findByEmail(email);
            }

            User user;
            if (userOpt.isPresent()) {

                user = userOpt.get();
                LOGGER.info("Updating existing user: {} from provider: apple", email);

                if (firstName != null && user.getFirstName() == null) {
                    user.setFirstName(firstName);
                }
                if (lastName != null && user.getLastName() == null) {
                    user.setLastName(lastName);
                }
            } else {

                LOGGER.info("Creating new user: {} from provider: apple", email);
                user = new User();

                user.setEmail(Objects.requireNonNullElseGet(email, () -> "apple-" + oauthId + "@placeholder.local"));
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setImageUrl(userDefaultImage);
                user.setJoinedDate(LocalDate.now());
                user.setRole(Role.USER);
                user.setAppleId(oauthId);
            }

            userRepository.save(user);

            AuthResponse response = generateAuthResponse(user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOGGER.error("Apple login failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        String jwt = jwtUtil.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return new AuthResponse(jwt, refreshToken.getToken());
    }

    public User parseAppleIdToken(String idToken) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        String email = claims.getStringClaim("email");
        String sub = claims.getSubject();

        User user = new User();
         user.setEmail(email);
         user.setAppleId(sub);

        return user;
    }

}
