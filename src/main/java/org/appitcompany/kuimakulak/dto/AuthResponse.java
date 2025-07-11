package org.appitcompany.kuimakulak.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken,
                           String refreshToken) {

}
