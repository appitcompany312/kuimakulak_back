package org.appitcompany.kuimakulak.component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.TokenExpirationException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private final ResourceLoader resourceLoader;

    @Value("${apple.team-id:}")
    private String appleTeamId;

    @Value("${apple.client-id:}")
    private String appleServicesId;

    @Value("${apple.key-id:}")
    private String appleKeyId;

    @Value("${apple.private-key-path:}")
    private String applePrivateKeyPath;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-token.expiration-ms}")
    private long refreshTokenExpirationMs;

    private PrivateKey applePrivateKey;
    private SecretKey appJwtSigningKey;
    private JwtParser jwtParser;

    public JwtUtil(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initializeKeys() {
        this.appJwtSigningKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(appJwtSigningKey).build();

        if (applePrivateKeyPath != null && !applePrivateKeyPath.isEmpty() && appleTeamId != null && !appleTeamId.isEmpty()) {
            try {
                Resource resource = resourceLoader.getResource(applePrivateKeyPath);
                if (resource.exists()) {
                    PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()));
                    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                    Object parsedObject = pemParser.readObject();
                    if (parsedObject instanceof PrivateKeyInfo) {
                        this.applePrivateKey = converter.getPrivateKey((PrivateKeyInfo) parsedObject);
                    } else if (parsedObject instanceof org.bouncycastle.openssl.PEMKeyPair) {
                        this.applePrivateKey = converter.getPrivateKey(((org.bouncycastle.openssl.PEMKeyPair) parsedObject).getPrivateKeyInfo());
                    } else {
                        LOGGER.error("Failed to load Apple private key: Unexpected object type in PEM file - {}", parsedObject != null ? parsedObject.getClass().getName() : "null");
                        throw new RuntimeException("Unexpected object in Apple private key PEM file.");
                    }
                    pemParser.close();
                    LOGGER.info("Apple private key loaded successfully.");
                } else {
                    LOGGER.warn("Apple private key file not found at path: {}", applePrivateKeyPath);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load Apple private key from path {}: {}", applePrivateKeyPath, e.getMessage(), e);
            }
        } else {
            LOGGER.info("Apple private key config incomplete. Apple client secret generation will not be available.");
        }
    }

    public String generateAppleClientSecret() {
        if (applePrivateKey == null || appleKeyId == null || appleTeamId == null || appleServicesId == null || appleServicesId.isEmpty()) {
            throw new IllegalStateException("Missing configuration for Apple client secret generation.");
        }
        Date now = new Date();
        Date expiration = new Date(now.getTime() + (5 * 60 * 1000));

        return Jwts.builder()
                .setHeaderParam("kid", appleKeyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(appleTeamId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setAudience("https://appleid.apple.com")
                .setSubject(appleServicesId)
                .signWith(applePrivateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateAccessToken(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User or user email is null.");
        }
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("username", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(appJwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException("User or user email is null.");
        }
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(appJwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new TokenExpirationException("Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
