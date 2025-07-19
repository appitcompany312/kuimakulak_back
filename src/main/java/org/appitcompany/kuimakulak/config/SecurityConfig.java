package org.appitcompany.kuimakulak.config;

import lombok.extern.slf4j.Slf4j;
import org.appitcompany.kuimakulak.component.AppleOAuth2Properties;
import org.appitcompany.kuimakulak.component.AuthenticationSuccessHandler;
import org.appitcompany.kuimakulak.component.GoogleOAuth2Properties;
import org.appitcompany.kuimakulak.component.JwtAuthenticationFilter;
import org.appitcompany.kuimakulak.component.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({GoogleOAuth2Properties.class, AppleOAuth2Properties.class})
@Slf4j
public class SecurityConfig {

    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;
    private final GoogleOAuth2Properties googleProps;
    private final AppleOAuth2Properties appleProps;
    private static final String REGISTRATION_ID_GOOGLE = "google";
    private static final String REGISTRATION_ID_APPLE = "apple";

    public SecurityConfig(AuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtUtil jwtUtil,
                          GoogleOAuth2Properties googleProps,
                          AppleOAuth2Properties appleProps) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtUtil = jwtUtil;
        this.googleProps = googleProps;
        this.appleProps = appleProps;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login/**", "/api/auth/token/refresh").permitAll()
                        .requestMatchers("/oauth2/**",
                                "api/book/save",//delete later
                                "api/genre/save",//delete later
                                "api/book/getBookIsSoon",//delete later
                                "api/book/getBookForGenre",//delete later
                                "api/contributor/save",//delete later
                                "/login/oauth2/code/*").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository)
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                buildGoogleClientRegistration(),
                buildAppleClientRegistration()
        );
    }

    private ClientRegistration buildGoogleClientRegistration() {
        String resolvedRedirectUri = googleProps.getRedirectUri()
                .replace("{baseUrl}", "http://localhost:8080")
                .replace("{registrationId}", REGISTRATION_ID_GOOGLE);

        List<String> scopes = Arrays.stream(googleProps.getScope().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        log.info("Configuring Google OAuth2 Client with clientId: {}", googleProps.getClientId());

        return ClientRegistration.withRegistrationId(REGISTRATION_ID_GOOGLE)
                .clientId(googleProps.getClientId())
                .clientSecret(googleProps.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(resolvedRedirectUri)
                .scope(scopes)
                .authorizationUri(googleProps.getAuthorizationUri())
                .tokenUri(googleProps.getTokenUri())
                .userInfoUri(googleProps.getUserInfoUri())
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(googleProps.getJwkSetUri())
                .clientName("Google")
                .build();
    }

    private ClientRegistration buildAppleClientRegistration() {
        String resolvedRedirectUri = appleProps.getRedirectUri()
                .replace("{baseUrl}", "http://localhost:8080")
                .replace("{registrationId}", REGISTRATION_ID_APPLE);

        List<String> scopes = Arrays.stream(appleProps.getScope().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        log.info("Configuring Apple OAuth2 Client with clientId: {}", appleProps.getClientId());

        return ClientRegistration.withRegistrationId(REGISTRATION_ID_APPLE)
                .clientId(appleProps.getClientId())
                .clientSecret(jwtUtil.generateAppleClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(resolvedRedirectUri)
                .scope(scopes)
                .authorizationUri(appleProps.getAuthorizationUri())
                .tokenUri(appleProps.getTokenUri())
                .jwkSetUri(appleProps.getJwkSetUri())
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientName("Apple")
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

