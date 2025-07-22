package org.appitcompany.kuimakulak.dto.Auth;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken) {

}
