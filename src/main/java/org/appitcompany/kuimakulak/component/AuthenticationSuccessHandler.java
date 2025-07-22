package org.appitcompany.kuimakulak.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.appitcompany.kuimakulak.dto.Auth.AuthResponse;
import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.service.impl.OAuth2ServiceImpl;
import org.appitcompany.kuimakulak.service.impl.RefreshTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@AllArgsConstructor
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final OAuth2ServiceImpl oAuth2ServiceImpl;
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();

            LOGGER.info("OAuth2 authentication successful for user: {} with provider: {}", oauth2User.getName(), registrationId);


            User appUser = oAuth2ServiceImpl.processOAuth2User(registrationId, oauth2User);


            String jwt = jwtUtil.generateAccessToken(appUser);


            RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(appUser.getEmail());


            AuthResponse authResponse = new AuthResponse(jwt, refreshToken.getToken());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authResponse));
            response.getWriter().flush();

            clearAuthenticationAttributes(request);
            return;
        }


        super.onAuthenticationSuccess(request, response, authentication);
    }
}
