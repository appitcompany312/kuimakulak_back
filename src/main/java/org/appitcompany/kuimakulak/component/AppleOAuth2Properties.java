package org.appitcompany.kuimakulak.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "apple")
public class AppleOAuth2Properties {
    private String clientId;
    private String redirectUri;
    private String scope;
    private String authorizationUri;
    private String tokenUri;
    private String jwkSetUri;
}