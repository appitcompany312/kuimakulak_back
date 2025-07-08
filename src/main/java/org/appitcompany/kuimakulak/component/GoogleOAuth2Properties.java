package org.appitcompany.kuimakulak.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "google")
public class GoogleOAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String jwkSetUri;
}