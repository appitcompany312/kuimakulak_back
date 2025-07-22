package org.appitcompany.kuimakulak.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken) {

}
